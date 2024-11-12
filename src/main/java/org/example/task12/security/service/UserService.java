package org.example.task12.security.service;

import lombok.RequiredArgsConstructor;
import org.example.task12.entity.ApiUser;
import org.example.task12.security.exceptions.UserAlreadyRegisteredException;
import org.example.task12.security.repository.ApiUserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final ApiUserRepository apiUserRepository;

    public void ifUserRegisteredThrowException(String login) {
        apiUserRepository.findByLogin(login)
                .ifPresent(user -> {
                    throw new UserAlreadyRegisteredException("User with username " + login + " already exists");
                });
    }

    public void save(ApiUser user) {
        apiUserRepository.save(user);
    }

    @Override
    public ApiUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return apiUserRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
