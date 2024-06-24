package com.SaiGonEats.SaiGonEats.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ShoppingCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "shoppingCartID")
    private ShoppingCart shoppingCart;
    @ManyToOne
    @JoinColumn(name = "menuItemID")
    private MenuItem menuItem;
    private int quantity;
    private int price;
}
