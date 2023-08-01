package com.example.eCommerceSpringBoot.repository;

import com.example.eCommerceSpringBoot.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDetail extends JpaRepository<OrderDetail,Long> {
}
