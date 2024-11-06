package com.asankovic.race.command.filters;

import com.asankovic.race.command.controllers.RunnerMutatingController;
import com.asankovic.race.command.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.*;

@Component
@Order(1)
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;

    public JwtFilter(final JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        if (!startsWith(request.getRequestURI(), RunnerMutatingController.ENDPOINT) ||
                !equalsIgnoreCase(request.getMethod(), "GET")) {
            LOG.trace("Skipping JWT token validation because of a GET request or a non-protected request URI");
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");

        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            LOG.trace("Received a request without a JWT token!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header is required to access this resource!");
            return;
        }

        final String jwtToken = authorizationHeader.substring(7);

        if (isBlank(jwtToken) && !jwtUtil.isTokenValid(jwtToken)) {
            LOG.trace("Received a request with invalid or expired JWT token: {}", jwtToken);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Provided JWT token is invalid or expired!");
            return;
        }

        LOG.trace("Received a request with valid JWT token: {}", jwtToken);
        filterChain.doFilter(request, response);
    }
}