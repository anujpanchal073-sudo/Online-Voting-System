package com.anuj.onlineVoting.Entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Ballot {

    @Id
    private String ballotId;

    //Map<Post, List of id of candidates per post
    private Map<String, List<ObjectId>> candidates = new HashMap<>();

}