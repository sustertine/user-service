package com.suster.user.service;

import com.suster.user.dao.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class UserService {
    @Inject
    private UserRepository userRepository;
}
