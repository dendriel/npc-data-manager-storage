package com.rozsa.security.filter;

import com.rozsa.service.AuthService;
import com.rozsa.service.AuthResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Component
public class AuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        final String authHeader = req.getHeader("Authorization");
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        String token = "Bearer " + jwt;
        AuthResponse authResponse;
        try {
            authResponse = authService.validate(token);
        } catch (feign.RetryableException e) {
            res.setStatus(HttpStatus.SC_GATEWAY_TIMEOUT);
            return;
        }
        catch (Exception e) {
            res.setStatus(HttpStatus.SC_BAD_GATEWAY);
            return;
        }

        log.info("Validation status for token {} is {}", token, authResponse);

        UserDetails userDetails = getUserDetails(authResponse);
        if (userDetails == null) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        chain.doFilter(req, res);
    }

    public UserDetails getUserDetails(AuthResponse response) {
        if (!response.isAuthenticated() || response.getUsername() == null) {
            return null;
        }

        List<GrantedAuthority> authorities = response.getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String username = response.getUsername();

        return new org.springframework.security.core.userdetails.User(username, "", authorities);
    }
}
