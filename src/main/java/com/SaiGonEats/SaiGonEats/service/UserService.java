package com.SaiGonEats.SaiGonEats.service;

import com.SaiGonEats.SaiGonEats.model.User;
import com.SaiGonEats.SaiGonEats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
    public Optional<User> getUserByID(Long id) {
        return userRepository.findById(id);
    }
    public User createUser(User user){
        return userRepository.save(user);
    }
    public Optional<User> updateUser(Long id, User user) {
        Optional<User> exsitingUser = userRepository.findById(id);
        if (exsitingUser.isPresent()) {
            User foundUser = exsitingUser.get();
            foundUser.setName(user.getName());
            foundUser.setEmail(user.getEmail());
            foundUser.setPassword(user.getPassword());
            foundUser.setPhone(user.getPhone());
            foundUser.setAddress(user.getAddress());
            userRepository.save(user);//The existing ID will override the current value
            return Optional.of(user);
        }
        else {
            return Optional.empty();
        }
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
