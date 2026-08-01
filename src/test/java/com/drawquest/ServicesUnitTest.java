package com.drawquest;

import com.drawquest.dtos.DrawingCreateDTO;
import com.drawquest.dtos.DrawingResponseDTO;
import com.drawquest.dtos.QuestCreateDTO;
import com.drawquest.dtos.QuestResponseDTO;
import com.drawquest.dtos.QuestUpdateDTO;
import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.dtos.UserLoginDTO;
import com.drawquest.dtos.UserResponseDTO;
import com.drawquest.enums.ERole;
import com.drawquest.exceptions.DuplicateResourceException;
import com.drawquest.exceptions.ResourceNotFoundException;
import com.drawquest.exceptions.UnauthorizedException;
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
import com.drawquest.security.JwtUtil;
import com.drawquest.services.impl.AuthServiceImpl;
import com.drawquest.services.impl.DrawingServiceImpl;
import com.drawquest.services.impl.ProgressServiceImpl;
import com.drawquest.services.impl.QuestServiceImpl;
import com.drawquest.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicesUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private DrawingRepository drawingRepository;

    @Mock
    private QuestRepository questRepository;

    @Mock
    private ProgressRepository progressRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private DrawingServiceImpl drawingService;

    @InjectMocks
    private QuestServiceImpl questService;

    @InjectMocks
    private ProgressServiceImpl progressService;

    @Test
    void authenticateReturnsJwtWhenPasswordMatches() {
        User user = user(1L, "alice", "hashed", ERole.ROLE_USER);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret123", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken("alice")).thenReturn("jwt-token");

        String token = authService.authenticate(new UserLoginDTO(null, "alice", "secret123"));

        assertThat(token).isEqualTo("jwt-token");
    }

    @Test
    void authenticateRejectsUnknownUserOrWrongPassword() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.authenticate(new UserLoginDTO(null, "missing", "secret123")))
                .isInstanceOf(UnauthorizedException.class);

        User user = user(1L, "alice", "hashed", ERole.ROLE_USER);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> authService.authenticate(new UserLoginDTO(null, "alice", "wrong")))
                .isInstanceOf(UnauthorizedException.class);

        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    void createUserEncodesPasswordAndAssignsDefaultRole() {
        Role role = new Role(ERole.ROLE_USER);
        when(passwordEncoder.encode("secret123")).thenReturn("encoded-password");
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        UserCreateDTO dto = new UserCreateDTO(null, "alice", "secret123", "alice@example.com");
        UserResponseDTO response = userService.createUser(dto);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getUsername()).isEqualTo("alice");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("encoded-password");
        assertThat(userCaptor.getValue().getRoles()).extracting(Role::getName).containsExactly(ERole.ROLE_USER);
    }

    @Test
    void createUserMapsDuplicateDatabaseErrors() {
        when(passwordEncoder.encode("secret123")).thenReturn("encoded-password");
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        UserCreateDTO dto = new UserCreateDTO(null, "alice", "secret123", "alice@example.com");

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void createDrawingCreatesProgressWhenMissingAndIncrementsAttempts() {
        User user = user(1L, "alice", "hashed", ERole.ROLE_USER);
        Quest quest = quest(2L, "Draw a tree", 40);
        Drawing savedDrawing = drawing(3L, user, quest, "https://example.com/tree.png", false);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(questRepository.findById(2L)).thenReturn(Optional.of(quest));
        when(progressRepository.findByUserIdAndQuestId(1L, 2L)).thenReturn(Optional.empty());
        when(drawingRepository.save(any(Drawing.class))).thenReturn(savedDrawing);

        DrawingCreateDTO dto = new DrawingCreateDTO(2L, "https://example.com/tree.png");
        DrawingResponseDTO response = drawingService.createDrawing(dto, "alice");

        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getImageUrl()).isEqualTo("https://example.com/tree.png");

        ArgumentCaptor<Progress> progressCaptor = ArgumentCaptor.forClass(Progress.class);
        verify(progressRepository).save(progressCaptor.capture());
        assertThat(progressCaptor.getValue().getAttempts()).isEqualTo(1);
        assertThat(progressCaptor.getValue().getUser()).isSameAs(user);
        assertThat(progressCaptor.getValue().getQuest()).isSameAs(quest);
    }

    @Test
    void approveDrawingAwardsXpOnlyWhenProgressIsNotCompleted() {
        User user = user(1L, "alice", "hashed", ERole.ROLE_USER);
        user.setXp(80);
        Quest quest = quest(2L, "Draw a tree", 40);
        Drawing drawing = drawing(3L, user, quest, "https://example.com/tree.png", false);
        Progress progress = progress(4L, user, quest, false, 1);
        when(drawingRepository.findById(3L)).thenReturn(Optional.of(drawing));
        when(progressRepository.findByUserIdAndQuestId(1L, 2L)).thenReturn(Optional.of(progress));
        when(drawingRepository.save(drawing)).thenReturn(drawing);

        DrawingResponseDTO response = drawingService.approveDrawing(3L);

        assertThat(response.isApproved()).isTrue();
        assertThat(user.getXp()).isEqualTo(120);
        assertThat(user.getLevel()).isEqualTo(1);
        assertThat(progress.isCompleted()).isTrue();
        verify(userRepository).save(user);
        verify(progressRepository).save(progress);

        progress.setCompleted(true);
        drawingService.approveDrawing(3L);

        assertThat(user.getXp()).isEqualTo(120);
    }

    @Test
    void questServiceCreatesUpdatesDeletesAndMapsNotFound() {
        Quest savedQuest = quest(1L, "Draw a tower", 25);
        when(questRepository.save(any(Quest.class))).thenReturn(savedQuest);

        QuestResponseDTO created = questService.createQuest(new QuestCreateDTO("Draw a tower", "Test", 1, 25));

        assertThat(created.getTitle()).isEqualTo("Draw a tower");

        when(questRepository.findById(1L)).thenReturn(Optional.of(savedQuest));
        when(questRepository.save(savedQuest)).thenReturn(savedQuest);

        QuestResponseDTO updated = questService.updateQuest(1L, new QuestUpdateDTO("Draw a tall tower", "Updated", 2, 50));

        assertThat(updated.getTitle()).isEqualTo("Draw a tall tower");
        assertThat(updated.getXpReward()).isEqualTo(50);

        questService.deleteQuest(1L);
        verify(questRepository).delete(savedQuest);

        when(questRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> questService.getQuestById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void progressServiceReturnsOnlyRepositoryResultsForUsername() {
        User user = user(1L, "alice", "hashed", ERole.ROLE_USER);
        Quest quest = quest(2L, "Draw a tree", 40);
        Progress progress = progress(3L, user, quest, false, 2);
        when(progressRepository.findByUserUsername("alice")).thenReturn(List.of(progress));
        when(progressRepository.findByIdAndUserUsername(3L, "alice")).thenReturn(Optional.of(progress));
        when(progressRepository.findByIdAndUserUsername(4L, "alice")).thenReturn(Optional.empty());

        assertThat(progressService.getAllProgress("alice")).hasSize(1);
        assertThat(progressService.getProgressById(3L, "alice").getAttempts()).isEqualTo(2);
        assertThatThrownBy(() -> progressService.getProgressById(4L, "alice"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private User user(Long id, String username, String password, ERole roleName) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(username + "@example.com");
        user.getRoles().add(new Role(roleName));
        return user;
    }

    private Quest quest(Long id, String title, int xpReward) {
        Quest quest = new Quest();
        quest.setId(id);
        quest.setTitle(title);
        quest.setDescription("Test quest");
        quest.setDifficulty(1);
        quest.setXpReward(xpReward);
        return quest;
    }

    private Drawing drawing(Long id, User user, Quest quest, String imageUrl, boolean approved) {
        Drawing drawing = new Drawing();
        drawing.setId(id);
        drawing.setUser(user);
        drawing.setQuest(quest);
        drawing.setImageUrl(imageUrl);
        drawing.setApproved(approved);
        return drawing;
    }

    private Progress progress(Long id, User user, Quest quest, boolean completed, int attempts) {
        Progress progress = new Progress();
        progress.setId(id);
        progress.setUser(user);
        progress.setQuest(quest);
        progress.setCompleted(completed);
        progress.setAttempts(attempts);
        return progress;
    }
}
