package com.SaiGonEats.SaiGonEats.controller;


import com.SaiGonEats.SaiGonEats.model.Menu;
import com.SaiGonEats.SaiGonEats.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping
    public String getAllMenus(Model model) {
        model.addAttribute("title", "Index Page");
        model.addAttribute("menus", menuService.getAllMenus());
        return "menu/list";
    }
    @GetMapping("/{id}")
    public String getMenuById(@PathVariable Long id, Model model) {
        model.addAttribute("menu", menuService.getMenuById(id));
        return "menu/detail";
    }

    @GetMapping("/new")
    public String createMenuForm(Model model) {
        model.addAttribute("menu", new Menu());
        return "menu/form";
    }

    @PostMapping
    public String saveMenu(@Valid @ModelAttribute Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            return "menu/form";
        }
        menuService.saveMenu(menu);
        return "redirect:/menu";
    }

    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return "redirect:/menu";
    }
}
