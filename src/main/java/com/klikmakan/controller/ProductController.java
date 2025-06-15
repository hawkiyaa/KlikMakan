package com.klikmakan.controller;

import com.klikmakan.model.Product;
import com.klikmakan.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
@RequestMapping("/pemilik/kelola_produk")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProduk(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("product", new Product());
        return "pemilik/kelola_produk";
    }

    @PostMapping("/simpan")
    public String simpanProduk(@ModelAttribute Product product,
                               @RequestParam("image") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            // Baca byte gambar
            byte[] imageBytes = imageFile.getBytes();

            // Encode ke Base64
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Set image URL dengan format data URI
            String mimeType = imageFile.getContentType(); // misal image/jpeg
            product.setImageUrl("data:" + mimeType + ";base64," + base64Image);
        }

        productService.save(product);
        return "redirect:/pemilik/kelola_produk";
    }

    @PostMapping("/hapus/{id}")
    public String hapusProduk(@PathVariable("id") String id) {
        productService.deleteById(id);
        return "redirect:/pemilik/kelola_produk";
    }
}