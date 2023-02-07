package com.irbis.irbisanalyticstest.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.irbis.irbisanalyticstest.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final static String LOGGER_WARNING_MESSAGE = "JWT Token does not begin with Bearer String";
    private final static String UNABLE_TO_GET_JWT_TOKEN_MESSAGE = "Unable to get JWT Token";
    private final static String JWT_TOKEN_HAS_EXPIRED_MESSAGE = "JWT Token has expired";

    private final static String REQUEST_TOKEN_HEADER =
            "Bearer " +
            "eyJhbGciOiJIUzUxMiJ9" +
            ".eyJzdWIiOiJ5YXIiLCJleHAiOjE2NzU3ODYyNDIsImlhdCI6MTY3NTc2ODI0Mn0" +
            ".L3F7bxNSy2tWXuvRv9CTeXtl-0kEFcCYm5orxvXQoSHP01y-kNb4N5Uj81qqxoG6hhpFqrTjwVvqO5uf68B6lg";

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtRequestFilter(
        final JwtUserDetailsService jwtUserDetailsService,
        final JwtTokenUtil jwtTokenUtil
    ) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain chain
    ) throws ServletException, IOException {

//        We don't need it in our case, but we still can use it if we need it
//        final String requestTokenHeader = request.getHeader("Authorization");

        final String requestTokenHeader = REQUEST_TOKEN_HEADER;

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

            jwtToken = requestTokenHeader.substring(7);

            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println(UNABLE_TO_GET_JWT_TOKEN_MESSAGE);
            } catch (ExpiredJwtException e) {
                System.out.println(JWT_TOKEN_HAS_EXPIRED_MESSAGE);
            }

        } else {
            logger.warn(LOGGER_WARNING_MESSAGE);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            final UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }

}
