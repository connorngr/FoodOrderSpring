package com.SaiGonEats.SaiGonEats.service;


import com.SaiGonEats.SaiGonEats.model.*;
import com.SaiGonEats.SaiGonEats.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private ShoppingCartItemRepository  shoppingCartItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public ShoppingCart getCartByUser(String username) {
        Optional<User> optionalUser = iUserRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            ShoppingCart cart = user.getShoppingCart();
            if (cart == null) {
                cart = new ShoppingCart();
                cart.setUser(user);
                user.setShoppingCart(cart);
                shoppingCartRepository.save(cart);
            }
            return cart;
        }
        return null;
    }

    public List<ShoppingCart> getCart() {
        // Fetch the current order
        List<ShoppingCart> cartList = shoppingCartRepository.findAll();
        // Return the order items
        return cartList;
    }

    public void addItemToCart(Long menuItemID, int quantity, String username) {
        Optional<User> optionalUser = iUserRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            ShoppingCart cart = user.getShoppingCart();
            //Create new cart if not exist
            if (cart == null) {
                cart = new ShoppingCart();
                cart.setUser(user);
                user.setShoppingCart(cart);
                shoppingCartRepository.save(cart);
            }
            //Add item to shopping cart item
            MenuItem menuItem = menuItemRepository.findById(menuItemID).orElseThrow();
            ShoppingCartItem cartItem = new ShoppingCartItem();
            cartItem.setMenuItem(menuItem);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(menuItem.getPrice());
            cartItem.setShoppingCart(cart);
            cart.getItems().add(cartItem);
            shoppingCartRepository.save(cart);
        }
    }

    public void removeItemFromCart(Long cartItemID, String username) {
        ShoppingCartItem cartItem = shoppingCartItemRepository.findById(cartItemID).orElseThrow();
        shoppingCartItemRepository.delete(cartItem);
    }
//    public void checkout(String username) {
//        Optional<User> optionalUser = iUserRepository.findByUsername(username);
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            ShoppingCart cart = user.getShoppingCart();
//            if (cart != null && !cart.getItems().isEmpty()) {
//                Order order = new Order();
//                order.setUser(user);
//                order.setOrderDate(LocalDateTime.now());
//                order.setStatus("PENDING");
//                order.setAddress(user.getAddress());
//                order.setPhone(user.getPhone());
//
//                double totalAmount = 0.0;
//                for (ShoppingCartItem cartItem : cart.getItems()) {
//                    OrderDetail orderItem = new OrderDetail();
//                    orderItem.setOrder(order);
//                    orderItem.setMenuItem(cartItem.getMenuItem());
//                    orderItem.setQuantity(cartItem.getQuantity());
//                    orderItem.setPrice(cartItem.getPrice());
//                    totalAmount += cartItem.getQuantity() * cartItem.getPrice();
//                    order.getOrderDetails().add(orderItem);
//                }
//                cart.clearItems();
//                order.setTotalAmount(totalAmount);
//                orderRepository.save(order);
//            }
//        }
//    }
}
