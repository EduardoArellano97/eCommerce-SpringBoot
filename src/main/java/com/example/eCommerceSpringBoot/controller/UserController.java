package com.example.eCommerceSpringBoot.controller;

import com.example.eCommerceSpringBoot.model.User;
import com.example.eCommerceSpringBoot.service.IUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/user")
public class UserController {
    Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @GetMapping("/register")
    public String register(){
        return "user_register";
    }
    @PostMapping("/save")
    public String save(User user){
        logger.info("Nuevo usuario registrado desde /save: {}" ,user);
        //Guardar usuario
        user.setType("USER");
        userService.save(user);
        return "redirect:/";
    }
}
