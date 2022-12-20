package pw.react.backend.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

public class JwtTokenService implements Serializable {

    private final Logger log = LoggerFactory.getLogger(JwtTokenService.class);

    private static final Set<String> INVALID_TOKENS = new LinkedHashSet<>();

    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    private final String secret;
    private final long expirationMs;

    public JwtTokenService(String secret, long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails, HttpServletRequest request) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("ip", getClientIp(request));
        claims.put("user-agent", getUserAgent(request));
        log.info("Adding ip:{} and user-agent:{} to the claims.", getClientIp(request), getUserAgent(request));
        return doGenerateToken(claims, userDetails.getUsername());
    }

    String getClientIpFromToken(String token) {
        return getClaimFromToken(token, claims -> String.valueOf(claims.get("ip")));
    }

    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

    private String getUserAgent(HttpServletRequest request) {
        String ua = "";
        if (request != null) {
            ua = request.getHeader("User-Agent");
        }
        return ua;
    }

    String getUserAgentFromToken(String token) {
        return getClaimFromToken(token, claims -> String.valueOf(claims.get("user-agent")));
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails, HttpServletRequest request) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token) &&
                isClientIpCorrect(token, request) &&
                isValidUserAgent(token, request) &&
                !INVALID_TOKENS.contains(token);
    }

    private boolean isValidUserAgent(String token, HttpServletRequest request) {
        return getUserAgent(request).equals(getUserAgentFromToken(token));
    }

    private boolean isClientIpCorrect(String token, HttpServletRequest request) {
        return getClientIp(request).equals(getClientIpFromToken(token));
    }

    public boolean invalidateToken(HttpServletRequest request) {
        String authorizationHeader = "Authorization";
        String bearer = "Bearer ";

        String requestTokenHeader = request.getHeader(authorizationHeader);
        if (requestTokenHeader != null && requestTokenHeader.startsWith(bearer)) {
            INVALID_TOKENS.add(requestTokenHeader.replace(bearer, ""));
            INVALID_TOKENS.removeIf(this::isTokenExpired);
            return true;
        }
        return false;
    }
}