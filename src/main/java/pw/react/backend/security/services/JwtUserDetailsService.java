package pw.react.backend.security.services;

import org.springframework.security.core.userdetails.*;

import java.util.ArrayList;

public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("pawg".equals(username)) {
            return new User("pawg", "$2a$10$xk3Wruq5OykyFmEIwZr.w.N69ZdGLSspK3cyVS9lVTCkQ.z9i5RAK",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
