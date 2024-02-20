package com.ipsos.services.Impl;

import com.ipsos.entities.User;
import com.ipsos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.ipsos.constants.ErrorMessages.UserOperations.USERNAME_NOT_FOUND;


@Service
public class SecurityUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public SecurityUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND, username)));

        return user;
    }
}
