package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.Applicant;
import com.anuj.onlineVoting.Entities.Candidate;
import com.anuj.onlineVoting.Entities.Poll;
import com.anuj.onlineVoting.Repository.CandidateRepository;
import com.anuj.onlineVoting.Repository.PollRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CandidateService {

    @Autowired
    CandidateRepository candidateRepo;

    @Autowired
    PollRepository pollRepo;

    public ResponseEntity<?> saveCandidate(Applicant applicant){
        try{
            Candidate candidate = new Candidate();
            BeanUtils.copyProperties(applicant,candidate);
            candidate.getAppliedPollId().add(applicant.getAppliedPollId());
            return ResponseEntity.ok(candidateRepo.save(candidate));
        }
        catch(Exception e){
            return ResponseEntity.ok("Some error occured");
        }
    }

//    public ResponseEntity<?> seeVotes(String email){
//        Candidate candidate = candidateRepo.findByemail(email);
//        if(candidate != null){
//            ObjectId pollId = candidate.getAppliedPollId();
//            Poll poll = pollRepo.findByid(pollId);
//            LocalDateTime presentTime = LocalDateTime.now();
//            if(presentTime.isBefore(poll.getStartDateTime())){
//                return ResponseEntity.ok("Polls not started yet.");
//            }
//            else if (!presentTime.isBefore(poll.getStartDateTime())&& !presentTime.isAfter(poll.getEndDateTime())){
//                return ResponseEntity.ok("Polls are ongoing. Wait for the polls to end.");
//            }
//
//            return ResponseEntity.ok(candidate);
//        }
//        else{
//            return ResponseEntity.ok("No such candidate found");
//        }
//    }
}
