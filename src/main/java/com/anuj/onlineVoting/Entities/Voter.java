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
@Document(collection = "Voters")
public class Voter {

    @Id
    private ObjectId id;

    //List<Ids of Polls for which voter is eligible to vote
    private List<ObjectId> appliedPollId = new ArrayList<>();

    @Indexed(unique = true)
    private String email;

    private String name;

    private String password;

    private String role;

    //Map<Id of Polls, List of posts for which voter is eligible to vote in a specific Poll>
//    private Map<ObjectId,List<String>> posts = new HashMap<>();

    //Map<id of Poll, List of posts for which the voter has voted>
//    private Map<ObjectId, List<String>> has_voted = new HashMap<>();

}