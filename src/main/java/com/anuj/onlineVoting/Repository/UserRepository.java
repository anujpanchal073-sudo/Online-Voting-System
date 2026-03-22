package com.anuj.onlineVoting.Repository;

import com.anuj.onlineVoting.Entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends MongoRepository<User, ObjectId> {
    public User findByemail(String email);
}
