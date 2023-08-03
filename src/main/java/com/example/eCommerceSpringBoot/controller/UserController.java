package com.example.eCommerceSpringBoot.controller;

import com.example.eCommerceSpringBoot.model.Order;
import com.example.eCommerceSpringBoot.model.User;
import com.example.eCommerceSpringBoot.service.IOrderService;
import com.example.eCommerceSpringBoot.service.IUserService;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;

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
    @GetMapping("/shopping")
    public String shopping(Model model, HttpSession session){
        model.addAttribute("current_session",session.getAttribute("UserId"));
        User user= userService.findById(Long.parseLong(session.getAttribute("UserId").toString())).get();
        List<Order> orders= orderService.findByUser(user);
        model.addAttribute("orders",orders);

        return "shopping";
    }
    @GetMapping("/detail/{id}")
    public String shoppingDetail(@PathVariable Long id, Model model, HttpSession session){

        logger.info("Id de la sesión: {}", id);
        Optional<Order> order= orderService.findById(id);
        //Se obtiene la Lista del controlador Order que accede a los detalles.
        model.addAttribute("details", order.get().getOrderDetail());



        //Sesion
        model.addAttribute("current_session",session.getAttribute("UserId"));

        return "shopping_detail";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("UserId");
        return "redirect:/";
    }
}