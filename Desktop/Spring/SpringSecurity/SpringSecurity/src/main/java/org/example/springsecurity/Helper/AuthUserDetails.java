package org.example.springsecurity.Helper;

import org.example.springsecurity.models.Users;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthUserDetails extends Users implements UserDetails {

    private String username;
    private String password;
    public AuthUserDetails(Users user){
        this.username = user.getUsername();
        this.password = user.getPassword();

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    @NullMarked
    public String getUsername() {
        return this.username;
    }
}
