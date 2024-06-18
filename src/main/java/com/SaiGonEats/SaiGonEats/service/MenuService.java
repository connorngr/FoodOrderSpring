package com.SaiGonEats.SaiGonEats.service;

import com.SaiGonEats.SaiGonEats.model.Menu;
import com.SaiGonEats.SaiGonEats.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }
}
