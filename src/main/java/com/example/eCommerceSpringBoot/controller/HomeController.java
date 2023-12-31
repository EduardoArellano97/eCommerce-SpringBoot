package com.example.eCommerceSpringBoot.controller;

import com.example.eCommerceSpringBoot.model.Order;
import com.example.eCommerceSpringBoot.model.OrderDetail;
import com.example.eCommerceSpringBoot.model.Product;
import com.example.eCommerceSpringBoot.model.User;
import com.example.eCommerceSpringBoot.service.IOrderDetailService;
import com.example.eCommerceSpringBoot.service.IOrderService;
import com.example.eCommerceSpringBoot.service.IProductService;
import com.example.eCommerceSpringBoot.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    //Datos de la orden
    Order order = new Order();

    //Almacena los detalles de la orden
    List<OrderDetail> details = new ArrayList<OrderDetail>();

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private IProductService productService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderDetailService orderDetailService;

    @GetMapping("")
    public String home(Model model, HttpSession session){
        logger.info("sesión de usuario: {}", session.getAttribute("UserId"));
        model.addAttribute("products", productService.findAll());
        //Sesión
        model.addAttribute("current_session", session.getAttribute("UserId"));

        return "user_home";
    }

    @GetMapping("/ourproducts/{id}")
    public String ourProducts(@PathVariable Long id, Model model){
        logger.info("Se ha solicitado el producto siguiente desde el lado usuario: {}", id);
        Product product=new Product();
        Optional<Product> productOptional = productService.get(id);
        product = productOptional.get();
        model.addAttribute("product",product);
        return "user_ourproducts";
    }

    @PostMapping("/cart")
    public String addToCart(@RequestParam Long id, @RequestParam Double quantity, Model model){
        OrderDetail orderDetail= new OrderDetail();
        Product product= new Product();
        double totalSum = 0;

        Optional<Product> optionalProduct = productService.get(id);
        logger.info("Added product: {}", optionalProduct.get());
        logger.info("Quantity selected: {}", quantity);
        product= optionalProduct.get();

        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(product.getPrice());
        orderDetail.setName(product.getName());
        orderDetail.setTotal(product.getPrice()*quantity);
        orderDetail.setProduct(product);
        //A continuación se implementará que un producto no pueda ser añadido dos veces

        Long productId= product.getId();
        Boolean existingProduct = details.stream().anyMatch(p->p.getProduct().getId()==productId);
        if(!existingProduct){
            details.add(orderDetail);
        }
        totalSum=details.stream().mapToDouble(dt->dt.getTotal()).sum();
        order.setTotal(totalSum);

        model.addAttribute("cart",details);
        model.addAttribute("order",order);

        return "cart";
    }

   @GetMapping("/delete/cart/{id}")
    public String deleteFromCart(@PathVariable Long id, Model model){
        /*Para modificar los productos seleccionados, es decir, eliminar un producto que ya no necesitamos, es necesario
         actualizar la lista con los respectivos cambios, es por eso que tendremos que crear una nueva lista en la
         cual podamos hacer una sumatoria de los productos*/
        List<OrderDetail> newOrder= new ArrayList<OrderDetail>();

        //El siguiente paso es remover el producto seleccionado, por medio del id, de la lista general (details)
        for (OrderDetail i: details) {
            if(i.getProduct().getId()!=id){
                newOrder.add(i);
            }
        }/* La lógica se encuentra en que, para un objeto de OrderDetail, recorriendo la lista global de details
        encuentra que, un producto registrado por su id ya está en esta lista, entonces descartará añadirlo a una
        nueva lista que se desplegara en la Vista. De otra manera, al ser un producto que ya se encuentra, no se volverá
        a añadir. La función está en que, realmente no se ejecutará una acción de BORRAR, si no de ACTUALIZAR sin el
        dato repetido. La previa acción será ejecutada gracias al botón Quitar que se encuentra en la vista.*/

        //Se ejecuta la lista con los productos restantes
        details= newOrder;
        double totalSum=0;
        totalSum=details.stream().mapToDouble(dt->dt.getTotal()).sum();
        order.setTotal(totalSum);

        model.addAttribute("cart",details);
        model.addAttribute("order",order);

        return "cart";
    }
    @GetMapping("/getCart")
    public String getCart(Model model, HttpSession session){
        model.addAttribute("cart",details);
        model.addAttribute("order",order);
        model.addAttribute("current_session",session.getAttribute("UserId"));
        return "cart";
    }
    @GetMapping("/orderSummary")
    public String orderSummary(Model model, HttpSession session){
        User user= userService.findById(Long.parseLong(session.getAttribute("UserId").toString())).get();
        model.addAttribute("cart",details);
        model.addAttribute("order",order);
        model.addAttribute("user", user);

        return "order_summary";
    }
    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session){
            //1. Llenado de datos referentes a la orden y su creación
        Date creationDate= new Date();
        order.setCreationDate(creationDate);
        order.setNumber(orderService.OrderNumberGenerator());
            //2. Llenado de datos referente al usuario creador.(Versión sin implementación de Spring Security.)
        User user = userService.findById(Long.parseLong(session.getAttribute("UserId").toString())).get();
        order.setUser(user);
            //3. Guardar la orden con los datos añadidos
        orderService.save(order);
            //4. Guardar los detalles con forEach de la lista details
        for (OrderDetail dt: details) {
            dt.setOrder(order);
                orderDetailService.save(dt);
        }
            //5.Limpiar orden y lista
        order=new Order();
        details.clear();
        return "redirect:/";
    }
    @PostMapping("/search")
    public String search(@RequestParam String name, Model model){
        logger.info("Se ha buscado el siguiente producto: {}", name);
        List<Product> products = productService.findAll().stream().filter(p -> p.getName().contains(name)).collect(Collectors.toList());
        model.addAttribute("products",products);
        return "user_home";
    }

}
