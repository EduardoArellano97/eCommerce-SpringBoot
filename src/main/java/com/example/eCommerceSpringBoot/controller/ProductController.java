package com.example.eCommerceSpringBoot.controller;

import com.example.eCommerceSpringBoot.model.Product;
import com.example.eCommerceSpringBoot.model.User;
import com.example.eCommerceSpringBoot.service.ProductService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


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
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        Product product= new Product();
        Optional<Product> optionalProduct = productService.get(id);
        product= optionalProduct.get();
        logger.info("Producto buscado: {}", product);
        model.addAttribute("product",product);
        return "edit";
    }
    @PostMapping("/update")
    public String update(Product product){
        productService.update(product);
        return "redirect:/products";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        productService.delete(id);
        return "redirect:/products";
    }

}
