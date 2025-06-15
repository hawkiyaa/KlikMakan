package com.klikmakan.controller;

import com.klikmakan.service.KeranjangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/keranjang")
public class KeranjangController {

    @Autowired
    private KeranjangService keranjangService;

    @PostMapping("/tambah")
    public String tambah(@RequestParam String productId, HttpSession session) {
        keranjangService.tambah(session, productId);
        return "redirect:/keranjang";
    }

    @PostMapping("/kurangi")
    public String kurangi(@RequestParam String productId, HttpSession session) {
        keranjangService.kurangi(session, productId);
        return "redirect:/keranjang";
    }

    @PostMapping("/hapus")
    public String hapus(@RequestParam String productId, HttpSession session) {
        keranjangService.hapus(session, productId);
        return "redirect:/keranjang";
    }

    @GetMapping
    public String lihat(Model model, HttpSession session) {
        model.addAttribute("items", keranjangService.getKeranjang(session));
        model.addAttribute("totalItem", keranjangService.getTotalItem(session));
        model.addAttribute("totalHarga", keranjangService.getTotalHarga(session));
        return "pembeli/keranjang";
    }
}