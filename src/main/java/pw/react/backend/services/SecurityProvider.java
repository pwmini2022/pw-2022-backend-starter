package pw.react.backend.services;

import org.springframework.http.HttpHeaders;

public interface SecurityProvider {
    boolean isAuthenticated(HttpHeaders headers);
    boolean isAuthorized(HttpHeaders headers);
}
