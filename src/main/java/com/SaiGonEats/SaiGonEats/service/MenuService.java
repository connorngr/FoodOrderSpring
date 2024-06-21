package com.SaiGonEats.SaiGonEats.service;

import com.SaiGonEats.SaiGonEats.model.Menu;
import com.SaiGonEats.SaiGonEats.repository.MenuRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }
    public void addMenu(Menu menu) {
        menuRepository.save(menu);
    }
    public void updateMenu(@NotNull Menu menu) {
        Menu existingMenu = menuRepository.findById(menu.getMenuID())
                .orElseThrow(() -> new IllegalStateException("Category with ID " + menu.getMenuID() + " does not exist."));
        existingMenu.setName(menu.getName());
        menuRepository.save(existingMenu);
    }
    public void deleteMenu(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new IllegalStateException("Category with ID " + id + " does not exist.");
        }
        menuRepository.deleteById(id);
    }
}
