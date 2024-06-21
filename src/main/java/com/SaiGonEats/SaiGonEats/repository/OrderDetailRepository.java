package com.SaiGonEats.SaiGonEats.repository;

import com.SaiGonEats.SaiGonEats.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}