package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Repository.CriteriaRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class PollCandidateService {

    @Autowired
    CriteriaRepository criteriaRepo;

    public ResponseEntity<?> castVote(ObjectId pollId, ObjectId voterId,  Map<String, ObjectId> votes){
        return ResponseEntity.ok(criteriaRepo.addVote(pollId, voterId, votes));
    }
}
