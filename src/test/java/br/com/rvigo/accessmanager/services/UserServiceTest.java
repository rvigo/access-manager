package br.com.rvigo.accessmanager.services;

import br.com.rvigo.accessmanager.dtos.UserDTO;
import br.com.rvigo.accessmanager.entities.User;
import br.com.rvigo.accessmanager.repositories.UserRepository;
import br.com.rvigo.accessmanager.security.exceptions.NoSuchUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UserServiceTest {
    private UserService service;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private UserDTO userDTO;

    private static final UUID USER_ID = randomUUID();
    private static final String username = "testUser";
    private static final String encryptedPassword = "testPassword";

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(USER_ID);
        userDTO.setUsername(username);
        userDTO.setPassword(encryptedPassword);

        user = new User(userDTO);

        this.service = new UserService(userRepository);
    }

    @Test
    public void shouldCreateANewUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = service.createNewUser(userDTO);

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(username, result.getUsername());
        assertEquals(encryptedPassword, result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void shouldReturnAnUserSearchingByUUID() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        User resultUser = service.findUserById(USER_ID);

        verify(userRepository, times(1)).findById(any(UUID.class));
        assertEquals(user.getUsername(), resultUser.getUsername());
        assertEquals(user.getPassword(), resultUser.getPassword());
        verify(userRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    public void shouldThrowAnErrorIfUserDoesNotExistsWhileSearchingByUserName() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        NoSuchUserException exception = assertThrows(NoSuchUserException.class, () -> service.findUserByUsername(userDTO.getUsername()));

        verify(userRepository, times(1)).findUserByUsername(anyString());
        assertEquals("user not found", exception.getMessage());
    }

    @Test
    public void shouldThrowAnErrorIfUserDoesNotExistsWhileSearchingByUUID() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        NoSuchUserException exception = assertThrows(NoSuchUserException.class, () -> service.findUserById(USER_ID));

        verify(userRepository, times(1)).findById(any(UUID.class));
        assertEquals("user not found", exception.getMessage());
    }


    @Test
    public void shouldReturnAnUserSearchingByUserName() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        User resultUser = service.findUserByUsername(user.getUsername());

        verify(userRepository, times(1)).findUserByUsername(anyString());
        assertEquals(user.getUsername(), resultUser.getUsername());
        assertEquals(user.getPassword(), resultUser.getPassword());
    }

    @Test
    public void shouldUpdateAnUser() {
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setUsername("newTestUsername");
        newUserDTO.setPassword(userDTO.getPassword());
        newUserDTO.setId(user.getId());
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(new User(newUserDTO));

        UserDTO response = service.updateUser(newUserDTO);

        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(userRepository, times(1)).save(any(User.class));
        assertNotEquals(userDTO.getUsername(), response.getUsername());
        assertEquals(userDTO.getPassword(), response.getPassword());
        assertEquals(userDTO.getId(), response.getId());
    }

    @Test
    public void shouldDoNothingWhenUpdateAnUserWithNoChanges() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        UserDTO response = service.updateUser(userDTO);

        verify(userRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(userRepository);
        assertEquals(userDTO.getUsername(), response.getUsername());
        assertEquals(userDTO.getPassword(), response.getPassword());
        assertEquals(userDTO.getId(), response.getId());
    }

    @Test
    public void shouldDeleteAnUserByUserId() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        service.deleteUser(userDTO);

        verify(userRepository, times(1)).deleteById(any(UUID.class));
    }
}
