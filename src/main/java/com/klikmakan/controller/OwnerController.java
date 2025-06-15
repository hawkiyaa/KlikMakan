package com.klikmakan.controller;

import com.klikmakan.service.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OwnerController {

    private final SalesService salesService;

    public OwnerController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/pemilik/riwayat_penjualan")
    public String showSalesHistory(Model model) {
        model.addAttribute("weeklySales", salesService.getWeeklySales());
        return "pemilik/riwayat_penjualan";
    }
}