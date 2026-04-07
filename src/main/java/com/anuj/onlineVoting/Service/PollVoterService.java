package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.PollVoter;
import com.anuj.onlineVoting.Repository.CriteriaRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PollVoterService {

    @Autowired
    CriteriaRepository criteriaRepo;

    public PollVoter findPollVoter(ObjectId pollId, ObjectId voterId){
        return criteriaRepo.findPollVoterByVoterIdAndPollId(pollId, voterId);
    }
}
