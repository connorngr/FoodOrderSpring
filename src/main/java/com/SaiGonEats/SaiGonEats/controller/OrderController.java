package com.SaiGonEats.SaiGonEats.controller;

import com.SaiGonEats.SaiGonEats.model.Order;
import com.SaiGonEats.SaiGonEats.model.User;
import com.SaiGonEats.SaiGonEats.repository.OrderRepository;
import com.SaiGonEats.SaiGonEats.repository.IUserRepository;
import com.SaiGonEats.SaiGonEats.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String listOrders(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        List<Order> orders = orderService.getOrdersByUsername(currentUser.getUsername());
        model.addAttribute("orders", orders);
        return "order/list";
    }

    @GetMapping("/orders/{orderId}")
    public String viewOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetails currentUser, Model model) {
        Order order = orderService.getOrderByIdAndUserEmail(orderId, currentUser.getUsername());
        if (order == null) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "order/view";
    }
    @GetMapping("/ordersAdmin")
    public String listOrdersAdmin(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orderAdmin/list";
    }
    @GetMapping("/ordersAdmin/{orderId}")
    public String viewOrderAdmin(@PathVariable Long orderId, @AuthenticationPrincipal UserDetails currentUser, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return "redirect:/ordersAdmin";
        }
        model.addAttribute("order", order);
        return "orderAdmin/view";
    }

}