package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.Applicant;
import com.anuj.onlineVoting.Entities.Candidate;
import com.anuj.onlineVoting.Repository.CandidateRepository;
import com.anuj.onlineVoting.Repository.PollRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

    @Autowired
    CandidateRepository candidateRepo;

    @Autowired
    PollRepository pollRepo;

    public Candidate findByemail(String email){
        return candidateRepo.findByemail(email);
    }

    public Candidate saveUpdate(Candidate candidate){
        return candidateRepo.save(candidate);
    }

    public ResponseEntity<?> saveNew(Applicant applicant){
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

}
