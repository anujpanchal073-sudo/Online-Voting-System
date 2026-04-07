package com.anuj.onlineVoting.Entities;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

@Data
public class Vote {

    private String voteId;

    //Map<post, idOfCandidate>
    private Map<String, ObjectId> votes = new HashMap<>();
}