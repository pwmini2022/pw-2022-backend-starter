package pw.react.backend.models;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
}
