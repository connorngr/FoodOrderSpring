package com.SaiGonEats.SaiGonEats.service;


import com.SaiGonEats.SaiGonEats.model.MenuItem;
import com.SaiGonEats.SaiGonEats.repository.MenuItemRepository;
import jakarta.validation.constraints.NotNull;
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
    public void addMenuItem(MenuItem menuItem) {
        menuItemRepository.save(menuItem);
    }
    public void updateMenuItem(@NotNull MenuItem menuItem) {
        MenuItem existingMenuItem = menuItemRepository.findById(menuItem.getMenuItemID())
                .orElseThrow(() -> new IllegalStateException("Product with ID " + menuItem.getMenuItemID() + " does not exist."));
        existingMenuItem.setName(menuItem.getName());
        existingMenuItem.setPrice(menuItem.getPrice());
        existingMenuItem.setDescription(menuItem.getDescription());
        existingMenuItem.setMenu(menuItem.getMenu());
        existingMenuItem.setImage(menuItem.getImage());
        existingMenuItem.setImages(menuItem.getImages());
        menuItemRepository.save(existingMenuItem);
    }

    // Delete a product by its id
    public void deleteMenuItemById(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        menuItemRepository.deleteById(id);
    }
}
