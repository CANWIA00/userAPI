package org.blaze.userapi.util;

import jakarta.annotation.PreDestroy;
import org.blaze.userapi.model.*;
import org.blaze.userapi.repository.FriendRepository;
import org.blaze.userapi.repository.ProfileRepository;
import org.blaze.userapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class DataMigration implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataMigration.class);
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;

    public DataMigration(UserRepository userRepository, ProfileRepository profileRepository, FriendRepository friendRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.friendRepository = friendRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreDestroy
    public void cleanUp() {
        log.info("Cleaning up database before shutdown...");
        profileRepository.deleteAll();
        userRepository.deleteAll();
        friendRepository.deleteAll();
        log.info("Database cleanup complete.");
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Starting Data Migration...");

        if (userRepository.count() > 0 && friendRepository.count() > 0 && profileRepository.count() > 0) {
            friendRepository.deleteAll();
            log.info("Users already exist, skipping migration.");
            return;
        }


        try {
            // Create and save users first
            User user1 = createUser("111@gmail.com", "123", Role.USER);
            User user2 = createUser("222@gmail.com", "123", Role.USER);
            
            // Ensure users are saved and managed by persistence context
            user1 = userRepository.save(user1);
            user2 = userRepository.save(user2);

            // Create and save profiles with managed user entities
            Profile profile1 = createProfile(user1, "111_name", "photo", "bio");
            Profile profile2 = createProfile(user2, "222_name", "photo", "bio");
            
            // Save profiles
            profile1 = profileRepository.save(profile1);
            profile2 = profileRepository.save(profile2);

            log.info("Data Migration Processed Successfully!");
            log.info("Inserted Users: {}", userRepository.findAll());
            log.info("Inserted Profiles: {}", profileRepository.findAll());
        } catch (Exception e) {
            log.error("Error during data migration: ", e);
            throw e; // Re-throw to ensure the application fails if migration fails
        }

        userRepository.findAll().forEach(System.out::println);
        profileRepository.findAll().forEach(System.out::println);

    }

    private User createUser(String email, String rawPassword, Role role) {
        return new User(
                null,
                email,
                passwordEncoder.encode(rawPassword),
                LocalDateTime.now(),
                null,
                List.of(),
                List.of(),
                List.of(),
                role
        );
    }

    private Profile createProfile(User user, String fullName, String photo, String bio) {
        return new Profile(
                null,
                user,
                fullName,
                photo,
                bio,
                LocalDate.now(),
                Status.AVAILABLE,
                Instant.now(),
                List.of(),
                List.of()
        );
    }
}
