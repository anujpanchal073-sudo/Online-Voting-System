package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.User;
import com.anuj.onlineVoting.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PublicService {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    UserRepository userRepo;

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

}
