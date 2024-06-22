package com.SaiGonEats.SaiGonEats.controller;


import com.SaiGonEats.SaiGonEats.model.Menu;
import com.SaiGonEats.SaiGonEats.model.MenuItem;
import com.SaiGonEats.SaiGonEats.service.MenuItemService;
import com.SaiGonEats.SaiGonEats.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.lang.String;


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
    @GetMapping("/list")
    public String getAllMenuItem(Model model) {
        model.addAttribute("menuItems", menuItemService.getAllMenuItem());
        return "item/list";
    }
    @GetMapping("/new")
    public String createMenuItemForm(Model model) {
        model.addAttribute("menuItem", new MenuItem());
        model.addAttribute("menus", menuService.getAllMenus());
        return "item/form";
    }

    @PostMapping("/new")
    public String saveMenuItem(@Valid @ModelAttribute MenuItem menuItem, BindingResult result, Model model,
                               @RequestParam("image") MultipartFile image,
                               @RequestParam("images") MultipartFile[] images) {
//        if (result.hasErrors()) {
//            model.addAttribute("menuItem", menuItem);
//            model.addAttribute("menus", menuService.getAllMenus());
//            return "item/form";
//        }

        List<String> images_temp = new ArrayList<>();

        // Handle single image file
        if (!image.isEmpty()) {
            try {
                String imageName = saveImageStatic(image);
                menuItem.setImage("/images/" + imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int i=0;

        for (MultipartFile image_temp : images) {
            if (!image_temp.isEmpty()) {
                try {
                    String imageUrl = saveImageStatic(image_temp);
                    images_temp.add("/images/" + imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Set the images field
        menuItem.setImages(images_temp);

//        MultipartFile imageFile = menuItem.getImageFile();
//        if (imageFile != null && !imageFile.isEmpty()) {
//            String imagePath = saveUploadedFile(imageFile);
//            images.add(imagePath);
//        }
//
//        // Handle multiple image files
//        List<MultipartFile> imageFiles = menuItem.getImageFiles();
//        if (imageFiles != null && !imageFiles.isEmpty()) {
//            for (MultipartFile file : imageFiles) {
//                if (!file.isEmpty()) {
//                    String imagePath = saveUploadedFile(file);
//                    images.add(imagePath);
//                }
//            }
//        }

        menuItemService.addMenuItem(menuItem);
        return "redirect:/";
    }

//    private String saveUploadedFile(MultipartFile file) {
//        try {
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//            Files.write(path, bytes);
//            return "/images/" + file.getOriginalFilename();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    private String saveImageStatic(MultipartFile image) throws IOException {

        Path dirImages = Paths.get("target/classes/static/images");
        if (!Files.exists(dirImages)) {
            Files.createDirectories(dirImages);
        }

        String newFileName = UUID.randomUUID()+ "." + StringUtils.getFilenameExtension(image.getOriginalFilename());

        Path pathFileUpload = dirImages.resolve(newFileName);
        Files.copy(image.getInputStream(), pathFileUpload,
                StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MenuItem menuItem = menuItemService.getMenuItemById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("menuItem", menuItem);
        model.addAttribute("menus", menuService.getAllMenus());
        return "/item/update";
    }
    // Process the form for updating a product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid MenuItem menuItem,
                                BindingResult result,@RequestParam("image") MultipartFile image,
                                @RequestParam("images") MultipartFile[] images) {
        if (result.hasErrors()) {
            menuItem.setMenuItemID(id); // set id to keep it in the form in case of errors
            return "/item/update";
        }
        List<String> images_temp = new ArrayList<>();

        // Handle single image file
        if (!image.isEmpty()) {
            try {
                String imageName = saveImageStatic(image);
                menuItem.setImage("/images/" + imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int i=0;

        for (MultipartFile image_temp : images) {
            if (!image_temp.isEmpty()) {
                try {
                    String imageUrl = saveImageStatic(image_temp);
                    images_temp.add("/images/" + imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        MultipartFile imageFile = menuItem.getImageFile();
//        if (imageFile != null && !imageFile.isEmpty()) {
//            String imagePath = saveUploadedFile(imageFile);
//            images.add(imagePath);
//        }
//
//        // Handle multiple image files
//        List<MultipartFile> imageFiles = menuItem.getImageFiles();
//        if (imageFiles != null && !imageFiles.isEmpty()) {
//            for (MultipartFile file : imageFiles) {
//                if (!file.isEmpty()) {
//                    String imagePath = saveUploadedFile(file);
//                    images.add(imagePath);
//                }
//            }
//        }

        // Set the images field
        menuItem.setImages(images_temp);

        menuItemService.updateMenuItem(menuItem);
        return "redirect:/";
    }
    // Handle request to delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        menuItemService.deleteMenuItemById(id);
        return "redirect:/";
    }
}
