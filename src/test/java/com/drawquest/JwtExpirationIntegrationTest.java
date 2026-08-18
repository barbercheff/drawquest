package com.drawquest;

import com.drawquest.enums.ERole;
import com.drawquest.models.Role;
import com.drawquest.models.User;
import com.drawquest.repositories.DrawingRepository;
import com.drawquest.repositories.ProgressRepository;
import com.drawquest.repositories.QuestRepository;
import com.drawquest.repositories.RoleRepository;
import com.drawquest.repositories.UserRepository;
import com.drawquest.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:drawquest-jwt-expiration-test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never",
        "spring.flyway.enabled=false"
})
@AutoConfigureMockMvc
@DirtiesContext
class JwtExpirationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        drawingRepository.deleteAll();
        progressRepository.deleteAll();
        questRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        roleRepository.save(new Role(ERole.ROLE_USER));
    }

    @Test
    void protectedEndpointRejectsExpiredToken() throws Exception {
        User user = createUser("expired_user", "secret123", "expired_user@example.com", ERole.ROLE_USER);
        ReflectionTestUtils.setField(jwtUtil, "expirationTime", -1000);

        String expiredToken = jwtUtil.generateToken(user.getUsername());

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
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
}
