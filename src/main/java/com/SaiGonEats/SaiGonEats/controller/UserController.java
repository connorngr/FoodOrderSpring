package com.SaiGonEats.SaiGonEats.controller;

import com.SaiGonEats.SaiGonEats.model.User;
import com.SaiGonEats.SaiGonEats.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static String UPLOADED_FOLDER = "src/main/resources/static/images/";
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }
    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User()); // Thêm một đối tượng User mới vào model
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, // Validate đối tượng User
                           @NotNull BindingResult bindingResult, // Kết quả của quá trình validate
                           Model model) {
        if (bindingResult.hasErrors()) { // Kiểm tra nếu có lỗi validate
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "user/register"; // Trả về lại view "register" nếu có lỗi
        }
        MultipartFile file = user.getImageFile();
        if (file != null && !file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                user.setImage(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userService.save(user); // Lưu người dùng vào cơ sở dữ liệu
        userService.setDefaultRole(user.getUsername()); // Gán vai trò mặc định cho người dùng
        return "redirect:/login"; // Chuyển hướng người dùng tới trang "login"
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        Optional<User> userOptional = userService.getCurrentUser();
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "user/profile";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/profile/{id}")
    public String updateProfile(@PathVariable Long id, @Valid @ModelAttribute("user") User user, @RequestParam MultipartFile imageUser, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setID(id); // set id to keep it in the form in case of errors
            model.addAttribute("user", user);
            return "user/profile";
        }
        if (imageUser != null && imageUser.getSize() > 0)
        {
            try {
                File saveFile = new ClassPathResource("static/images").getFile();
                if (user.getImage() != null && !user.getImage().isEmpty()) {
                    Path oldImagePath = Paths.get(saveFile.getAbsolutePath() + File.separator + user.getImage());
                    Files.deleteIfExists(oldImagePath);
                }
                String newImageFile = UUID.randomUUID() + ".png";
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newImageFile);
                Files.copy(imageUser.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                user.setImage(newImageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Cập nhật thông tin người dùng
        userService.updateUser(user);
        // Redirect sau khi cập nhật thành công
        return "redirect:/profile";
    }
}