package pw.react.backend.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import pw.react.backend.models.User;

public interface UserClient {
    User validateAndSave(User user);
    User updatePassword(User user, String password);
    void setPasswordEncoder(PasswordEncoder passwordEncoder);
}
