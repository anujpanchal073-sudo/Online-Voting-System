package com.anuj.onlineVoting.Repository;

import com.anuj.onlineVoting.Entities.Candidate;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandidateRepository extends MongoRepository<Candidate, ObjectId> {
    Candidate findByid(ObjectId id);
    Candidate findByemail(String email);
}
