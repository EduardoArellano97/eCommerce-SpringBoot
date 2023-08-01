package com.example.eCommerceSpringBoot.service;

import com.example.eCommerceSpringBoot.model.Order;
import com.example.eCommerceSpringBoot.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IOrderServiceImpl  implements  IOrderService{
    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return  orderRepository.save(order);
    }
}
