package com.vaidyo.vaidyo_backend.config;

import com.vaidyo.vaidyo_backend.service.AdminService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminDataInitializer implements ApplicationRunner {

    private final AdminService adminService;

    public AdminDataInitializer(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        adminService.seedDefaultAdmin();
    }
}