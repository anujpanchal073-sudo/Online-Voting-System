package com.anuj.onlineVoting.Repository;

import com.anuj.onlineVoting.Entities.PollCandidate;
import com.anuj.onlineVoting.Entities.PollVoter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CriteriaRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public PollVoter findPollVoterByVoterIdAndPollId(ObjectId pollId, ObjectId voterId){
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

    public ResponseEntity<?> addVote(ObjectId pollId, ObjectId voterId, Map<String, ObjectId> votes){

        for(Map.Entry<String, ObjectId> entry: votes.entrySet()){
            String post = entry.getKey();
            ObjectId candidateId = entry.getValue();

            mongoTemplate.updateFirst(
                    Query.query(Criteria
                            .where("pollId")
                            .is(pollId)
                            .and("voterId")
                            .is(voterId)),
                    new Update().set("has_voted." + post, true),
                    PollVoter.class
            );

            mongoTemplate.updateFirst(
                    Query.query(Criteria
                            .where("pollId")
                            .is(pollId)
                            .and("candidateId")
                            .is(candidateId)),
                    new Update().inc("votes",1),
                    PollCandidate.class
            );

        }
        return ResponseEntity.ok(true);
    }

}
