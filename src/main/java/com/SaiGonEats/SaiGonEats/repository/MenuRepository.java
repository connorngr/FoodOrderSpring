package com.SaiGonEats.SaiGonEats.repository;

import com.SaiGonEats.SaiGonEats.model.Menu;
import com.SaiGonEats.SaiGonEats.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
