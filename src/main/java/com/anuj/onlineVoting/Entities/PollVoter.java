package com.anuj.onlineVoting.Entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "PollVoters")
public class PollVoter {

    @Id
    private ObjectId id;

    private ObjectId pollId;

    private ObjectId voterId;

    private String title_of_poll;

    //List of posts for which the voter is eligible to vote
    private List<String> posts = new ArrayList<>();

    //Map<name of posts, true or false that a voter has casted vote for the corresponding post
    private Map<String, Boolean> has_voted = new HashMap<>();

}
