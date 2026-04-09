package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.PollCandidate;
import com.anuj.onlineVoting.Repository.CriteriaRepository;
import com.anuj.onlineVoting.Repository.PollCandidateRepository;
import com.anuj.onlineVoting.Repository.PollVoterRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class PollCandidateService {

    @Autowired
    CriteriaRepository criteriaRepo;

    @Autowired
    PollCandidateRepository pollCandidateRepo;

    public ResponseEntity<?> castVote(ObjectId pollId, ObjectId voterId,  Map<String, ObjectId> votes){
        return criteriaRepo.addVote(pollId, voterId, votes);
    }

    public List<PollCandidate> getPollCandidatesForResult(String pollId){
        return criteriaRepo.getPollCandidatesForResult(new ObjectId(pollId));
    }

    public PollCandidate save(PollCandidate pollCandidate){
        return pollCandidateRepo.save(pollCandidate);
    }
}
