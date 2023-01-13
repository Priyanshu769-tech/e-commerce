package com.example.inventory.Wallet;

import com.example.inventory.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WalletController {

    @Autowired
    WalletRepository walletrepo;


    @RequestMapping("/wallet")
    public String Wallet(Model model) {
        model.addAttribute("wallet", new Wallet());
        return "wallet";
    }

    @PostMapping("/wallet/save")
    public String saveWallet(Wallet wallet) {
        walletrepo.save(wallet);

        return "redirect:/home";
    }

}
