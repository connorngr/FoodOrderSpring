package com.SaiGonEats.SaiGonEats.data;

import com.SaiGonEats.SaiGonEats.model.Role;
import com.SaiGonEats.SaiGonEats.model.User;
import com.SaiGonEats.SaiGonEats.repository.IRoleRepository;
import com.SaiGonEats.SaiGonEats.repository.IUserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    public DataInitializer(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initializeRoles();
        initializeMasterAccount();
    }

    private void initializeRoles() {
        // Kiểm tra xem các roles đã tồn tại trong cơ sở dữ liệu hay chưa
        if (roleRepository.count() == 0) {
            Role adminRole = new Role("ADMIN", "ADMIN");
            Role employeeRole = new Role("EMPLOYEE", "EMPLOYEE");
            Role userRole = new Role("USER", "USER");
            roleRepository.saveAll(Arrays.asList(adminRole, employeeRole, userRole));
        }
    }

    private void initializeMasterAccount() {
        // Kiểm tra xem tài khoản master đã tồn tại trong cơ sở dữ liệu hay chưa
        if (userRepository.count() == 0) {
            Role masterRole = roleRepository.findByName("ADMIN");

            User masterUser = new User();
            masterUser.setEmail("hani101003@gmail.com");
            masterUser.setPhone("0981020042");
            masterUser.setUsername("Lộc");
            masterUser.setAddress("34A, Linh Trung");
            masterUser.setPassword(new BCryptPasswordEncoder().encode("Abc@123"));
            masterUser.setRoles(Collections.singleton(masterRole));

            userRepository.save(masterUser);
        }
    }
}