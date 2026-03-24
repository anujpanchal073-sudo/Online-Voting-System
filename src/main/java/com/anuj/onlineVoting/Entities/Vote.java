package com.anuj.onlineVoting.Entities;

import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

public class Vote {

    //Map<post, idOfCandidate>
    private Map<String, ObjectId> vote = new HashMap<>();
}
