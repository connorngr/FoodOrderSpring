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

    @PostMapping("/new")
    public String saveMenu(@Valid @ModelAttribute Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            return "menu/form";
        }
        menuService.addMenu(menu);
        return "redirect:/menu";
    }
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Menu menu = menuService.getMenuById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("menu", menu);
        return "/menu/update";

    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, @Valid Menu menu,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            menu.setMenuID(id);
            return "/menu/update";
        }
        menuService.updateMenu(menu);
        model.addAttribute("menus", menuService.getAllMenus());
        return "redirect:/menu";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, Model model) {
        Menu category = menuService.getMenuById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        menuService.deleteMenu(id);
        model.addAttribute("menus", menuService.getAllMenus());
        return "redirect:/menu";
    }
}
