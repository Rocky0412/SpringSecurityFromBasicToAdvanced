package org.example.springsecurity.Services;

import org.example.springsecurity.Helper.AuthUserDetails;
import org.example.springsecurity.Repositories.UserRepository;
import org.example.springsecurity.models.Users;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImp implements UserDetailsService {
     final private UserRepository userRepository;
    public UserDetailsServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @NullMarked
    public  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new AuthUserDetails(user);

    }
}
