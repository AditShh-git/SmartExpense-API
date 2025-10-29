package com.Expense.Tracker.API.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Get the JWT from the "Authorization" header
            String jwt = getJwtFromRequest(request);

            // 2. Validate the token
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateJwtToken(jwt)) {
                // 3. Get the username from the token
                String username = jwtTokenProvider.getUsernameFromJwtToken(jwt);

                // 4. Load the user's details from the database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Create an "authentication" object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Set this authentication in Spring's SecurityContext
                // This is the line that "logs in" the user for this request.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // Handle exceptions (e.g., failed token validation)
            logger.error("Could not set user authentication in security context", ex);
        }

        // 7. Pass the request to the next filter in the chain
        filterChain.doFilter(request, response);
    }

    /**
     * Helper method to get the token from the request header.
     * It expects "Bearer <token>"
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}