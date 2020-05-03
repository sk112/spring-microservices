package com.auth.springauthservice.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.auth.springauthservice.JwtTokenUtil;
import com.auth.springauthservice.dto.LoginUserDto;
import com.auth.springauthservice.services.UserDetailsServiceImpl;
import com.auth.springauthservice.utils.JsonResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/authenticate")
    public String authenticateUser(@ModelAttribute("model") @NotNull LoginUserDto user,@RequestBody LoginUserDto loginUser, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        
        try{
            UserDetails userDetails;

            if(user.getId() == null){
                userDetails = userDetailsServiceImpl.loadUserByUsername(loginUser.getId());
            }else{
                userDetails = userDetailsServiceImpl.loadUserByUsername(user.getId());
            }

            try{

                if(!passwordEncoder.matches(loginUser.getPasswd(), userDetails.getPassword())){
                    throw new BadCredentialsException("Password incorrect");
                }

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
                Authentication auth = this.authenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(auth);

                Map<String, String> map = new HashMap<>();
                map.put("status", HttpStatus.OK.toString());
                map.put("user", user.getId());
                map.put("token", jwtTokenUtil.generateToken(userDetails));
                return JsonResponseBuilder.getJsonFormatString(map);
            }catch(BadCredentialsException ex){

                // response.sendRedirect("/loginError");
                Map<String, String> map = new HashMap<>();
                map.put("user", user.getId());
                map.put("message", "BadCredentials=Access Denied ->" +ex.toString() + ex.getMessage());
                map.put("status", HttpStatus.UNAUTHORIZED.toString());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return JsonResponseBuilder.getJsonFormatString(map);
            }

        }catch(UsernameNotFoundException ex){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            // response.sendRedirect("/loginError");
            
            Map<String, String> map = new HashMap<>();
            map.put("user", user.getId());
            map.put("message", ex.toString() + ex.getMessage());
            map.put("status", HttpStatus.BAD_REQUEST.toString());
            return JsonResponseBuilder.getJsonFormatString(map);
        }
    }

}