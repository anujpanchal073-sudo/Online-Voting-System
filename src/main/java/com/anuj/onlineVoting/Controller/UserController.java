package com.anuj.onlineVoting.Controller;

import com.anuj.onlineVoting.Entities.Applicant;
import com.anuj.onlineVoting.Entities.Poll;
import com.anuj.onlineVoting.Entities.User;
import com.anuj.onlineVoting.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("email")
    public User getByEmail(){
        return userService.findByEmail();
    }

    @PostMapping("host-poll")
    public ObjectId hostPoll(@RequestBody Poll poll){
        return userService.hostPoll(poll);
    }

    @GetMapping("view-candidate-applicants/{idOfPoll}")
    public List<Applicant> viewCandidatureApplications(@PathVariable String idOfPoll){
        return userService.viewCandidatureApplications(idOfPoll);
    }

    @GetMapping("view-voter-applicants/{idOfPoll}")
    public List<Applicant> viewVoterApplications(@PathVariable String idOfPoll){
        return userService.viewVoterApplications(idOfPoll);
    }

    @DeleteMapping("reject-candidate/{id}")
    public Boolean rejectCandidatureApplication(@PathVariable String id){
        return userService.rejectCandidatureApplication(id);
    }

    @DeleteMapping("reject-voter/{id}")
    public Boolean rejectVoterApplication(@PathVariable String id){
        return userService.rejectVoterApplication(id);
    }

    @PostMapping("accept-candidate/{id}")
    public ResponseEntity<?> acceptCandidatureApplication(@PathVariable String id){
        return userService.acceptCandidatureApplication(id);
    }

    @PostMapping("accept-voter/{id}")
    public ResponseEntity<?> acceptVoterApplication(@PathVariable String id){
        return userService.acceptVoterApplication(id);
    }

}
