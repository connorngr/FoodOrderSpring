package com.SaiGonEats.SaiGonEats.service;

import com.SaiGonEats.SaiGonEats.model.Restaurant;
import com.SaiGonEats.SaiGonEats.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
    public Optional<Restaurant> updateRestaurant(Long id, Restaurant restaurantDetails) {
        return restaurantRepository.findById(id).map(restaurant -> {
            restaurant.setName(restaurantDetails.getName());
            restaurant.setAddress(restaurantDetails.getAddress());
            restaurant.setPhone(restaurantDetails.getPhone());
            restaurant.setEmail(restaurantDetails.getEmail());
            return restaurantRepository.save(restaurant);
        });
    }
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
}
