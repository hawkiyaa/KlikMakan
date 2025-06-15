package com.klikmakan.service;

import com.klikmakan.model.*;
import com.klikmakan.repository.ProductRepository;
import com.klikmakan.repository.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class CheckoutService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KeranjangService keranjangService;

    @Autowired
    private TransactionRepository transactionRepository;

    public void checkout(HttpSession session, String userId, String paymentMethod, String shippingAddress) {
        Map<Product, Integer> keranjang = keranjangService.getKeranjang(session);

        if (keranjang.isEmpty()) {
            throw new IllegalStateException("Keranjang kosong, tidak bisa checkout.");
        }

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setUserId(userId);
        transaction.setPaymentMethod(Transaction.PaymentMethod.valueOf(paymentMethod));
        transaction.setShippingAddress(shippingAddress);

        BigDecimal totalHarga = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : keranjang.entrySet()) {
            BigDecimal subtotal = entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
            totalHarga = totalHarga.add(subtotal);
        }

        transaction.setTotalPrice(totalHarga);
        transaction.setStatus(Transaction.Status.MENUNGGU);

        // Proses setiap produk: kurangi stok, buat item transaksi
        for (Map.Entry<Product, Integer> entry : keranjang.entrySet()) {
            Product produk = entry.getKey();
            int jumlah = entry.getValue();

            // Validasi stok cukup
            if (produk.getStock() < jumlah) {
                throw new IllegalStateException("Stok tidak cukup untuk produk: " + produk.getName());
            }

            // Kurangi stok dan simpan
            produk.setStock(produk.getStock() - jumlah);
            productRepository.save(produk);

            // Buat item transaksi
            TransactionItem item = new TransactionItem();
            item.setId(UUID.randomUUID().toString());
            item.setTransaction(transaction);
            item.setProduct(produk);
            item.setQuantity(jumlah);
            item.setPrice(produk.getPrice());

            transaction.getItems().add(item);
        }

        // Simpan transaksi beserta item (CascadeType.ALL)
        transactionRepository.save(transaction);

        // Kosongkan keranjang
        session.removeAttribute("keranjang");
    }
}