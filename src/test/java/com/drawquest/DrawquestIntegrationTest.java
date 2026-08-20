package com.drawquest;

import com.drawquest.enums.ERole;
import com.drawquest.models.Drawing;
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
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:drawquest-test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never",
        "spring.flyway.enabled=false",
        "drawquest.cors.allowed-origins=http://localhost:5173",
        "drawquest.upload.drawings-dir=target/test-uploads/drawings",
        "drawquest.upload.drawings-public-path=/uploads/drawings"
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
    void setUp() throws Exception {
        drawingRepository.deleteAll();
        progressRepository.deleteAll();
        questRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        deleteDirectory(Path.of("target/test-uploads/drawings"));

        roleRepository.save(new Role(ERole.ROLE_USER));
        roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        roleRepository.save(new Role(ERole.ROLE_ADMIN));
    }

    @Test
    void auditableEntitiesPopulateTimestamps() {
        User user = createUser("audit_user", "secret123", "audit_user@example.com", ERole.ROLE_USER);
        Quest quest = createQuest("Draw a clock", 20);
        Drawing drawing = createDrawing(user, quest, "https://example.com/clock.png");
        Progress progress = createProgress(user, quest, 1);

        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getModifiedAt()).isNotNull();
        assertThat(quest.getCreatedAt()).isNotNull();
        assertThat(quest.getModifiedAt()).isNotNull();
        assertThat(drawing.getCreatedAt()).isNotNull();
        assertThat(drawing.getModifiedAt()).isNotNull();
        assertThat(progress.getCreatedAt()).isNotNull();
        assertThat(progress.getModifiedAt()).isNotNull();
    }

    @Test
    void actuatorHealthAndInfoArePublic() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());

        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk());
    }

    @Test
    void corsPreflightAllowsConfiguredFrontendOrigin() throws Exception {
        mockMvc.perform(options("/drawings")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(header().string("Access-Control-Allow-Methods", org.hamcrest.Matchers.containsString("POST")))
                .andExpect(header().string("Access-Control-Allow-Headers", org.hamcrest.Matchers.containsString("authorization")));
    }

    @Test
    void corsPreflightRejectsUnconfiguredOrigin() throws Exception {
        mockMvc.perform(options("/drawings")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void authEndpointsArePublicButProtectedEndpointsRequireValidToken() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "public_user",
                                  "password": "secret123",
                                  "email": "public_user@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("public_user"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "public_user",
                                  "password": "secret123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

        mockMvc.perform(get("/quests"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/quests")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
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
    void moderatorCanCreateAndUpdateQuestsButCannotDeleteThem() throws Exception {
        User moderator = createUser("moderator", "secret123", "moderator@example.com", ERole.ROLE_MODERATOR);
        String moderatorToken = tokenFor(moderator, "secret123");

        String createResponse = mockMvc.perform(post("/quests")
                        .header("Authorization", bearer(moderatorToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validQuestJson("Draw a lantern", 40)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Draw a lantern"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long questId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(put("/quests/{id}", questId)
                        .header("Authorization", bearer(moderatorToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validQuestJson("Draw a bright lantern", 55)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Draw a bright lantern"))
                .andExpect(jsonPath("$.xpReward").value(55));

        mockMvc.perform(delete("/quests/{id}", questId)
                        .header("Authorization", bearer(moderatorToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanDeleteQuests() throws Exception {
        User admin = createUser("quest_admin", "secret123", "quest_admin@example.com", ERole.ROLE_ADMIN);
        Quest quest = createQuest("Draw a mountain", 75);
        String adminToken = tokenFor(admin, "secret123");

        mockMvc.perform(delete("/quests/{id}", quest.getId())
                        .header("Authorization", bearer(adminToken)))
                .andExpect(status().isNoContent());

        assertThat(questRepository.findById(quest.getId())).isEmpty();
    }

    @Test
    void invalidQuestAndDrawingPayloadsReturnBadRequest() throws Exception {
        User admin = createUser("validation_admin", "secret123", "validation_admin@example.com", ERole.ROLE_ADMIN);
        User user = createUser("validation_user", "secret123", "validation_user@example.com", ERole.ROLE_USER);
        String adminToken = tokenFor(admin, "secret123");
        String userToken = tokenFor(user, "secret123");

        mockMvc.perform(post("/quests")
                        .header("Authorization", bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "",
                                  "description": "Invalid quest",
                                  "difficulty": 0,
                                  "xpReward": -1
                                }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));

        mockMvc.perform(post("/drawings")
                        .header("Authorization", bearer(userToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "questId": 1,
                                  "imageUrl": "not-a-url"
                                }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));

        mockMvc.perform(post("/drawings")
                        .header("Authorization", bearer(userToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "questId": 0,
                                  "imageUrl": "https://example.com/drawings/castle.png"
                                }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void createDrawingWithMultipartImageStoresFileAndExposesPublicUrl() throws Exception {
        User user = createUser("upload_user", "secret123", "upload_user@example.com", ERole.ROLE_USER);
        Quest quest = createQuest("Draw a cloud", 35);
        String token = tokenFor(user, "secret123");
        byte[] imageBytes = new byte[] { 1, 2, 3, 4 };
        MockMultipartFile image = new MockMultipartFile("image", "cloud.png", "image/png", imageBytes);

        String response = mockMvc.perform(multipart("/drawings")
                        .file(image)
                        .param("questId", quest.getId().toString())
                        .header("Authorization", bearer(token)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.imageUrl").value(org.hamcrest.Matchers.startsWith("/uploads/drawings/")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String imageUrl = objectMapper.readTree(response).get("imageUrl").asText();
        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        Path storedFile = Path.of("target/test-uploads/drawings").resolve(filename);

        assertThat(storedFile).exists();
        assertThat(Files.readAllBytes(storedFile)).containsExactly(imageBytes);

        mockMvc.perform(get(imageUrl))
                .andExpect(status().isOk());
    }

    @Test
    void multipartDrawingUploadRejectsInvalidFileType() throws Exception {
        User user = createUser("invalid_upload_user", "secret123", "invalid_upload_user@example.com", ERole.ROLE_USER);
        Quest quest = createQuest("Draw a river", 35);
        String token = tokenFor(user, "secret123");
        MockMultipartFile image = new MockMultipartFile("image", "notes.txt", "text/plain", "not an image".getBytes());

        mockMvc.perform(multipart("/drawings")
                        .file(image)
                        .param("questId", quest.getId().toString())
                        .header("Authorization", bearer(token)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void multipartDrawingUploadRejectsMissingImagePart() throws Exception {
        User user = createUser("missing_image_user", "secret123", "missing_image_user@example.com", ERole.ROLE_USER);
        Quest quest = createQuest("Draw a lake", 35);
        String token = tokenFor(user, "secret123");

        mockMvc.perform(multipart("/drawings")
                        .param("questId", quest.getId().toString())
                        .header("Authorization", bearer(token)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.image").value("Required request part is missing"));
    }

    @Test
    void updateDrawingImageWithMultipartReplacesImageUrl() throws Exception {
        User user = createUser("replace_image_user", "secret123", "replace_image_user@example.com", ERole.ROLE_USER);
        Quest quest = createQuest("Draw a moon", 45);
        Drawing drawing = createDrawing(user, quest, "https://example.com/old-moon.png");
        String token = tokenFor(user, "secret123");
        MockMultipartFile image = new MockMultipartFile("image", "moon.webp", "image/webp", new byte[] { 5, 6, 7 });

        mockMvc.perform(multipart("/drawings/{id}/image", drawing.getId())
                        .file(image)
                        .header("Authorization", bearer(token))
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").value(org.hamcrest.Matchers.startsWith("/uploads/drawings/")))
                .andExpect(jsonPath("$.imageUrl").value(org.hamcrest.Matchers.endsWith(".webp")));

        Drawing updatedDrawing = drawingRepository.findById(drawing.getId()).orElseThrow();
        assertThat(updatedDrawing.getImageUrl()).startsWith("/uploads/drawings/");
        assertThat(updatedDrawing.getImageUrl()).endsWith(".webp");
    }

    @Test
    void nonPositivePathIdsReturnBadRequest() throws Exception {
        User admin = createUser("param_admin", "secret123", "param_admin@example.com", ERole.ROLE_ADMIN);
        User user = createUser("param_user", "secret123", "param_user@example.com", ERole.ROLE_USER);
        String adminToken = tokenFor(admin, "secret123");
        String userToken = tokenFor(user, "secret123");

        mockMvc.perform(get("/quests/0")
                        .header("Authorization", bearer(userToken)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));

        mockMvc.perform(put("/drawings/0/approve")
                        .header("Authorization", bearer(adminToken)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));

        mockMvc.perform(get("/progress/0")
                        .header("Authorization", bearer(userToken)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void usersCanOnlyAccessTheirOwnDrawingsAndProgress() throws Exception {
        User owner = createUser("owner", "secret123", "owner@example.com", ERole.ROLE_USER);
        User other = createUser("other", "secret123", "other@example.com", ERole.ROLE_USER);
        Quest quest = createQuest("Draw a tree", 30);
        Drawing ownerDrawing = createDrawing(owner, quest, "https://example.com/owner-tree.png");
        Drawing otherDrawing = createDrawing(other, quest, "https://example.com/other-tree.png");
        Progress ownerProgress = createProgress(owner, quest, 2);
        Progress otherProgress = createProgress(other, quest, 1);
        String ownerToken = tokenFor(owner, "secret123");

        mockMvc.perform(get("/drawings")
                        .header("Authorization", bearer(ownerToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(ownerDrawing.getId()));

        mockMvc.perform(get("/drawings/{id}", otherDrawing.getId())
                        .header("Authorization", bearer(ownerToken)))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/drawings/{id}", otherDrawing.getId())
                        .header("Authorization", bearer(ownerToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "imageUrl": "https://example.com/stolen-update.png"
                                }
                                """))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/progress")
                        .header("Authorization", bearer(ownerToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(ownerProgress.getId()));

        mockMvc.perform(get("/progress/{id}", otherProgress.getId())
                        .header("Authorization", bearer(ownerToken)))
                .andExpect(status().isNotFound());
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

    private Drawing createDrawing(User user, Quest quest, String imageUrl) {
        Drawing drawing = new Drawing();
        drawing.setUser(user);
        drawing.setQuest(quest);
        drawing.setImageUrl(imageUrl);
        drawing.setApproved(false);

        return drawingRepository.save(drawing);
    }

    private Progress createProgress(User user, Quest quest, int attempts) {
        Progress progress = new Progress();
        progress.setUser(user);
        progress.setQuest(quest);
        progress.setAttempts(attempts);
        progress.setCompleted(false);

        return progressRepository.save(progress);
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

    private void deleteDirectory(Path path) throws Exception {
        if (!Files.exists(path)) {
            return;
        }

        try (var paths = Files.walk(path)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (Exception ex) {
                            throw new IllegalStateException("Could not clean test upload directory", ex);
                        }
                    });
        }
    }
}
