package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.Poll;
import com.anuj.onlineVoting.Repository.PollRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PollService {

    @Autowired
    PollRepository pollRepo;

    public Poll findPoll(ObjectId pollId){
        return pollRepo.findByid(pollId);
    }
}
