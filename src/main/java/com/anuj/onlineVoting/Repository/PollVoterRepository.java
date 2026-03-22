package com.anuj.onlineVoting.Repository;

import com.anuj.onlineVoting.Entities.PollVoter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PollVoterRepository extends MongoRepository<PollVoter, ObjectId> {
}
