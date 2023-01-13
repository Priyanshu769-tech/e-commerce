package com.example.inventory.Home;

import com.example.inventory.Product.Product;
import com.example.inventory.Product.ProductRepository;
import com.example.inventory.Product.ProductService;
import com.example.inventory.category.CategoryRepository;
import com.example.inventory.global.GlobalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryRepository caterepo;

    @Autowired
    private ProductRepository prorepo;

    @Autowired
    private ProductService proservice;



    @GetMapping("/home")
    public String shop(Model model){
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("categories",caterepo.findAll());
        model.addAttribute("products",prorepo.findAll());
        return "home";
    }

    @GetMapping("/home/{id}")
    public String shopByCategory(Model model, @PathVariable("id")Integer id){
        model.addAttribute("categories",caterepo.findAll());
        model.addAttribute("cartCount",GlobalData.cart.size());

        model.addAttribute("products",proservice.getAllProductsByCategoryId(id));

        return "shop2";
    }
    @GetMapping("/home/viewproduct/{id}")
    public String viewProduct(Model model, @PathVariable int id){
        model.addAttribute("products",proservice.getProductById(id).get());
        model.addAttribute("cartCount",GlobalData.cart.size());
        return"viewproduct";
    }

    @GetMapping("about")
    public String About(){
        return"about";
    }

    @GetMapping("contactus")
    public String  contact() {
        return "contactus";
    }


}
