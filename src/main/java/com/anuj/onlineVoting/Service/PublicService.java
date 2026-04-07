package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.User;
import com.anuj.onlineVoting.Repository.UserRepository;
import com.anuj.onlineVoting.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PublicService {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    UserRepository userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsImpl userDetailsImpl;

    @Autowired
    JwtUtil jwtUtil;

    public boolean saveUser(User user){
        try{
            user.setPassword(encoder.encode(user.getPassword()));
            user.getRoles().add("User");
            userRepo.save(user);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public String login(User user){
        try{
            authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            UserDetails userDetails = userDetailsImpl.loadUserByUsername(user.getEmail());
            return jwtUtil.generateToken(userDetails.getUsername());
        } catch (Exception e) {
            return "No such user found";
        }
    }

}