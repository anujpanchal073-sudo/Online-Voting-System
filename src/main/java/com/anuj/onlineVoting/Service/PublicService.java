package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.Candidate;
import com.anuj.onlineVoting.Entities.Result;
import com.anuj.onlineVoting.Entities.User;
import com.anuj.onlineVoting.Entities.Voter;
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

    @Autowired
    ResultService resultService;

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

    public String loginUser(User user){
        try{
            authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            UserDetails userDetails = userDetailsImpl.loadUserByUsername(user.getEmail());
            return jwtUtil.generateToken(userDetails.getUsername(), "USER");
        } catch (Exception e) {
            return "No such user found";
        }
    }

    public String loginCandidate(Candidate candidate){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(candidate.getEmail(), candidate.getPassword())
            );
            UserDetails userDetails = userDetailsImpl.loadUserByUsername(candidate.getEmail());
            return jwtUtil.generateToken(userDetails.getUsername(), "CANDIDATE");
        } catch (Exception e) {
            return "No such candidate found";
        }
    }

    public String loginVoter(Voter voter){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(voter.getEmail(), voter.getPassword())
            );
            UserDetails userDetails = userDetailsImpl.loadUserByUsername(voter.getEmail());
            return jwtUtil.generateToken(userDetails.getUsername(), "VOTER");
        } catch (Exception e) {
            return "No such voter found";
        }
    }

    public Result findResult(String pollId){
        return resultService.findResult(pollId);
    }

}