package com.anuj.onlineVoting.Repository;

import com.anuj.onlineVoting.Entities.Poll;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PollRepository extends MongoRepository<Poll, ObjectId> {
    Poll findByid(ObjectId id);
}
