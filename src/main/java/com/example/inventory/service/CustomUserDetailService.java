package com.example.inventory.service;

import com.example.inventory.User.CustomUserDetail;
import com.example.inventory.User.User;
import com.example.inventory.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user= userepo.findUserByEmail(email);
        user.orElseThrow(()->new UsernameNotFoundException("User not available"));
        return user.map(CustomUserDetail::new).get();

    }
}
