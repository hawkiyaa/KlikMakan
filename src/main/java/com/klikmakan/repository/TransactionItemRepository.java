// TransactionItemRepository.java
package com.klikmakan.repository;

import com.klikmakan.model.TransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.data.repository.query.Param;


public interface TransactionItemRepository extends JpaRepository<TransactionItem, String> {

    @Query("""
        SELECT ti.product.name AS productName,
               SUM(ti.quantity) AS totalQuantity,
               SUM(ti.price * ti.quantity) AS totalRevenue,
               FUNCTION('YEARWEEK', t.createdAt) AS yearWeek
        FROM TransactionItem ti
        JOIN ti.transaction t
        WHERE t.status = 'SELESAI'
          AND t.createdAt BETWEEN :startDate AND :endDate
        GROUP BY ti.product.name, FUNCTION('YEARWEEK', t.createdAt)
        ORDER BY yearWeek DESC
    """)
    List<Object[]> getWeeklySalesSummary(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
}
