package com.auth.springauthservice.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth.springauthservice.JwtTokenUtil;
import com.auth.springauthservice.services.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilterRequest extends OncePerRequestFilter{
    
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
                final String requestAccessToken = request.getHeader("Authorization");

                String username = null;
                String token = null;


                if(requestAccessToken != null && requestAccessToken.startsWith("Bearer ")){
                    token = requestAccessToken.substring(7);
                    username = jwtTokenUtil.getUserNameFromToken(token);
                }

                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                    if(jwtTokenUtil.validateToken(token, userDetails)){
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
                        
                        // usernamePasswordAuthenticationToken.setDetails(details);

                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                    
                }

                filterChain.doFilter(request, response);
        }
    
}