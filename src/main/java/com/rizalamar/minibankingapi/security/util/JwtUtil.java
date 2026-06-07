package com.rizalamar.minibankingapi.security.util;

import com.rizalamar.minibankingapi.domain.User;
import com.rizalamar.minibankingapi.security.SecurityUser;
import com.rizalamar.minibankingapi.security.annotation.CurrentUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expiration;

    private SecretKey getSingingKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSingingKey())
                .compact();
    }

    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSingingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken (String token, UserDetails userDetails){
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Component
    public static class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(CurrentUser.class) && parameter.getParameterType().equals(User.class);
        }

        @Override
        public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("authentication: " + authentication);

            Object principal = authentication.getPrincipal();
            System.out.println("principal: " + principal);

            if(principal instanceof SecurityUser(User user)) { // SecurityUser securityUser
                return user; // get user from SecurityUser
            }

            return null;
        }
    }
}
