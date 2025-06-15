package com.klikmakan.repository;

import com.klikmakan.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // JpaRepository sudah menyediakan method findAll()
}