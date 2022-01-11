package pw.react.backend.security.models;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ApiModel(description="Log in details.")
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    @ApiModelProperty(notes = "Username used to log in.", required = true)
    private String username;
    @ApiModelProperty(notes = "Password used to log in.", required = true)
    private String password;

}
