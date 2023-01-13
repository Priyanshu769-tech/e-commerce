package com.example.inventory.Product;

import com.example.inventory.User.User;
import com.example.inventory.User.UserRepository;
import com.example.inventory.category.Category;
import com.example.inventory.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    public static String uploadDir=System.getProperty("user.dir")+"/src/main/resources/static/productImages";
    @Autowired
    private ProductRepository productrepo;

    @Autowired
    private CategoryRepository categoryrepo;

    @Autowired
    private ProductService proservice;

    @Autowired
    private UserRepository userepo;

    @GetMapping("/products/new")
    public String showNewProductForm(Model model){
        List<Category> listCategories=categoryrepo.findAll();

        model.addAttribute("product",new Product());
        model.addAttribute("listCategories",listCategories);
        return "product_form";
    }
    @PostMapping("/products/save")
    public String saveProduct(Product product,@RequestParam("productImage")MultipartFile file,@RequestParam("imgName")String imgName)throws IOException{
        String imageUUID;
        if(!file.isEmpty()){
            imageUUID= file.getOriginalFilename();
            Path fileNameAndPath= Paths.get(uploadDir,imageUUID);
            Files.write(fileNameAndPath,file.getBytes());
        }else{
            imageUUID=imgName;
        }
        product.setImageName(imageUUID);
        proservice.addProduct(product);
        productrepo.save(product);
        return "redirect:/admin";
    }
    @GetMapping("/products")
    public String listProducts(Model model){
        List<Product>listProducts= productrepo.findAll();
        model.addAttribute("listProducts",listProducts);
        return "products";
    }

    @GetMapping("products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Integer id, Model model){
        Product product= productrepo.findById(id).get();
    model.addAttribute("product",product);
        List<Category> listCategories=categoryrepo.findAll();


        model.addAttribute("listCategories",listCategories);
    return "edit_product";
    }

    @GetMapping("products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, Model model){
        productrepo.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/user")
    public String User(Model model){
        List<User>listUsers= userepo.findAll();
        model.addAttribute("listUsers",listUsers);
        return "user";
    }
    @GetMapping("user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model){
        userepo.deleteById(id);
        return "redirect:/user";
    }




}
