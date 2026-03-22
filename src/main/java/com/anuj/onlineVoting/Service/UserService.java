package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.*;
import com.anuj.onlineVoting.Repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    PollRepository pollRepo;

    @Autowired
    ApplicantRepository applicantRepo;

    @Autowired
    VoterRepository voterRepo;

    @Autowired
    CandidateRepository candidateRepo;

    @Autowired
    CandidateService candidateService;

    @Autowired
    VoterService voterService;

    @Autowired
    PollCandidateRepository pollCandidateRepo;

    @Autowired
    CriteriaRepository criteriaRepo;

    @Autowired
    PollVoterRepository pollVoterRepo;

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public User findByEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepo.findByemail(email);
    }

    public boolean EncryptPassword(String email){
        try{
            User userFromDb = userRepo.findByemail(email);
            userFromDb.setPassword(encoder.encode(userFromDb.getName()));
            userRepo.save(userFromDb);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    //TO HOST A NEW POLL
    public ObjectId hostPoll(Poll poll){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByemail(email);
        Poll hostedPoll = pollRepo.save(poll);
        user.getElections().add(hostedPoll.getId());
        userRepo.save(user);
        return hostedPoll.getId();
    }

    //TO VIEW CANDIDATURE APPLICATIONS
    public List<Applicant> viewCandidatureApplications(String id){
        ObjectId objectId = new ObjectId(id);
        List<Applicant> applicants = new ArrayList<>();
        for(ObjectId o: pollRepo.findByid(objectId).getApplicantsOfCandidature()){
            applicants.add(applicantRepo.findByid(o));
        }
        return applicants;
    }

    //TO VIEW VOTER APPLICATIONS
    public List<Applicant> viewVoterApplications(String id){
        ObjectId objectId = new ObjectId(id);
        List<Applicant> applicants = new ArrayList<>();
        for(ObjectId o: pollRepo.findByid(objectId).getApplicantsOfVoter()){
            applicants.add(applicantRepo.findByid(o));
        }
        return applicants;
    }

    //TO REJECT A CANDIDATURE APPLICATION
    public Boolean rejectCandidatureApplication(String idOfApplicant){
        try{
            ObjectId obIdOfApplicant = new ObjectId(idOfApplicant);
            Applicant applicant = applicantRepo.findByid(obIdOfApplicant);
            Poll poll = pollRepo.findByid(applicant.getAppliedPollId());
            poll.getApplicantsOfCandidature().remove(obIdOfApplicant);
            pollRepo.save(poll);
            applicantRepo.deleteById(obIdOfApplicant);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    //TO REJECT A VOTER APPLICATION
    public Boolean rejectVoterApplication(String idOfApplicant){
        try{
            ObjectId obIdOfApplicant = new ObjectId(idOfApplicant);
            Applicant applicant = applicantRepo.findByid(obIdOfApplicant);
            Poll poll = pollRepo.findByid(applicant.getAppliedPollId());
            poll.getApplicantsOfVoter().remove(obIdOfApplicant);
            pollRepo.save(poll);
            applicantRepo.deleteById(obIdOfApplicant);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    //TO ACCEPT A CANDIDATURE APPLICATION
    public ResponseEntity<?> acceptCandidatureApplication(String idOfApplicant){

        ObjectId obIdOfApplicant = new ObjectId(idOfApplicant);
        Applicant applicant = applicantRepo.findByid(obIdOfApplicant);
        Candidate candidate = candidateRepo.findByemail(applicant.getEmail());
        Poll poll = pollRepo.findByid(applicant.getAppliedPollId());
        if(candidate != null){
            PollCandidate pollCandidate = criteriaRepo.findPollCandidateByEmailAndPollId(applicant.getAppliedPollId(),candidate.getId());
            if(pollCandidate != null){
                return ResponseEntity.ok(" The applicant already exits as candidate for selected poll " + pollCandidate);
            }
            else{
                PollCandidate pollCandidate1 = new PollCandidate();
                pollCandidate1.setCandidateId(candidate.getId());
                pollCandidate1.setPollId(applicant.getAppliedPollId());
                pollCandidate1.setPost(applicant.getPost_applied_for());
                pollCandidate1.setTitle_of_pole(poll.getTitle());
                pollCandidate1.setVotes(0);
                pollCandidateRepo.save(pollCandidate1);
                candidate.getAppliedPollId().add(applicant.getAppliedPollId());
                candidateRepo.save(candidate);
            }
        }
        else{
            Candidate candidate1 = (Candidate) candidateService.saveCandidate(applicant).getBody();
            PollCandidate pollCandidate = new PollCandidate();
            pollCandidate.setTitle_of_pole(poll.getTitle());
            pollCandidate.setCandidateId(candidate1.getId());
            pollCandidate.setPollId(poll.getId());
            pollCandidate.setPost(applicant.getPost_applied_for());
            pollCandidate.setVotes(0);
            pollCandidateRepo.save(pollCandidate);
        }
        applicantRepo.deleteById(obIdOfApplicant);
        poll.getApplicantsOfCandidature().remove(obIdOfApplicant);
        Map<String, List<ObjectId>> mp = poll.getCandidates();

        String post = applicant.getPost_applied_for();
        if(mp.containsKey(post)){
            mp.get(post).add(obIdOfApplicant);
            return ResponseEntity.ok(pollRepo.save(poll));
        }
        else{
            mp.put(post, new ArrayList<>(Arrays.asList(obIdOfApplicant)));
            return ResponseEntity.ok(pollRepo.save(poll));
        }
    }

    //TO ACCEPT A VOTER APPLICATION
    public ResponseEntity<?> acceptVoterApplication(String idOfApplicant){

        ObjectId obIdOfApplicant = new ObjectId(idOfApplicant);
        Applicant applicant = applicantRepo.findByid(obIdOfApplicant);

        if(applicant == null){
            return ResponseEntity.ok("No such record found");
        }
        applicantRepo.deleteById(obIdOfApplicant);
        Voter voter = voterRepo.findByemail(applicant.getEmail());
        if(voter != null){
            PollVoter pollVoter = criteriaRepo.findPollVoterByEmailAndPollId(applicant.getAppliedPollId(),voter.getId());
            if(pollVoter != null){
                pollVoter.getPosts().add(applicant.getPost_applied_for());
                pollVoter.getHas_voted().put(applicant.getPost_applied_for(), false);
                pollVoterRepo.save(pollVoter);
            }
            else {
                PollVoter pollVoter1 = new PollVoter();
                pollVoter1.setVoterId(voter.getId());
                pollVoter1.setPollId(applicant.getAppliedPollId());
                pollVoter1.getPosts().add(applicant.getPost_applied_for());
                pollVoter1.getHas_voted().put(applicant.getPost_applied_for(),false);
                pollVoterRepo.save(pollVoter1);
            }
            voter.getAppliedPollId().add(applicant.getAppliedPollId());
            voterRepo.save(voter);
        }
        else{
            Voter voter1 = (Voter) voterService.saveVoter(applicant).getBody();
            if(voter1 != null){
                PollVoter pollVoter1 = new PollVoter();
                pollVoter1.setVoterId(voter1.getId());
                pollVoter1.setPollId(applicant.getAppliedPollId());
                pollVoter1.getPosts().add(applicant.getPost_applied_for());
                pollVoter1.getHas_voted().put(applicant.getPost_applied_for(),false);
                pollVoterRepo.save(pollVoter1);
            }
            else{
                return ResponseEntity.ok("Some error occured while making a voter corresponding to the application");
            }
        }

        Poll poll = pollRepo.findByid(applicant.getAppliedPollId());
        poll.getApplicantsOfVoter().remove(obIdOfApplicant);
        Map<String, List<ObjectId>> mp = poll.getVoters();

        String post = applicant.getPost_applied_for();
        if(mp.containsKey(post)){
            mp.get(post).add(obIdOfApplicant);
            return ResponseEntity.ok(pollRepo.save(poll));
        }
        else{
            mp.put(post, new ArrayList<>(Arrays.asList(obIdOfApplicant)));
            return ResponseEntity.ok(pollRepo.save(poll));
        }

    }

}