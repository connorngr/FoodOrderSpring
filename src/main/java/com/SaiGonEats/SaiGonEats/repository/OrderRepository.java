package com.SaiGonEats.SaiGonEats.repository;

import com.SaiGonEats.SaiGonEats.model.Order;
import com.SaiGonEats.SaiGonEats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}