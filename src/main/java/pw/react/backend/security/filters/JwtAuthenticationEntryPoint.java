package pw.react.backend.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import pw.react.backend.exceptions.ExceptionDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.error("Unauthorized error: {}", authException.getMessage());
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errJson);
        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionDetails exceptionDetails = new ExceptionDetails(HttpStatus.UNAUTHORIZED, authException.getMessage());
        String errJson = objectMapper.writeValueAsString(exceptionDetails);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getOutputStream().write(errJson.getBytes(StandardCharsets.UTF_8));
    }
}
