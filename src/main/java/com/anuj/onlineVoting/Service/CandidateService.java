package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.Applicant;
import com.anuj.onlineVoting.Entities.Candidate;
import com.anuj.onlineVoting.Entities.PollCandidate;
import com.anuj.onlineVoting.Repository.CandidateRepository;
import com.anuj.onlineVoting.Repository.CriteriaRepository;
import com.anuj.onlineVoting.Repository.PollRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

    @Autowired
    CandidateRepository candidateRepo;

    @Autowired
    PollRepository pollRepo;

    @Autowired
    CriteriaRepository criteriaRepo;

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

    public PollCandidate viewVotes(String pollId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Candidate candidate = candidateRepo.findByemail(email);
        return criteriaRepo.findPollCandidateByCandidateIdAndPollId(new ObjectId(pollId), candidate.getId());
    }

}
