package com.klikmakan.controller;

import com.klikmakan.model.User;
import com.klikmakan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProfilController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/pembeli/profil")
    public String showProfil(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        User user = userRepo.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "User tidak ditemukan");
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "pembeli/profil";
    }

    @PostMapping("/profil")
    public String updateProfil(
            @RequestParam String password,
            @RequestParam String fullName,
            @RequestParam String address,
            @RequestParam String phone,
            HttpSession session,
            Model model) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        User user = userRepo.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "User tidak ditemukan");
            return "pembeli/profil";
        }

        user.setPassword(password);
        user.setFullName(fullName);
        user.setAddress(address);
        user.setPhone(phone);

        userRepo.save(user);

        model.addAttribute("user", user);
        model.addAttribute("success", "Profil berhasil diperbarui!");
        return "pembeli/profil";
    }

    // === Tambahan untuk PEMILIK ===

    @GetMapping("/pemilik/profil")
    public String showProfilPemilik(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        User user = userRepo.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "User tidak ditemukan");
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "pemilik/profil";
    }

    @PostMapping("/pemilik/profil")
    public String updateProfilPemilik(
            @RequestParam String password,
            @RequestParam String fullName,
            @RequestParam String address,
            @RequestParam String phone,
            HttpSession session,
            Model model) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        User user = userRepo.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "User tidak ditemukan");
            return "pemilik/profil";
        }

        user.setPassword(password);
        user.setFullName(fullName);
        user.setAddress(address);
        user.setPhone(phone);

        userRepo.save(user);

        model.addAttribute("user", user);
        model.addAttribute("success", "Profil berhasil diperbarui!");
        return "pemilik/profil";
    }
}