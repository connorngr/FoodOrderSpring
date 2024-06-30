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
    public void updateMenuItem(Long id, MenuItem updatedMenuItem) {
        MenuItem existingMenuItem = getMenuItemById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu item Id:" + id));
        // Copy properties from updatedMenuItem to existingMenuItem
        existingMenuItem.setName(updatedMenuItem.getName());
        existingMenuItem.setDescription(updatedMenuItem.getDescription());
        existingMenuItem.setPrice(updatedMenuItem.getPrice());
        existingMenuItem.setMenu(updatedMenuItem.getMenu());
        existingMenuItem.setImages(updatedMenuItem.getImages());
        menuItemRepository.save(existingMenuItem);
    }

    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
    public List<MenuItem> searchMenuItems(String menuName, String name) {
        if (menuName != null && !menuName.isEmpty() && name != null && !name.isEmpty()) {
            return menuItemRepository.searchByMenuNameAndItemName(menuName, name);
        } else if (menuName != null && !menuName.isEmpty()) {
            return menuItemRepository.findByMenuNameContainingIgnoreCase(menuName);
        } else if (name != null && !name.isEmpty()) {
            return menuItemRepository.findByNameContainingIgnoreCase(name);
        } else {
            return null; // Return all items if no search parameters are provided
        }
    }
}
