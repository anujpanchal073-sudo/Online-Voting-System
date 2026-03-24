package com.anuj.onlineVoting.Controller;

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

    @PostMapping("cast-vote")
    public ResponseEntity<?> castVote(@RequestBody Vote vote){
        try{
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}
