package com.anuj.onlineVoting.Repository;

import com.anuj.onlineVoting.Entities.PollCandidate;
import com.anuj.onlineVoting.Entities.PollVoter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CriteriaRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public PollVoter findPollVoterByEmailAndPollId(ObjectId pollId, ObjectId voterId){
        Query query = new Query();
        query.addCriteria(Criteria.where("pollId").is(pollId).and("voterId").is(voterId));
        List<PollVoter> pollVoters = mongoTemplate.find(query, PollVoter.class);
        return  pollVoters.get(0);
    }

    public PollCandidate findPollCandidateByEmailAndPollId(ObjectId pollId, ObjectId candidateId){
        Query query = new Query();
        query.addCriteria(Criteria.where("pollId").is(pollId).and("voterId").is(candidateId));
        List<PollCandidate> pollCandidates = mongoTemplate.find(query, PollCandidate.class);
        return  pollCandidates.get(0);
    }

}
