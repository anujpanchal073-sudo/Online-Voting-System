package com.anuj.onlineVoting.Entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "Applicants")
public class Applicant {

    @Id
    private ObjectId id;

    private ObjectId appliedPollId;

    private String email;

    private String name;

    private String password;

    private LocalDateTime timeOfApplication;

    private String post_applied_for;

    private String role;

}
