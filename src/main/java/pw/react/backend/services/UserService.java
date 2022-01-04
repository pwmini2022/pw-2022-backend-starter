package pw.react.backend.services;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import pw.react.backend.dao.UserRepository;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.User;

import java.util.Optional;

@Slf4j
public class UserService implements UserClient {

    private final UserRepository userRepository;
    @Setter
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User validateAndSave(User user) {
        if (isValidUser(user)) {
            log.info("User is valid");
            Optional<User> dbUser = userRepository.findByUsername(user.getUsername());
            if (dbUser.isPresent()) {
                log.info("User already exists. Updating it.");
                user.setId(dbUser.get().getId());
            }
            user = userRepository.save(user);
            log.info("User was saved.");
        }
        return user;
    }

    private boolean isValidUser(User user) {
        if (user != null) {
            if (!isValid(user.getUsername())) {
                log.error("Empty username.");
                throw new UserValidationException("Empty username.");
            }
            if (!isValid(user.getPassword())) {
                log.error("Empty user password.");
                throw new UserValidationException("Empty user password.");
            }
            if (!isValid(user.getEmail())) {
                log.error("UEmpty email.");
                throw new UserValidationException("Empty email.");
            }
            return true;
        }
        log.error("User is null.");
        throw new UserValidationException("User is null.");
    }

    private boolean isValid(String value) {
        return value != null && !value.isBlank();
    }

    @Override
    public User updatePassword(User user, String password) {
        if (isValidUser(user)) {
            if (passwordEncoder != null) {
                log.debug("Encoding password.");
                user.setPassword(passwordEncoder.encode(password));
            } else {
                log.debug("Password in plain text.");
                user.setPassword(password);
            }
            user = userRepository.save(user);
        }
        return user;
    }
}
