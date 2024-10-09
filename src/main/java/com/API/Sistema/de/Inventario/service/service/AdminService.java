package com.API.Sistema.de.Inventario.service.service;

import com.API.Sistema.de.Inventario.persistence.entity.AdminEntity;
import com.API.Sistema.de.Inventario.persistence.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    private static final String[] ADJECTIVES = {"Happy", "Sunny", "Clever", "Bright", "Swift", "Calm", "Wise", "Kind"};
    private static final String[] NOUNS = {"Lion", "River", "Mountain", "Ocean", "Forest", "Star", "Moon", "Bird"};

    public String generatePassword() {
        Random random = new Random();
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];
        int number = random.nextInt(100);
        return adjective + noun + number;
    }

    public String getOrCreateSystemPassword() {
        List<AdminEntity> configs = adminRepository.findAll();
        if (configs.isEmpty()) {
            String newPassword = generatePassword();
            AdminEntity config = new AdminEntity();
            config.setPassword(newPassword);
            config.setLastGenerated(LocalDateTime.now());
            adminRepository.save(config);
            return newPassword;
        } else {
            return configs.get(0).getPassword();
        }
    }

    public boolean verifyPassword(String inputPassword) {
        String systemPassword = getOrCreateSystemPassword();
        return systemPassword.equals(inputPassword);
    }

    public String regeneratePassword() {
        String newPassword = generatePassword();
        List<AdminEntity> configs = adminRepository.findAll();
        AdminEntity config;
        if (configs.isEmpty()) {
            config = new AdminEntity();
        } else {
            config = configs.get(0);
        }
        config.setPassword(newPassword);
        config.setLastGenerated(LocalDateTime.now());
        adminRepository.save(config);
        return newPassword;
    }
}
