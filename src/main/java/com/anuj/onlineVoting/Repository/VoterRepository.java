package com.anuj.onlineVoting.Repository;

import com.anuj.onlineVoting.Entities.Voter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoterRepository extends MongoRepository<Voter, ObjectId> {
    Voter findByemail(String email);
}
