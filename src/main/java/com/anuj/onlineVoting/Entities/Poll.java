package com.anuj.onlineVoting.Entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "Polls")
public class Poll {

    @Id
    private ObjectId id;

    private String title;

    private List<String> posts = new ArrayList<>();

    private LocalDateTime enrollStartDateTime;

    private LocalDateTime enrollEndDateTime;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private Map<String, List<ObjectId>> candidates = new HashMap<>();

    private Map<String, List<ObjectId>> voters = new HashMap<>();

    private List<ObjectId> applicantsOfCandidature = new ArrayList<>();

    private List<ObjectId> applicantsOfVoter = new ArrayList<>();
}
