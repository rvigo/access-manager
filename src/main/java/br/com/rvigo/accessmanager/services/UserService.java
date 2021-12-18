package br.com.rvigo.accessmanager.services;

import br.com.rvigo.accessmanager.dtos.UserDTO;
import br.com.rvigo.accessmanager.entities.User;
import br.com.rvigo.accessmanager.repositories.UserRepository;
import br.com.rvigo.accessmanager.security.exceptions.NoSuchUserException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(NoSuchUserException::new);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(NoSuchUserException::new);
    }

    //password is encrypted by client
    public UserDTO createNewUser(UserDTO userDTO) {
        User createdUser = userRepository.save(new User(userDTO));
        log.info(format("User %s was created", createdUser.getUsername()));
        return new UserDTO(createdUser);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User incomingUser = new User(userDTO);
        User currentUser = userRepository.findById(incomingUser.getId())
                .orElseThrow(NoSuchUserException::new);

        if (currentUser.equals(incomingUser)) {
            log.warn("No changes! Nothing to do");
            return new UserDTO(incomingUser);
        } else {
            User updated = userRepository.save(incomingUser);
            log.info(format("User %s was updated", updated.getUsername()));
            return new UserDTO(updated);
        }
    }

    public void deleteUser(UserDTO userDTO) {
        User user = new User(userDTO);
        userRepository.deleteById(user.getId());
        log.info("The user was deleted!");
    }
}
