package com.anuj.onlineVoting.Entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "Candidates")
public class Candidate {

    @Id
    private ObjectId id;

    private List<ObjectId> appliedPollId = new ArrayList<>();

    @Indexed(unique = true)
    private String email;

    private String name;

    private String password;

    private String role;

    // Map<pollId, number of votes a candidate got in a particular poll
//    private Map<ObjectId,Integer> votes = new HashMap<>();

}