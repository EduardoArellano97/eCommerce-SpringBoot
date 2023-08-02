package com.example.eCommerceSpringBoot.controller;

import com.example.eCommerceSpringBoot.model.User;
import com.example.eCommerceSpringBoot.service.IUserService;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @GetMapping("/register")
    public String register() {
        return "user_register";
    }

    @PostMapping("/save")
    public String save(User user) {
        logger.info("Nuevo usuario registrado desde /save: {}", user);
        //Guardar usuario
        user.setType("USER");
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/access")
    public String access(User user, HttpSession session) {
        Optional<User> userMail = userService.findByEmail(user.getEmail()); /*Realizaremos una búsqueda de usuario
        por medio del email, buscamos encontrar el usuario al cual le corresponde el email ingresado*/
        logger.info("Usuario con ese correo: {}", userMail.get());

        if (userMail.isPresent()) { //si el Email está presente obtendremos el Id
            session.setAttribute("UserId", userMail.get().getId());
            if (userMail.get().getType().equals("ADMIN")) {
                return "redirect:/admin";
            } else {
                return "redirect:/";
            }
        }else {
            logger.info("usuario no existe");
        }

        return "redirect:/";
    }
}