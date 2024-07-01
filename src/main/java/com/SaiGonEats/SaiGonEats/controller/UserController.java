package com.SaiGonEats.SaiGonEats.controller;

import com.SaiGonEats.SaiGonEats.model.Role;
import com.SaiGonEats.SaiGonEats.model.User;
import com.SaiGonEats.SaiGonEats.repository.RoleRepository;
import com.SaiGonEats.SaiGonEats.repository.UserRepository;
import com.SaiGonEats.SaiGonEats.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


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

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userRepository.findAll().stream()
                .filter(user -> !hasMasterRole(user))
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        return "user/user-list";
    }

    private boolean hasMasterRole(User user) {
        for (Role role : user.getRoles()) {
            if (role.getName().equals("ADMIN")) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/users/{userId}/edit")
    public String editUser(@PathVariable Long userId, Model model) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + userId));
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "user/user-detail";
    }

    @PostMapping("/users/{userId}/update-role")
    public String updateRole(@PathVariable Long userId, @RequestParam("roleId") Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role id: " + roleId));

        user.getRoles().clear();
        user.getRoles().add(role);
        userRepository.save(user);

        return "redirect:/users";
    }
}