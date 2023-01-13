package com.example.inventory.category;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller

public class CategoryController {
    @Autowired
    private CategoryRepository repo;

    @GetMapping("/admin")
    public String adminHome(){
        return "index";
    }
    @GetMapping("manager")
    public String manager(){
        return "manager";
    }

    @RequestMapping("/categories")
    public String listCategories(Model model) {
        List<Category> listCategories = repo.findAll();
        model.addAttribute("listCategories", listCategories);

        return "categories";

    }

    @RequestMapping("/categories/new")
    public String showCategoryNewForm(Model model) {
        model.addAttribute("category", new Category());
        return "Category_form";
    }
    @PostMapping("/categories/save")
    public String saveCategories(Category category){
        repo.save(category);

        return "redirect:/categories";
    }
}
