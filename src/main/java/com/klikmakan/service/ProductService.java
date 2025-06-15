package com.klikmakan.service;

import com.klikmakan.model.Product;
import com.klikmakan.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    public List<Product> findAll() { return repo.findAll(); }
    public Optional<Product> findById(String id) { return repo.findById(id);}

    public void save(Product product) {
    repo.save(product);
}

public void deleteById(String id) {
    repo.deleteById(id);
}

}