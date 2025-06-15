package com.klikmakan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import com.klikmakan.model.User;
import com.klikmakan.repository.UserRepository;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,  // <-- tambah HttpSession disini
            Model model) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "Username tidak ditemukan");
            return "login";
        }

        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Password salah");
            return "login";
        }

        // Simpan user id dan username ke session agar bisa dipakai di controller lain
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());

        // Cek role user, arahkan ke dashboard sesuai role
        String roleId = user.getRole().getId();
        if ("ADMIN".equals(roleId)) {
            return "redirect:/pemilik/kelola_produk";
        } else if ("USER".equals(roleId)) {
            return "redirect:/pembeli/dashboard";
        } else {
            model.addAttribute("error", "Role tidak valid");
            return "login";
        }
    }

    @GetMapping("/pemilik/dashboard")
    public String pemilikDashboard() {
        return "pemilik/dashboard";
    }

    // Uncomment jika ingin gunakan pembeli dashboard
    // @GetMapping("/pembeli/dashboard")
    // public String pembeliDashboard() {
    //     return "pembeli/dashboard";
    // }
}