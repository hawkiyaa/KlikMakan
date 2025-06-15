package com.klikmakan.controller;

import com.klikmakan.model.Transaction;
import com.klikmakan.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pemilik/daftar_pesanan")
public class DaftarPesananController {

    @Autowired
    private TransactionRepository transactionRepository;

    // Tampilkan halaman daftar pesanan
    @GetMapping
    public String tampilkanDaftarPesanan(Model model) {
        List<Transaction> transactions = transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        model.addAttribute("transactions", transactions);
        return "pemilik/daftar_pesanan";
    }

    // Tangani perubahan status pesanan
    @PostMapping("/ubah_status")
    public String ubahStatusPesanan(@RequestParam("id") String id, @RequestParam("status") String status) {
        Optional<Transaction> optional = transactionRepository.findById(id);
        if (optional.isPresent()) {
            Transaction trx = optional.get();
            trx.setStatus(Transaction.Status.valueOf(status));
            transactionRepository.save(trx);
        }
        return "redirect:/pemilik/daftar_pesanan";
    }
}