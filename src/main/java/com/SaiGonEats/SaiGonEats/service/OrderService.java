package com.SaiGonEats.SaiGonEats.service;

import com.SaiGonEats.SaiGonEats.model.Order;
import com.SaiGonEats.SaiGonEats.model.User;
import com.SaiGonEats.SaiGonEats.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByUserUsername(username);
    }
    public Order getOrderByIdAndUserEmail(Long orderId, String email) {
        return orderRepository.findByOrderIDAndUserUsername(orderId, email);
    }
}
