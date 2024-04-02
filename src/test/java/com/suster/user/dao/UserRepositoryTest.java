package com.suster.user.dao;

import com.suster.user.vao.User;
import io.quarkus.test.junit.QuarkusTest;

import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class UserRepositoryTest {

    @Inject
    UserRepository userRepository;

    @Test
    @DisplayName("Find user by email")
    public void findByEmail() {
        userRepository.registerUser(new User("test",
                "test",
                "test")).await().indefinitely();
        assertEquals("test", userRepository.findByEmail("test").await().indefinitely().getUsername());
    }

    @Test
    @DisplayName("Register user")
    public void register() {
        userRepository.registerUser(new User("test", "test", "test")).await().indefinitely();
        assertThat(userRepository.findByEmail("test").await().indefinitely().getUsername()).isEqualTo("test");
    }
}