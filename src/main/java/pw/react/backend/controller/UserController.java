package pw.react.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.models.User;
import pw.react.backend.services.UserClient;

@RestController
@RequestMapping(path = "/users")
@Profile({"!jwt"})
@Slf4j
@Api(tags = "Users")
public class UserController {

    protected final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostMapping(path = "")
    @ApiOperation(value = "Creates a new user.", notes = "Returns newly created user.")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user = userClient.validateAndSave(user);
        log.info("Password is not going to be encoded");
        return ResponseEntity.ok(user);
    }
}
