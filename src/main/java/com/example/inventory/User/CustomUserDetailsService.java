package com.example.inventory.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetail(user);
    }

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException{
        User user=userRepo.findByEmail(email);

        if(user!=null){
            user.setResetPasswordToken(token);
            userRepo.save(user);
        }

        else{
            throw new UserNotFoundException("Couldn't find any customer with email"+email);
        }
    }

    public User get(String resetPasswordToken){

        return userRepo.findByResetPasswordToken(resetPasswordToken);
    }

    public void updatePassword(User user,String newPassword){
        BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
        String encodedPassword=passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);

        userRepo.save(user);
    }

}
