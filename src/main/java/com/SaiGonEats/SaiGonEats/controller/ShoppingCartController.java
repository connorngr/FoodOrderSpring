package com.SaiGonEats.SaiGonEats.controller;

import com.SaiGonEats.SaiGonEats.model.ShoppingCart;
import com.SaiGonEats.SaiGonEats.model.ShoppingCartItem;
import com.SaiGonEats.SaiGonEats.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        ShoppingCart cart = shoppingCartService.getCartByUser(principal.getName());
        model.addAttribute("cart", cart.getItems());
        return "cart/view";
    }

    @PostMapping("/add")
    public String addItemToCart(@RequestParam Long menuItemID, @RequestParam int quantity, Principal principal) {
        shoppingCartService.addItemToCart(menuItemID, quantity, principal.getName());
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItemFromCart(@RequestParam Long cartItemID, Principal principal) {
        shoppingCartService.removeItemFromCart(cartItemID, principal.getName());
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Principal principal) {
        shoppingCartService.checkout(principal.getName());
        return "redirect:/cart";
    }
}