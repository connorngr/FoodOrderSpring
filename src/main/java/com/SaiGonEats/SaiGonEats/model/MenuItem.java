package com.SaiGonEats.SaiGonEats.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@Data
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

    private String description;
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private double price;

    @ElementCollection
    private List<String> images;

    @Transient
    private MultipartFile imageFile;

    @Transient
    private List<MultipartFile> imageFiles;
}
