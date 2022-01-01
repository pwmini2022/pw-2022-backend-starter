package pw.react.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.models.User;

@RestController
@RequestMapping(path = "/users")
@Profile({"jwt"})
@Slf4j
public class JwtUserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path = "")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        log.info("Encoded password [{}]", encodedPassword);
        user.setId(1L);
        user.setPassword(encodedPassword);
        return ResponseEntity.ok(user);
    }
}
