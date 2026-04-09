package com.anuj.onlineVoting.Controller;

import com.anuj.onlineVoting.Entities.Applicant;
import com.anuj.onlineVoting.Entities.Candidate;
import com.anuj.onlineVoting.Entities.User;
import com.anuj.onlineVoting.Entities.Voter;
import com.anuj.onlineVoting.Service.ApplicantService;
import com.anuj.onlineVoting.Service.PublicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("public")
public class PublicController {

    @Autowired
    PublicService publicService;

    @Autowired
    ApplicantService applicantService;

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

    @PostMapping("login/user")
    public String login(@RequestBody User user){
        return publicService.loginUser(user);
    }

    @PostMapping("login/candidate")
    public String login(@RequestBody Candidate candidate){
        return publicService.loginCandidate(candidate);
    }

    @PostMapping("login/voter")
    public String login(@RequestBody Voter voter){
        return publicService.loginVoter(voter);
    }

}
