package com.SaiGonEats.SaiGonEats.controller;

import com.SaiGonEats.SaiGonEats.model.MenuItem;
import com.SaiGonEats.SaiGonEats.service.MenuItemService;
import com.SaiGonEats.SaiGonEats.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/")
    public String getAllMenuItem(Model model) {
        List<MenuItem> menuItems = menuItemService.getAllMenuItem();
        model.addAttribute("menus",menuService.getAllMenus());
        model.addAttribute("menuItems", menuItems);
        return "home/home";
    }
}