package pw.react.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.models.User;

@RestController
@RequestMapping(path = "/users")
@Profile({"!jwt"})
@Slf4j
public class UserController {

    @PostMapping(path = "")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Password is not going to be encoded");
        user.setId(2L);
        return ResponseEntity.ok(user);
    }
}
