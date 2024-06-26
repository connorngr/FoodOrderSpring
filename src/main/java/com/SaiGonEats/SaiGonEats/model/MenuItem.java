package com.SaiGonEats.SaiGonEats.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Setter
@Getter
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuItemID;

    @ManyToOne
    @JoinColumn(name = "menuID")
    private Menu menu;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    @Size(min = 2, max = 1000, message = "Check your description!")
    private String description;
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private int price;
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ShoppingCartItem> shoppingCartItems;
    @OneToMany(mappedBy = "menuItem")
    private List<OrderDetail> orderDetail;
    @NotNull
    @ElementCollection
    private List<String> images;

    @Transient
    private MultipartFile imageFile;

    @Transient
    private List<MultipartFile> imageFiles;
}
