package com.SaiGonEats.SaiGonEats.controller;


import com.SaiGonEats.SaiGonEats.model.Restaurant;
import com.SaiGonEats.SaiGonEats.service.RestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    @GetMapping
    public String getAllRestaurants(Model model) {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "restaurant/restaurant-list";
    }
    @GetMapping("/{id}")
    public String getRestaurantById(@PathVariable Long id, Model model) {
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(id);
        if (restaurant.isPresent()) {
            model.addAttribute("restaurant", restaurant.get());
            return "restaurant-detail"; // Thymeleaf template name
        } else {
            return "error/404"; // Thymeleaf template name for 404 error
        }
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return "restaurant/restaurant-form"; // Thymeleaf template name for creating a new restaurant
    }

    @PostMapping
    public String createRestaurant(@ModelAttribute Restaurant restaurant) {
        restaurantService.createRestaurant(restaurant);
        return "redirect:/restaurants";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(id);
        if (restaurant.isPresent()) {
            model.addAttribute("restaurant", restaurant.get());
            return "restaurant-form"; // Thymeleaf template name for editing a restaurant
        } else {
            return "error/404"; // Thymeleaf template name for 404 error
        }
    }

    @PostMapping("/update/{id}")
    public String updateRestaurant(@PathVariable Long id, @ModelAttribute Restaurant restaurantDetails) {
        restaurantService.updateRestaurant(id, restaurantDetails);
        return "redirect:/restaurants";
    }

    @GetMapping("/delete/{id}")
    public String deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return "redirect:/restaurants";
    }
}

