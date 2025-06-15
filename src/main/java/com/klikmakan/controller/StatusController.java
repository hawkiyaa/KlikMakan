package com.klikmakan.controller;

import com.klikmakan.model.Transaction;
import com.klikmakan.repository.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StatusController {

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/pembeli/status")
    public String lihatStatusPengiriman(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        List<Transaction> transaksi = transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        model.addAttribute("transaksiList", transaksi);
        return "pembeli/status";
    }
}
