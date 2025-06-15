package com.klikmakan.controller;

import com.klikmakan.model.Product;
import com.klikmakan.service.KeranjangService;
import com.klikmakan.service.CheckoutService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pembeli/checkout")
public class CheckoutController {

    @Autowired
    private KeranjangService keranjangService;

    @Autowired
    private CheckoutService checkoutService;

    @GetMapping
    public String checkoutPage(HttpSession session, Model model) {
        Map<Product, Integer> keranjang = keranjangService.getKeranjang(session);

        // Buat map subtotal
        Map<Product, BigDecimal> subtotalMap = new HashMap<>();
        BigDecimal totalHarga = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : keranjang.entrySet()) {
            BigDecimal subtotal = entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
            subtotalMap.put(entry.getKey(), subtotal);
            totalHarga = totalHarga.add(subtotal);
        }

        model.addAttribute("keranjang", keranjang);
        model.addAttribute("subtotalMap", subtotalMap);
        model.addAttribute("totalHarga", totalHarga);

        return "pembeli/checkout";
    }

    @PostMapping
    public String processCheckout(
            HttpSession session,
            @RequestParam String paymentMethod,
            @RequestParam String shippingAddress,
            Model model
    ) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            // Jika belum login, redirect ke halaman login dengan pesan error
            model.addAttribute("error", "User belum login.");
            return "redirect:/login";
        }

        try {
            // Jalankan proses checkout dengan userId yang diambil dari session
            checkoutService.checkout(session, userId, paymentMethod, shippingAddress);
            return "redirect:/pembeli/checkout/success";
        } catch (Exception e) {
            model.addAttribute("error", "Checkout gagal: " + e.getMessage());
            // Tampilkan kembali halaman checkout dengan pesan error
            return "pembeli/checkout";
        }
    }

    @GetMapping("/success")
    public String checkoutSuccess() {
        return "pembeli/checkout_success";
    }
}