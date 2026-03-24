package com.anuj.onlineVoting.Controller;

import com.anuj.onlineVoting.Entities.Applicant;
import com.anuj.onlineVoting.Entities.User;
import com.anuj.onlineVoting.Service.ApplicantService;
import com.anuj.onlineVoting.Service.PublicService;
import com.anuj.onlineVoting.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public")
public class PublicController {

    @Autowired
    PublicService publicService;

    @Autowired
    ApplicantService applicantService;

    @Autowired
    UserService userService;

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

}
