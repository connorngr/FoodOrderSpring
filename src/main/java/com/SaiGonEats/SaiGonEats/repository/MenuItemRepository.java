package com.SaiGonEats.SaiGonEats.repository;


import com.SaiGonEats.SaiGonEats.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
