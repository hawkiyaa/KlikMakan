package com.klikmakan.controller;

import com.klikmakan.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PembeliController {

    @Autowired
    private ProductService productService;

    @GetMapping("/pembeli/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("products", productService.findAll());
        return "pembeli/dashboard";  // nama file thymeleaf: dashboard.html
    }
}