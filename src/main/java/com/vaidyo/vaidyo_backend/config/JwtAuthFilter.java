package com.vaidyo.vaidyo_backend.config;

import com.vaidyo.vaidyo_backend.service.UserDetailsServiceImpl;
import com.vaidyo.vaidyo_backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil,
                         UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader =
                request.getHeader("Authorization");

        System.out.println("🔐 Auth header: " + authHeader);

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        System.out.println("🎫 Token starts: "
                + jwt.substring(0, 20) + "...");

        try {
            final String mobileNumber =
                    jwtUtil.extractUsername(jwt);
            System.out.println("📱 Mobile: " + mobileNumber);

            if (mobileNumber != null &&
                    SecurityContextHolder.getContext()
                            .getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(mobileNumber);

                System.out.println("👤 User: "
                        + userDetails.getUsername());
                System.out.println("🎭 Authorities: "
                        + userDetails.getAuthorities());

                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    System.out.println("✅ Token valid!");

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);
                } else {
                    System.out.println("❌ Token invalid!");
                }
            }
        } catch (Exception e) {
            System.out.println("💥 Error: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}