package com.drawquest;

import com.drawquest.enums.ERole;
import com.drawquest.models.Progress;
import com.drawquest.models.Quest;
import com.drawquest.models.Role;
import com.drawquest.models.User;
import com.drawquest.repositories.DrawingRepository;
import com.drawquest.repositories.ProgressRepository;
import com.drawquest.repositories.QuestRepository;
import com.drawquest.repositories.RoleRepository;
import com.drawquest.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:drawquest-test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never"
})
@AutoConfigureMockMvc
class DrawquestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private DrawingRepository drawingRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @BeforeEach
    void setUp() {
        drawingRepository.deleteAll();
        progressRepository.deleteAll();
        questRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        roleRepository.save(new Role(ERole.ROLE_USER));
        roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        roleRepository.save(new Role(ERole.ROLE_ADMIN));
    }

    @Test
    void registerStoresHashedPasswordAndLoginReturnsJwt() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "alice",
                                  "password": "secret123",
                                  "email": "alice@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"));

        User savedUser = userRepository.findByUsername("alice").orElseThrow();
        assertThat(savedUser.getPassword()).isNotEqualTo("secret123");
        assertThat(passwordEncoder.matches("secret123", savedUser.getPassword())).isTrue();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "alice",
                                  "password": "secret123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void userRoleCannotListUsersApproveDrawingsOrMutateQuests() throws Exception {
        String token = tokenFor(createUser("bob", "secret123", "bob@example.com", ERole.ROLE_USER), "secret123");

        mockMvc.perform(get("/users")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/drawings/1/approve")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/quests")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validQuestJson("Daily apple", 25)))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/quests/1")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validQuestJson("Updated apple", 25)))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/quests/1")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isForbidden());
    }

    @Test
    void approvingDrawingCompletesProgressAndAwardsXpOnlyOnce() throws Exception {
        User user = createUser("carol", "secret123", "carol@example.com", ERole.ROLE_USER);
        User admin = createUser("admin", "secret123", "admin@example.com", ERole.ROLE_ADMIN);
        Quest quest = createQuest("Draw a castle", 120);

        String userToken = tokenFor(user, "secret123");
        String adminToken = tokenFor(admin, "secret123");

        String createDrawingResponse = mockMvc.perform(post("/drawings")
                        .header("Authorization", bearer(userToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "questId": %d,
                                  "imageUrl": "https://example.com/castle.png"
                                }
                                """.formatted(quest.getId())))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long drawingId = objectMapper.readTree(createDrawingResponse).get("id").asLong();

        mockMvc.perform(put("/drawings/{id}/approve", drawingId)
                        .header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(true));

        mockMvc.perform(put("/drawings/{id}/approve", drawingId)
                        .header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findByUsername("carol").orElseThrow();
        Progress progress = progressRepository.findByUserIdAndQuestId(user.getId(), quest.getId()).orElseThrow();

        assertThat(updatedUser.getXp()).isEqualTo(120);
        assertThat(updatedUser.getLevel()).isEqualTo(1);
        assertThat(progress.isCompleted()).isTrue();
        assertThat(progress.getAttempts()).isEqualTo(1);
    }

    private User createUser(String username, String password, String email, ERole roleName) {
        Role role = roleRepository.findByName(roleName).orElseThrow();

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(new HashSet<>());
        user.getRoles().add(role);

        return userRepository.save(user);
    }

    private Quest createQuest(String title, int xpReward) {
        Quest quest = new Quest();
        quest.setTitle(title);
        quest.setDescription("A test quest");
        quest.setDifficulty(1);
        quest.setXpReward(xpReward);

        return questRepository.save(quest);
    }

    private String tokenFor(User user, String password) throws Exception {
        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "%s",
                                  "password": "%s"
                                }
                                """.formatted(user.getUsername(), password)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        return json.get("token").asText();
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    private String validQuestJson(String title, int xpReward) {
        return """
                {
                  "title": "%s",
                  "description": "Test description",
                  "difficulty": 1,
                  "xpReward": %d
                }
                """.formatted(title, xpReward);
    }
}
