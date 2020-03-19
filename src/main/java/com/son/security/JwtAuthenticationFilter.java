package com.son.security;

import com.son.entity.User;
import com.son.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("Jwt Filter");

        try {
            String jwt = jwtTokenService.getJwtTokenFromRequest(request);

            if (jwt == null || !StringUtils.hasText(jwt) && !jwtTokenService.validateToken(jwt)) {
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtTokenService.getUsernameFromJWT(jwt);
            Credentials credentials = (Credentials) userDetailsService.loadUserByUsername(username);

            if (credentials == null || credentials.getStatus() != User.Status.ACTIVE) {
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken
                    auth = new UsernamePasswordAuthenticationToken(credentials, null,
                    credentials.getAuthorities());

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }
}
