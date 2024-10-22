package com.API.Sistema.de.Inventario.web.controller;

import com.API.Sistema.de.Inventario.service.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/regenerate-password")
    public ResponseEntity<String> regeneratePassword() {
        String newPassword = adminService.regeneratePassword();
        return ResponseEntity.ok("" + newPassword);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String password) {
        if (adminService.verifyPassword(password)) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.badRequest().body("Invalid password");
        }
    }
}
