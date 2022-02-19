package pw.react.backend.security.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "jwt")
@Profile("jwt")
public class JwtConfig {

    private String secret;
    private long expirationMs;

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
        return new JwtTokenService(secret, expirationMs);
    }

    @Bean
    public OncePerRequestFilter jwtRequestFilter(UserRepository userRepository) {
        return new JwtRequestFilter(jwtUserDetailsService(userRepository), jwtTokenService());
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
}

