package com.drawquest.controllers;

import com.drawquest.dtos.UserUpdateDTO;
import com.drawquest.models.User;
import com.drawquest.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para el controlador UserController.
 * Se usa Mockito para simular el servicio UserService.
 */
class UserControllerTest {

    // Simula el servicio de usuarios
    @Mock
    private UserService userService;

    // Inyecta el mock de UserService dentro del controlador que queremos probar
    @InjectMocks
    private UserController userController;

    /**
     * Configuración inicial antes de ejecutar cada test.
     * Abre los mocks para que puedan ser usados.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test para el método getAllUsers().
     * Verifica que se obtienen todos los usuarios correctamente.
     */
    @Test
    void testGetAllUsers() {
        // Simula una lista de usuarios como datos de prueba
        User mockUser1 = new User();
        mockUser1.setId(1L);
        mockUser1.setUsername("user1");
        mockUser1.setEmail("email1@example.com");
        mockUser1.setPassword("password1");

        User mockUser2 = new User();
        mockUser2.setId(2L);
        mockUser2.setUsername("user2");
        mockUser2.setEmail("email2@example.com");
        mockUser2.setPassword("password2");
        
        List<User> mockUsers = Arrays.asList(
                mockUser1,
                mockUser2
        );
        when(userService.getAllUsers()).thenReturn(mockUsers); // Simula la respuesta del servicio

        // Llama al método del controlador
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Verifica que la respuesta es correcta
        assertEquals(200, response.getStatusCodeValue()); // Debe devolver HTTP 200 OK
        assertEquals(2, response.getBody().size()); // La lista debe tener 2 usuarios
        verify(userService, times(1)).getAllUsers(); // Se debe llamar al servicio una vez
    }

    /**
     * Test para el método getUserById().
     * Verifica que se obtiene un usuario por su ID correctamente.
     */
    @Test
    void testGetUserById() {
        // Simula un usuario de prueba
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("user1");
        mockUser.setEmail("email1@example.com");
        mockUser.setPassword("password1");
        
        when(userService.getUserById(1L)).thenReturn(mockUser); // Simula la respuesta del servicio

        // Llama al método del controlador
        ResponseEntity<User> response = userController.getUserById(1L);

        // Verifica que la respuesta es correcta
        assertEquals(200, response.getStatusCodeValue()); // HTTP 200 OK
        assertNotNull(response.getBody()); // El cuerpo de la respuesta no debe ser null
        assertEquals("user1", response.getBody().getUsername()); // El nombre de usuario debe coincidir
        verify(userService, times(1)).getUserById(1L); // Se debe llamar al servicio una vez
    }

    /**
     * Test para el método createUser().
     * Verifica que se puede crear un usuario correctamente.
     */
    @Test
    void testCreateUser() {
        // Usuario a crear (sin ID)
        User newUser = new User();
        newUser.setId(null);
        newUser.setUsername("newUser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("newPass");

        // Usuario simulado después de guardarse (con ID asignado)
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newUser");
        savedUser.setEmail("new@example.com");
        savedUser.setPassword("newPass");

        // Mockeamos el objeto BindingResult para simular que no hay errores en la validación
        BindingResult mockBindingResult = mock(BindingResult.class);  // Creamos una instancia mock de BindingResult
        when(mockBindingResult.hasErrors()).thenReturn(false);  // Simulamos que no hay errores en la validación

        // Simulamos la llamada al servicio, que debería devolver el usuario guardado con un ID
        when(userService.createUser(any(User.class))).thenReturn(savedUser);  // Simulamos que el servicio crea un usuario correctamente

        // Llamamos al método del controlador, pasando el nuevo usuario y el resultado de validación simulado
        ResponseEntity<?> response = userController.createUser(newUser, mockBindingResult);  // Llamamos al controlador con los parámetros mockeados

        // Verificamos que la respuesta es la esperada
        assertEquals(201, response.getStatusCodeValue());  // Verificamos que el código de estado es 201 (CREATED)
        assertNotNull(response.getBody());  // Verificamos que el cuerpo de la respuesta no es nulo
        assertInstanceOf(User.class, response.getBody());  // Verificamos que el cuerpo es una instancia de User

        // Convertimos el cuerpo de la respuesta en un objeto User
        User createdUser = (User) response.getBody();
        assertEquals(1L, createdUser.getId());  // Verificamos que el ID del usuario creado es el esperado (1L)

        // Verificamos que el servicio fue llamado una vez
        verify(userService, times(1)).createUser(any(User.class));  // Verificamos que createUser fue llamado exactamente una vez con cualquier objeto de tipo User
    }

    /**
     * Test para el método updateUser().
     * Verifica que se puede actualizar un usuario correctamente.
     */
    @Test
    void testUpdateUser() {
        Long userId = 1L;
        // Datos que se van a actualizar
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setLevel(7);

        // Usuario simulado después de la actualización
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("user1");
        updatedUser.setEmail("newEmail@example.com");
        updatedUser.setPassword("newPassword");
        updatedUser.setLevel(7);

        // Mockeamos el objeto BindingResult para simular que no hay errores en la validación
        BindingResult mockBindingResult = mock(BindingResult.class);  // Creamos una instancia mock de BindingResult
        when(mockBindingResult.hasErrors()).thenReturn(false);  // Simulamos que no hay errores en la validación
        
        when(userService.updateUser(eq(userId), any(UserUpdateDTO.class))).thenReturn(updatedUser); // Simula la respuesta del servicio

        // Llama al método del controlador
        ResponseEntity<?> response = userController.updateUser(userId, updateDTO, mockBindingResult);

        // Verifica que la respuesta es correcta
        assertEquals(200, response.getStatusCodeValue()); // HTTP 200 OK
        assertNotNull((User) response.getBody()); // El cuerpo de la respuesta no debe ser null
        assertEquals(7, ((User)response.getBody()).getLevel()); // El nivel debe haber cambiado a 7
        verify(userService, times(1)).updateUser(eq(userId), any(UserUpdateDTO.class)); // Se debe llamar al servicio una vez
    }

    /**
     * Test para el método deleteUser().
     * Verifica que se puede eliminar un usuario correctamente.
     */
    @Test
    void testDeleteUser() {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId); // Simula la acción de eliminar sin errores

        // Llama al método del controlador
        ResponseEntity<Void> response = userController.deleteUser(userId);

        // Verifica que la respuesta es correcta
        assertEquals(204, response.getStatusCodeValue()); // HTTP 204 No Content (eliminado con éxito)
        verify(userService, times(1)).deleteUser(userId); // Se debe llamar al servicio una vez
    }
}
