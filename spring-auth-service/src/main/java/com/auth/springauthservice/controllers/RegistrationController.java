package com.auth.springauthservice.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.auth.springauthservice.constants.Constants;
import com.auth.springauthservice.dto.LoginUserDto;
import com.auth.springauthservice.dto.UserDto;
import com.auth.springauthservice.services.UserServiceImpl;
import com.auth.springauthservice.utils.JsonResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class RegistrationController {
    
    @Autowired
    private UserServiceImpl userServiceImpl;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserDto userDto,@RequestBody UserDto requestUserDto, HttpServletResponse response){
        
        UserDto applicableUser = new UserDto();

        if(userDto.getEmail() == null){
            applicableUser = requestUserDto;
        }else{
            applicableUser = userDto;
        }


        String email = applicableUser.getEmail();
        String password = applicableUser.getPassword();
        String confirmPasswd = applicableUser.getConfirmPassword();
        String firstName = applicableUser.getFirstName();
        String lastName = applicableUser.getLastName();
        
        if(!password.equals(confirmPasswd)){
            Map<String, String> map = new HashMap<>();
            map.put("message", "Passsword Does not match");
            map.put("errorcode", String.valueOf(Constants.PASSWORD_DOES_NOT_MATCH));
            map.put("status", HttpStatus.BAD_REQUEST.toString());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return JsonResponseBuilder.getJsonFormatString(map);
        }

        com.auth.springauthservice.models.User user = new com.auth.springauthservice.models.User(firstName, lastName,
                email, password);

        userServiceImpl.save(user);

        Map<String, String> map = new HashMap<>();
        map.put("message", email+" registered succesfully.");
        map.put("status", HttpStatus.OK.toString());
        response.setStatus(HttpStatus.OK.value());
        return JsonResponseBuilder.getJsonFormatString(map);
    }    
}