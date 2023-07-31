package com.example.eCommerceSpringBoot.service;

import com.example.eCommerceSpringBoot.model.Product;
import com.example.eCommerceSpringBoot.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private IProductRepository IProductRepository;

    @Override
    public Product save(Product product) {
        return IProductRepository.save(product);
    }

    @Override
    public Optional<Product> get(Long id) {
        return IProductRepository.findById(id);
    }

    @Override
    public void update(Product product) {
        IProductRepository.save(product);

    }

    @Override
    public void delete(Long id) {
        IProductRepository.deleteById(id);
    }

    @Override
    public List<Product> findAll() {
        return IProductRepository.findAll();
    }
}
