package com.example.eCommerceSpringBoot.controller;

import com.example.eCommerceSpringBoot.model.Product;
import com.example.eCommerceSpringBoot.model.User;
import com.example.eCommerceSpringBoot.service.ProductService;
import com.example.eCommerceSpringBoot.service.UploadFileService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UploadFileService uploadFileService;
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
    public String save(Product product, @RequestParam("img") MultipartFile file) throws IOException {
        logger.info("Objeto producto a continuación: {}",product);
        User user = new User(1L,"","","","","","","");
        product.setUser(user);
        //implementación de image
        if(product.getId()==null){ //Se valida que el producto sea nuevo para añadir nueva imagen.
            String imageName= uploadFileService.saveImage(file);
            product.setImage(imageName);
        }
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
    public String update(Product product, @RequestParam("img") MultipartFile file) throws IOException{
        Product product1 = new Product();
        product1=productService.get(product.getId()).get();
        if(file.isEmpty()){ //Se edita el producto pero no se cambia la imagen

            product.setImage(product.getImage());
        }else{ //Se edita el producto y se edita la imagen
            if (!product1.getImage().equals("default.jpg")){
                uploadFileService.delete(product.getImage());
            }
            String imageName= uploadFileService.saveImage(file);
            product.setImage(imageName);
        }
        product.setUser(product1.getUser());
        productService.update(product);
        return "redirect:/products";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        Product product=new Product();
        product=productService.get(id).get();
        if (!product.getImage().equals("default.jpg")){
            uploadFileService.delete(product.getImage());
        }

        productService.delete(id);
        return "redirect:/products";
    }

}
