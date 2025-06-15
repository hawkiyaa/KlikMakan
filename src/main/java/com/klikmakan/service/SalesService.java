package com.klikmakan.service;

import com.klikmakan.repository.TransactionItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SalesService {

    private final TransactionItemRepository transactionItemRepository;

    public SalesService(TransactionItemRepository transactionItemRepository) {
        this.transactionItemRepository = transactionItemRepository;
    }

    public List<Map<String, Object>> getWeeklySales() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusWeeks(4); // ambil 4 minggu terakhir

        List<Object[]> results = transactionItemRepository.getWeeklySalesSummary(startDate, endDate);

        List<Map<String, Object>> weeklySales = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> data = new HashMap<>();
            data.put("productName", row[0]);
            data.put("totalQuantity", row[1]);
            data.put("totalRevenue", row[2]);
            data.put("yearWeek", row[3]);
            weeklySales.add(data);
        }

        return weeklySales;
    }
}