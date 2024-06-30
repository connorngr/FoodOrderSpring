package com.SaiGonEats.SaiGonEats.repository;

import com.SaiGonEats.SaiGonEats.model.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
}
