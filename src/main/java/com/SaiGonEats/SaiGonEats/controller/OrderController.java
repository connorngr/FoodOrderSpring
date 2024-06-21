package com.SaiGonEats.SaiGonEats.controller;

import com.SaiGonEats.SaiGonEats.model.Order;
import com.SaiGonEats.SaiGonEats.model.User;
import com.SaiGonEats.SaiGonEats.repository.OrderRepository;
import com.SaiGonEats.SaiGonEats.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

}