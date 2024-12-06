package com.chris.LaboratoryManagement.configuration;


import com.chris.LaboratoryManagement.constant.PredefinedRole;
import com.chris.LaboratoryManagement.entity.Role;
import com.chris.LaboratoryManagement.entity.User;
import com.chris.LaboratoryManagement.enums.RoleEnum;
import com.chris.LaboratoryManagement.repository.RoleRepository;
import com.chris.LaboratoryManagement.repository.SessionRepository;
import com.chris.LaboratoryManagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(RoleEnum.LECTURER)
                        .build());
//
//                Role adminRole = roleRepository.save(Role.builder()
//                        .name(PredefinedRole.ADMIN_ROLE)
//                        .build());
                Role adminRole = Role.builder()
                        .name(RoleEnum.ADMIN)
                        .build();

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .id("admin")
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .verified(true)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}