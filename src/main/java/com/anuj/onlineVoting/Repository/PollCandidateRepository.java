package com.anuj.onlineVoting.Repository;

import com.anuj.onlineVoting.Entities.PollCandidate;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PollCandidateRepository extends MongoRepository<PollCandidate, ObjectId> {
}
