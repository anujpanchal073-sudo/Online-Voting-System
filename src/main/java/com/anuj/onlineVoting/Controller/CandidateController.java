package com.anuj.onlineVoting.Controller;

import com.anuj.onlineVoting.Entities.Result;
import com.anuj.onlineVoting.Service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("candidate")
public class CandidateController {

    @Autowired
    CandidateService candidateService;

    @GetMapping("check")
    public String check(){
        return "Working fine";
    }

    public ResponseEntity<?> viewVotes(@PathVariable String pollId){
        return ResponseEntity
                .ok(Objects.requireNonNullElse(candidateService.viewVotes(pollId), "No such poll found or Result has not declared yet."));
    }
}
