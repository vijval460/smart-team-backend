package com.teamforge.teammatch.service;

import com.teamforge.teammatch.model.AppUser;
import com.teamforge.teammatch.repository.AppUserRepository;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final MemberDirectory memberDirectory;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String demoEmail;
    private final String demoPassword;

    public DatabaseSeeder(
            MemberDirectory memberDirectory,
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${teamforge.demo.email}") String demoEmail,
            @Value("${teamforge.demo.password}") String demoPassword
    ) {
        this.memberDirectory = memberDirectory;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.demoEmail = demoEmail;
        this.demoPassword = demoPassword;
    }

    @Override
    public void run(String... args) {
        memberDirectory.seedIfEmpty();
        String normalizedEmail = demoEmail.trim().toLowerCase(Locale.ROOT);
        if (!userRepository.existsById(normalizedEmail)) {
            userRepository.save(new AppUser(normalizedEmail, "Teamforge Demo", passwordEncoder.encode(demoPassword), "ROLE_USER"));
        }
    }
}