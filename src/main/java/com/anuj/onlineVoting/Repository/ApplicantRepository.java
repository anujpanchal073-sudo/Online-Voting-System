package com.anuj.onlineVoting.Repository;

import com.anuj.onlineVoting.Entities.Applicant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicantRepository extends MongoRepository<Applicant, ObjectId> {
    Applicant findByid(ObjectId id);
}
