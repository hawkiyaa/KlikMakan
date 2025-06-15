package com.klikmakan.service;

import com.klikmakan.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class KeranjangService {

    private static final String SESSION_KEY = "keranjang";

    @Autowired
    private ProductService productService;

    @SuppressWarnings("unchecked")
    private Map<String, Integer> getKeranjangMap(HttpSession session) {
        return (Map<String, Integer>) session.getAttribute(SESSION_KEY);
    }

    private void saveKeranjangMap(HttpSession session, Map<String, Integer> map) {
        session.setAttribute(SESSION_KEY, map);
    }

    public void tambah(HttpSession session, String productId) {
        Map<String, Integer> map = getKeranjangMap(session);
        if (map == null) map = new LinkedHashMap<>();
        map.put(productId, map.getOrDefault(productId, 0) + 1);
        saveKeranjangMap(session, map);
    }

    public void kurangi(HttpSession session, String productId) {
        Map<String, Integer> map = getKeranjangMap(session);
        if (map != null && map.containsKey(productId)) {
            int qty = map.get(productId);
            if (qty > 1) map.put(productId, qty - 1);
            else map.remove(productId);
        }
        saveKeranjangMap(session, map);
    }

    public void hapus(HttpSession session, String productId) {
        Map<String, Integer> map = getKeranjangMap(session);
        if (map != null) {
            map.remove(productId);
            saveKeranjangMap(session, map);
        }
    }

    public Map<Product, Integer> getKeranjang(HttpSession session) {
        Map<String, Integer> map = getKeranjangMap(session);
        if (map == null) return Collections.emptyMap();

        Map<Product, Integer> result = new LinkedHashMap<>();
        map.forEach((pid, qty) -> {
            productService.findById(pid).ifPresent(p -> result.put(p, qty));
        });
        return result;
    }

    public int getTotalItem(HttpSession session) {
        return getKeranjangMap(session) == null ? 0 :
            getKeranjangMap(session).values().stream().mapToInt(i -> i).sum();
    }

    public BigDecimal getTotalHarga(HttpSession session) {
        return getKeranjang(session).entrySet().stream()
            .map(e -> e.getKey().getPrice().multiply(BigDecimal.valueOf(e.getValue())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}