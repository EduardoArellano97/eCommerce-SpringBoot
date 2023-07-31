package com.example.eCommerceSpringBoot.repository;

import com.example.eCommerceSpringBoot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

}
