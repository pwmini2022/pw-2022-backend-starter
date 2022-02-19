package pw.react.backend.security.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;
import pw.react.backend.dao.UserRepository;
import pw.react.backend.security.filters.JwtAuthenticationEntryPoint;
import pw.react.backend.security.filters.JwtRequestFilter;
import pw.react.backend.security.services.JwtTokenService;
import pw.react.backend.security.services.JwtUserDetailsService;

@Configuration
@Profile("jwt")
public class JwtConfig {

    private final String jwtSecret;
    private final long jwtExpirationMs;

    public JwtConfig(@Value(value = "${jwt.secret}") String jwtSecret,
                     @Value(value = "${jwt.expirationMs}") long jwtExpirationMs
    ) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @Bean
    public JwtUserDetailsService jwtUserDetailsService(UserRepository userRepository) {
        return new JwtUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenService jwtTokenService() {
        return new JwtTokenService(jwtSecret, jwtExpirationMs);
    }

    @Bean
    public OncePerRequestFilter jwtRequestFilter(UserRepository userRepository) {
        return new JwtRequestFilter(jwtUserDetailsService(userRepository), jwtTokenService());
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
}
