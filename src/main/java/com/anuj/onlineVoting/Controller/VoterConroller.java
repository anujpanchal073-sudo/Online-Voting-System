package com.anuj.onlineVoting.Controller;

import com.anuj.onlineVoting.Entities.Ballot;
import com.anuj.onlineVoting.Entities.Vote;
import com.anuj.onlineVoting.Service.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("voter")
public class VoterConroller {

    @Autowired
    VoterService voterService;

    @GetMapping("generate-ballot/{pollId}")
    public ResponseEntity<?> generateBallot(@PathVariable String pollId){
        return voterService.generateBallot(pollId);
    }

    @PostMapping("cast-vote")
    public ResponseEntity<?> castVote(@RequestBody Vote vote){
        return voterService.castVote(vote);
    }

    @GetMapping("check")
    public String check(){
        return "Working";
    }
}
