package com.anuj.onlineVoting.Controller;

import com.anuj.onlineVoting.Entities.Applicant;
import com.anuj.onlineVoting.Entities.User;
import com.anuj.onlineVoting.Service.ApplicantService;
import com.anuj.onlineVoting.Service.PublicService;
import com.anuj.onlineVoting.Service.UserDetailsImpl;
import com.anuj.onlineVoting.Utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("public")
public class PublicController {

    @Autowired
    PublicService publicService;

    @Autowired
    ApplicantService applicantService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsImpl userDetailsImpl;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/check")
    public String check(){
        return "Working Perfectly";
    }

    @PostMapping("user-registration")
    public boolean userRegistration(@RequestBody User user){
        return publicService.saveUser(user);
    }

    @PostMapping("application/candidate/{id}")
    public ResponseEntity<?> candidateApplication(@RequestBody Applicant application, @PathVariable String id){
        return applicantService.submitCandidatureApplication(application,id);
    }

    @PostMapping("application/voter/{id}")
    public ResponseEntity<?> voterApplication(@RequestBody Applicant application, @PathVariable String id){
        return applicantService.submitVoterApplication(application,id);
    }

    @PostMapping("login")
    public String login(@RequestBody User user){
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
