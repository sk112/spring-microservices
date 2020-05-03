package com.auth.springauthservice.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth.springauthservice.dto.LoginUserDto;
import com.auth.springauthservice.dto.UserDto;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
   
    @GetMapping(value = "/auth")
    public String loginPage(Model model){
        model.addAttribute("model",new LoginUserDto());
        return "login";
    }

    @GetMapping("/loginError")
    public String loginError(Model model, HttpServletRequest request, HttpServletResponse response){
        
        LoginUserDto user = new LoginUserDto();
        user.setIdOrPasswdNotMatch(true);
        model.addAttribute("model", user);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return "login";
    }

    @GetMapping(value="/register")
    public String postMethodName(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "register";
    }


    @GetMapping("/home")
    @ResponseBody
    public String sayHello(){
        return "Hello World";
    }

}