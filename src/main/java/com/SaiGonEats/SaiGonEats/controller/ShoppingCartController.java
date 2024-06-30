package com.SaiGonEats.SaiGonEats.controller;

import com.SaiGonEats.SaiGonEats.model.ShoppingCart;
import com.SaiGonEats.SaiGonEats.model.ShoppingCartItem;
import com.SaiGonEats.SaiGonEats.service.CheckOutService;
import com.SaiGonEats.SaiGonEats.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
//@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final CheckOutService checkOutService;


    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal) {
        ShoppingCart cart = shoppingCartService.getCartByUser(principal.getName());
        model.addAttribute("cart", cart.getItems());

        double totalValue = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        model.addAttribute("totalValue", totalValue);
        return "cart/view";
    }

    @PostMapping("cart/update")
    public String updateCartItem(@RequestParam Long cartItemID, @RequestParam int quantity, Principal principal) {
        shoppingCartService.updateCartItem(cartItemID, quantity, principal.getName());
        return "redirect:/cart";
    }

    @PostMapping("/cart/add")
    public String addItemToCart(@RequestParam Long menuItemID, @RequestParam int quantity, Principal principal) {
        shoppingCartService.addItemToCart(menuItemID, quantity, principal.getName());
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeItemFromCart(@RequestParam Long cartItemID, Principal principal) {
        shoppingCartService.removeItemFromCart(cartItemID, principal.getName());
        return "redirect:/cart";
    }

    @PostMapping(value = "/checkout")
    public String checkOut(HttpServletRequest request, Principal principal) {
        ShoppingCart cart = shoppingCartService.getCartByUser(principal.getName());
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = checkOutService.checkOutWithPayOnline(cart.getUser().getUsername(), baseUrl);
        System.out.println("vnpayUrl: " + vnpayUrl);
        return "redirect:" + vnpayUrl;
    }
    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus =checkOutService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", Integer.valueOf(totalPrice) / 100);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
}