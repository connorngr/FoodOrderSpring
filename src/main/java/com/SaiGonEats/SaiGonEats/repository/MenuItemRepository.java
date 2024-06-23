package com.SaiGonEats.SaiGonEats.repository;


import com.SaiGonEats.SaiGonEats.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @Query("SELECT mi FROM MenuItem mi WHERE LOWER(mi.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MenuItem> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT mi FROM MenuItem mi JOIN mi.menu m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :menuName, '%'))")
    List<MenuItem> findByMenuNameContainingIgnoreCase(@Param("menuName") String menuName);
}
