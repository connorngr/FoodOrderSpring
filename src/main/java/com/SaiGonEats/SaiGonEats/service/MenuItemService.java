package com.SaiGonEats.SaiGonEats.service;


import com.SaiGonEats.SaiGonEats.model.MenuItem;
import com.SaiGonEats.SaiGonEats.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;
    public List<MenuItem> getAllMenuItem() {
        return menuItemRepository.findAll();
    }
    public Optional<MenuItem> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }
    public MenuItem saveMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}
