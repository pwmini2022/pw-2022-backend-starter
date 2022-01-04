package pw.react.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pw.react.backend.models.User;
import pw.react.backend.services.UserClient;

@RestController
@RequestMapping(path = "/users")
@Profile({"!jwt"})
@Slf4j
public class UserController {

    protected final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostMapping(path = "")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user = userClient.validateAndSave(user);
        log.info("Password is not going to be encoded");
        return ResponseEntity.ok(user);
    }
}
