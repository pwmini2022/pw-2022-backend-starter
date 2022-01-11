package pw.react.backend.security.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@ApiModel(description = "Response containing value of the token.")
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    @ApiModelProperty(notes = "Token value.")
    private final String jwttoken;

}
