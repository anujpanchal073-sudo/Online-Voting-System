package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.Applicant;
import com.anuj.onlineVoting.Entities.Candidate;
import com.anuj.onlineVoting.Entities.Vote;
import com.anuj.onlineVoting.Entities.Voter;
import com.anuj.onlineVoting.Repository.VoterRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class VoterService {

    @Autowired
    VoterRepository voterRepo;

    public ResponseEntity<?> saveVoter(Applicant applicant){
        try{
            Voter voter = new Voter();
            BeanUtils.copyProperties(applicant,voter);
            voter.getAppliedPollId().add(applicant.getAppliedPollId());
            return ResponseEntity.ok(voterRepo.save(voter));
        }
        catch(Exception e){
            return ResponseEntity.ok("Some error occured");
        }
    }

    public ResponseEntity<?> castVote(Vote vote){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Voter voter = voterRepo.findByemail(email);
        if(voter != null){

        }
        return ResponseEntity.ok("ok");
    }

}
