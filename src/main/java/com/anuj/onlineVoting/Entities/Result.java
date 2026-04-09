package com.anuj.onlineVoting.Entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@Document(collection = "Results")
public class Result {

    @Id
    private ObjectId pollId;

    //Map <Post, Map < CandidateId, Votes>>
    Map<String, Map<String, Integer>> result = new HashMap<>();

}
