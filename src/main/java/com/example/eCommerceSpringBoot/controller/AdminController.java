package com.example.eCommerceSpringBoot.controller;

import com.example.eCommerceSpringBoot.model.Product;
import com.example.eCommerceSpringBoot.service.IProductService;
import com.example.eCommerceSpringBoot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IProductService IProductService;
    @Autowired
    private IUserService userService;
    @GetMapping("")
    public String home(Model model){
        List<Product> products = IProductService.findAll();
        model.addAttribute("products",products);

        return "home";
    }
    @GetMapping("/users")
    public String users(Model model){
        model.addAttribute("users",userService.findAll());

        return "users";
    }
}
