package com.example.eCommerceSpringBoot.service;

import com.example.eCommerceSpringBoot.model.Product;
import com.example.eCommerceSpringBoot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> get(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void update(Product product) {
        productRepository.save(product);

    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
