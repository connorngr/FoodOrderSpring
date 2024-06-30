package com.SaiGonEats.SaiGonEats.repository;

import com.SaiGonEats.SaiGonEats.model.Order;
import com.SaiGonEats.SaiGonEats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserUsername(String username);

    Order findByOrderIDAndUserUsername(Long orderId, String username);


}