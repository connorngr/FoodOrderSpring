package com.SaiGonEats.SaiGonEats.controller;


import com.SaiGonEats.SaiGonEats.model.Menu;
import com.SaiGonEats.SaiGonEats.model.MenuItem;
import com.SaiGonEats.SaiGonEats.service.MenuItemService;
import com.SaiGonEats.SaiGonEats.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/items")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private MenuService menuService;

    private static final String UPLOADED_FOLDER = "src/main/resources/static/images/";

    @GetMapping("/{id}")
    public String getMenuItemById(@PathVariable Long id, Model model) {
        model.addAttribute("menuItem", menuItemService.getMenuItemById(id));
        return "item/detail";
    }

    @GetMapping("/new")
    public String createMenuItemForm(Model model) {
        model.addAttribute("menuItem", new MenuItem());
        model.addAttribute("menus", menuService.getAllMenus());
        return "item/form";
    }

    @PostMapping
    public String saveMenuItem(@Valid @ModelAttribute MenuItem menuItem, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("menus", menuService.getAllMenus());
            return "item/form";
        }
        List<String> images = new ArrayList<>();

        // Handle single image file
        MultipartFile imageFile = menuItem.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveUploadedFile(imageFile);
            images.add(imagePath);
        }

        // Handle multiple image files
        List<MultipartFile> imageFiles = menuItem.getImageFiles();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String imagePath = saveUploadedFile(file);
                    images.add(imagePath);
                }
            }
        }

        // Set the images field
        menuItem.setImages(images);

        menuItemService.saveMenuItem(menuItem);
        return "redirect:/";
    }

    private String saveUploadedFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            return "/images/" + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return "redirect:/";
    }
}
