package com.anuj.onlineVoting.Entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "PollCandidates")
public class PollCandidate {

    @Id
    private ObjectId id;

    private ObjectId pollId;

    private ObjectId candidateId;

    private String title_of_pole;

    private String post;

    private int votes;

}
