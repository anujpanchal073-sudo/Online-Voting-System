package com.anuj.onlineVoting.Repository;

import com.anuj.onlineVoting.Entities.Result;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResultRepository extends MongoRepository<Result, ObjectId> {
    Result findByid(ObjectId id);
}
