package com.example.eCommerceSpringBoot.controller;

import com.example.eCommerceSpringBoot.model.Product;
import com.example.eCommerceSpringBoot.model.User;
import com.example.eCommerceSpringBoot.service.IProductService;
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
    private IProductService IProductService;
    @Autowired
    private UploadFileService uploadFileService;
    public Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("products", IProductService.findAll());
        return "show";
    }

    @GetMapping("/create")
    public String create(){
        return "create";
    }
    @PostMapping("/save")
    public String save(Product product, @RequestParam("img") MultipartFile file) throws IOException {

        /* Los datos que se ingresen en el formulario que se encuentra en la vista /products/create, serán utilizados
         para llenar los atributos que el objeto product requiere, por ello al imprimir con Logger obtendremos los datos
         ingresados desde la vista.
         */

        logger.info("Objeto producto a continuación: {}",product);

        /* En el constructor también se solicita un usuario, el cual creamos aquí mismo con el objeto user, dotandole
         de atributos para después ser utilizados.*/

        User user = new User(1L,"","","","","","","");
        product.setUser(user);

        /* A continuación  se implementa el guardado de la imagen,requisitandola de la vista e ingresandola en file. */
        if(product.getId()==null){ //Se valida que el producto sea nuevo para añadir nueva imagen.
            String imageName= uploadFileService.saveImage(file); //file es el archivo recabado de la vista create.
            product.setImage(imageName);
        }
        IProductService.save(product);
        return "redirect:/products";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){

        /*Edit esta reservado para obtener en pantalla el objeto que estemos buscando según su ID. De la vista buscaremos
        si existe el producto al pinchar en edit(utilizando el optional), con la cuall obtendremos el Id. y reflejaremos
        entonces la información que este tenga para que, posteriormente, podamos guardar los cambios.*/

        Product product= new Product();
        Optional<Product> optionalProduct = IProductService.get(id);
        product= optionalProduct.get();
        logger.info("Producto buscado: {}", product);
        model.addAttribute("product",product);
        return "edit";
    }
    @PostMapping("/update")
    public String update(Product product, @RequestParam("img") MultipartFile file) throws IOException{
        /*Para poder hacer actualizaciones necesitamos obtener el producto según el ID...*/
        Product product1 = new Product();
        product1= IProductService.get(product.getId()).get();


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
        IProductService.update(product);
        return "redirect:/products";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        Product product=new Product();
        product= IProductService.get(id).get();
        /*Si no es igual a la imagen default, lo que significa que tiene información, procede.*/
        if (!product.getImage().equals("default.jpg")){
            uploadFileService.delete(product.getImage());
        }

        IProductService.delete(id);
        return "redirect:/products";
    }

}
