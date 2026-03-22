package com.anuj.onlineVoting.Controller;

import com.anuj.onlineVoting.Entities.Vote;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("voter")
public class VoterConroller {

    @PostMapping("cast-vote")
    public boolean castVote(@RequestBody Vote vote){
        return false;
    }
}
