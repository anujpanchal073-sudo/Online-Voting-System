package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.Applicant;
import com.anuj.onlineVoting.Entities.Poll;
import com.anuj.onlineVoting.Repository.ApplicantRepository;
import com.anuj.onlineVoting.Repository.PollRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApplicantService {

    @Autowired
    PollRepository pollRepo;

    @Autowired
    ApplicantRepository applicantRepo;

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public Applicant findById(ObjectId applicantId){
        return applicantRepo.findByid(applicantId);
    }

    public void deleteById(ObjectId applicantId){
        applicantRepo.deleteById(applicantId);
    }

    public
    //FUNCTION TO SAVE APPLICATIONS
    //PUBLIC CONTROLLER - candidateApplication
    public ResponseEntity<?> submitCandidatureApplication(Applicant application, String p_id){
        ObjectId id = new ObjectId(p_id);
        Poll poll = pollRepo.findByid(id);

        if(poll == null){
            return ResponseEntity.ok("No such poll found");
        }

        LocalDateTime timeOfApplication = application.getTimeOfApplication();
        LocalDateTime startTime = poll.getEnrollStartDateTime();
        LocalDateTime endTime = poll.getEnrollEndDateTime();

        if(timeOfApplication.isBefore(startTime)){
            return ResponseEntity.ok("Application not started yet");
        }
        else if (timeOfApplication.isAfter(endTime)) {
            return ResponseEntity.ok("Application process is ended");
        }
        else{
            application.setRole("Candidate");
            application.setAppliedPollId(id);
            application.setPassword(encoder.encode(application.getPassword()));
            Applicant savedApplication = applicantRepo.save(application);
            poll.getApplicantsOfCandidature().add(savedApplication.getId());
            pollRepo.save(poll);
            return ResponseEntity.ok(application);
        }
    }

    //FUNCTION TO SAVE APPLICATIONS
    //PUBLIC CONTROLLER - voterApplication
    public ResponseEntity<?> submitVoterApplication(Applicant application, String p_id){
        ObjectId id = new ObjectId(p_id);
        Poll poll = pollRepo.findByid(id);

        if(poll == null){
            return ResponseEntity.ok("No such poll found");
        }

        LocalDateTime timeOfApplication = application.getTimeOfApplication();
        LocalDateTime startTime = poll.getEnrollStartDateTime();
        LocalDateTime endTime = poll.getEnrollEndDateTime();

        if(timeOfApplication.isBefore(startTime)){
            return ResponseEntity.ok("Application not started yet");
        }
        else if (timeOfApplication.isAfter(endTime)) {
            return ResponseEntity.ok("Application process is ended");
        }
        else{
            application.setRole("Voter");
            application.setAppliedPollId(id);
            application.setPassword(encoder.encode(application.getPassword()));
            Applicant savedApplication = applicantRepo.save(application);
            poll.getApplicantsOfVoter().add(savedApplication.getId());
            pollRepo.save(poll);
            return ResponseEntity.ok(application);
        }
    }

}
