package com.example.eCommerceSpringBoot.controller;

import com.example.eCommerceSpringBoot.model.Product;
import com.example.eCommerceSpringBoot.model.User;
import com.example.eCommerceSpringBoot.service.ProductService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    public Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("products",productService.findAll());
        return "show";
    }

    @GetMapping("/create")
    public String create(){
        return "create";
    }
    @PostMapping("/save")
    public String save(Product product){
        logger.info("Objeto producto a continuación: {}",product);
        User user = new User(1L,"","","","","","","");
        product.setUser(user);
        productService.save(product);
        return "redirect:/products";


    }
}
