package com.example.eCommerceSpringBoot.controller;

import com.example.eCommerceSpringBoot.model.Order;
import com.example.eCommerceSpringBoot.model.Product;
import com.example.eCommerceSpringBoot.service.IOrderService;
import com.example.eCommerceSpringBoot.service.IProductService;
import com.example.eCommerceSpringBoot.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IProductService IProductService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;

    private final Logger logger= LoggerFactory.getLogger(AdminController.class);
    @GetMapping("")
    public String home(Model model){
        List<Product> products = IProductService.findAll();
        model.addAttribute("products",products);

        return "home";
    }
    @GetMapping("/users")
    public String users(Model model){
        model.addAttribute("users",userService.findAll());

        return "users_admin";
    }
    @GetMapping("/orders")
    public String orders(Model model){
        model.addAttribute("orders",orderService.findAll());
        return "orders_admin";
    }
    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable Long id){
        logger.info("Detalle de orden requerida: {}", id);
        Order order = orderService.findById(id).get();
        model.addAttribute("details",order.getOrderDetail());

        return "order_detail_admin";
    }
}
