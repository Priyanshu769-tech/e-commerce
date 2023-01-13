package com.example.inventory.global;

import com.example.inventory.Product.Product;
import com.example.inventory.Wallet.Wallet;

import java.util.ArrayList;
import java.util.List;

public class GlobalData {
    public static List<Product> cart;
    static{
        cart=new ArrayList<Product>();
    }

    public static List<Wallet>wallet;
    static{
        wallet=new ArrayList<Wallet>();

    }
}
