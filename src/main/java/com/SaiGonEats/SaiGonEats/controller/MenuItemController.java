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

    private static final String UPLOADED_FOLDER = "src/main/resources/static/image/";

    @GetMapping("/view/{menuItemID}")
    public String getMenuItemById(@PathVariable Long menuItemID, Model model) {
        MenuItem menuItem = menuItemService.getMenuItemById(menuItemID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu item Id:" + menuItemID));
        model.addAttribute("menuItem", menuItem);
        return "item/detail"; // Ensure this path matches your Thymeleaf template
    }


    @GetMapping("/new")
    public String createMenuItemForm(Model model) {
        model.addAttribute("menuItem", new MenuItem());
        model.addAttribute("menus", menuService.getAllMenus());
        return "item/form";
    }

    @PostMapping("/new")
    public String saveMenuItem(@Valid @ModelAttribute MenuItem menuItem, BindingResult result, Model model) {
        
//        if (result.hasErrors()) {
//            model.addAttribute("menus", menuService.getAllMenus());
//            return "item/form";
//        }
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
            return "/image/" + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("/update/{menuItemID}")
    public String showUpdateForm(@PathVariable("menuItemID") Long menuItemID, Model model) {
        MenuItem menuItem = menuItemService.getMenuItemById(menuItemID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu item Id:" + menuItemID));
        model.addAttribute("menuItem", menuItem);
        model.addAttribute("menus", menuService.getAllMenus());
        return "item/update-form"; // Ensure this path matches your Thymeleaf template for updating
    }

    @PostMapping("/update/{menuItemID}")
    public String updateMenuItem(@PathVariable("menuItemID") Long menuItemID, @Valid @ModelAttribute MenuItem menuItem,
                                 BindingResult result, Model model) {
        if (!result.hasErrors()) {
            model.addAttribute("menus", menuService.getAllMenus());
            return "item/update-form"; // Ensure this path matches your Thymeleaf template for updating
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

        menuItemService.updateMenuItem(menuItemID, menuItem); // Ensure you have an update method in your service
        return "redirect:/"; // Redirect to a relevant page after updating
    }


    @GetMapping("/delete/{menuItemID}")
    public String deleteMenuItem(@PathVariable("menuItemID") Long menuItemID) {
        menuItemService.deleteMenuItem(menuItemID);
        return "redirect:/";
    }
    @GetMapping("/search")
    public String searchMenuItems(@RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "menuID", required = false) Long menuID,
                                  Model model) {
        String menuName = menuID != null ? menuService.getMenuById(menuID).get().getName() : null;
        List<MenuItem> menuItems = menuItemService.searchMenuItems(menuName, name);
        if (menuItems == null || menuItems.isEmpty()) {
            menuItems = null;
        }
        model.addAttribute("menus", menuService.getAllMenus());
        model.addAttribute("menuItems", menuItems);
        return "home/home"; // Ensure this path matches your Thymeleaf template for listing items
    }
}
