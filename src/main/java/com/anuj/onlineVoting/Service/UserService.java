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

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    PollService pollService;

    @Autowired
    ApplicantService applicantService;

    @Autowired
    VoterService voterService;

    @Autowired
    CandidateService candidateService;

    @Autowired
    PollCandidateService pollCandidateService;

    @Autowired
    CriteriaRepository criteriaRepo;

    @Autowired
    PollVoterService pollVoterService;

    @Autowired
    ResultService resultService;

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public User findByEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepo.findByemail(email);
    }

    //TO HOST A NEW POLL
    public ObjectId hostPoll(Poll poll){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByemail(email);
        Poll hostedPoll = pollService.savePoll(poll);
        user.getElections().add(hostedPoll.getId());
        userRepo.save(user);
        return hostedPoll.getId();
    }

    //TO VIEW CANDIDATURE APPLICATIONS
    public List<Applicant> viewCandidatureApplications(String pollId){
        List<Applicant> applicants = new ArrayList<>();
        for(ObjectId o: pollService.findPoll(pollId).getApplicantsOfCandidature()){
            applicants.add(applicantService.findById(o));
        }
        return applicants;
    }

    //TO VIEW VOTER APPLICATIONS
    public List<Applicant> viewVoterApplications(String id){
        List<Applicant> applicants = new ArrayList<>();
        for(ObjectId o: pollService.findPoll(id).getApplicantsOfVoter()){
            applicants.add(applicantService.findById(o));
        }
        return applicants;
    }

    //TO REJECT A CANDIDATURE APPLICATION
    public Boolean rejectCandidatureApplication(String idOfApplicant){
        try{
            ObjectId obIdOfApplicant = new ObjectId(idOfApplicant);
            Applicant applicant = applicantService.findById(obIdOfApplicant);
            Poll poll = pollService.findPoll(applicant.getAppliedPollId().toHexString());
            poll.getApplicantsOfCandidature().remove(obIdOfApplicant);
            pollService.savePoll(poll);
            applicantService.deleteById(obIdOfApplicant);
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
            Applicant applicant = applicantService.findById(obIdOfApplicant);
            Poll poll = pollService.findPoll(applicant.getAppliedPollId().toHexString());
            poll.getApplicantsOfVoter().remove(obIdOfApplicant);
            pollService.savePoll(poll);
            applicantService.deleteById(obIdOfApplicant);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    //TO ACCEPT A CANDIDATURE APPLICATION
    public ResponseEntity<?> acceptCandidatureApplication(String idOfApplicant){

        ObjectId obIdOfApplicant = new ObjectId(idOfApplicant);
        Applicant applicant = applicantService.findById(obIdOfApplicant);
        Candidate candidate = candidateService.findByemail(applicant.getEmail());
        Poll poll = pollService.findPoll(applicant.getAppliedPollId().toHexString());
        if(candidate != null){
            PollCandidate pollCandidate = criteriaRepo.findPollCandidateByCandidateIdAndPollId(applicant.getAppliedPollId(),candidate.getId());
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
                pollCandidateService.save(pollCandidate1);
                candidate.getAppliedPollId().add(applicant.getAppliedPollId());
                candidateService.saveUpdate(candidate);
            }
        }
        else{
            Candidate candidate1 = (Candidate) candidateService.saveNew(applicant).getBody();
            PollCandidate pollCandidate = new PollCandidate();
            pollCandidate.setTitle_of_pole(poll.getTitle());
            pollCandidate.setCandidateId(candidate1.getId());
            pollCandidate.setPollId(poll.getId());
            pollCandidate.setPost(applicant.getPost_applied_for());
            pollCandidate.setVotes(0);
            pollCandidateService.save(pollCandidate);
        }
        applicantService.deleteById(obIdOfApplicant);
        poll.getApplicantsOfCandidature().remove(obIdOfApplicant);
        Map<String, List<ObjectId>> mp = poll.getCandidates();

        String post = applicant.getPost_applied_for();
        if(mp.containsKey(post)){
            mp.get(post).add(obIdOfApplicant);
            return ResponseEntity.ok(pollService.savePoll(poll));
        }
        else{
            mp.put(post, new ArrayList<>(Arrays.asList(obIdOfApplicant)));
            return ResponseEntity.ok(pollService.savePoll(poll));
        }
    }

    //TO ACCEPT A VOTER APPLICATION
    public ResponseEntity<?> acceptVoterApplication(String idOfApplicant){

        ObjectId obIdOfApplicant = new ObjectId(idOfApplicant);
        Applicant applicant = applicantService.findById(obIdOfApplicant);

        if(applicant == null){
            return ResponseEntity.ok("No such record found");
        }
        applicantService.deleteById(obIdOfApplicant);
        Voter voter = voterService.findByemail(applicant.getEmail());
        if(voter != null){
            PollVoter pollVoter = criteriaRepo.findPollVoterByVoterIdAndPollId(applicant.getAppliedPollId(),voter.getId());
            if(pollVoter != null){
                pollVoter.getPosts().add(applicant.getPost_applied_for());
                pollVoter.getHas_voted().put(applicant.getPost_applied_for(), false);
                pollVoterService.save(pollVoter);
            }
            else {
                PollVoter pollVoter1 = new PollVoter();
                pollVoter1.setVoterId(voter.getId());
                pollVoter1.setPollId(applicant.getAppliedPollId());
                pollVoter1.getPosts().add(applicant.getPost_applied_for());
                pollVoter1.getHas_voted().put(applicant.getPost_applied_for(),false);
                pollVoterService.save(pollVoter1);
            }
            voter.getAppliedPollId().add(applicant.getAppliedPollId());
            voterService.saveUpdate(voter);
        }
        else{
            Voter voter1 = (Voter) voterService.saveNew(applicant).getBody();
            if(voter1 != null){
                PollVoter pollVoter1 = new PollVoter();
                pollVoter1.setVoterId(voter1.getId());
                pollVoter1.setPollId(applicant.getAppliedPollId());
                pollVoter1.getPosts().add(applicant.getPost_applied_for());
                pollVoter1.getHas_voted().put(applicant.getPost_applied_for(),false);
                pollVoterService.save(pollVoter1);
            }
            else{
                return ResponseEntity.ok("Some error occured while making a voter corresponding to the application");
            }
        }

        Poll poll = pollService.findPoll(applicant.getAppliedPollId());
        poll.getApplicantsOfVoter().remove(obIdOfApplicant);
        Map<String, List<ObjectId>> mp = poll.getVoters();

        String post = applicant.getPost_applied_for();
        if(mp.containsKey(post)){
            mp.get(post).add(obIdOfApplicant);
            return ResponseEntity.ok(pollService.savePoll(poll));
        }
        else{
            mp.put(post, new ArrayList<>(Arrays.asList(obIdOfApplicant)));
            return ResponseEntity.ok(pollService.savePoll(poll));
        }

    }

    public Result computeResult(String pollId){
        Authentication authentication = SecurityContextHolder.createEmptyContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepo.findByemail(username);

        if(!user.getElections().contains(new ObjectId(pollId))){
            return null;
        }

        Poll poll = pollService.findPoll(pollId);
        if(poll.getEndDateTime().isAfter(LocalDateTime.now())){
            return null;
        }
        if(poll.getResultAnnounced()){
            return  resultService.findResult(pollId);
        }

        List<PollCandidate> list = new ArrayList<>();
        list = pollCandidateService.getPollCandidatesForResult(pollId);
        Map<String, Map<String,Integer>> result = new HashMap<>();

        for(String post: poll.getPosts()){
            result.put(post,new HashMap<String, Integer>());
        }
        for(PollCandidate pc: list){
            result.get(pc.getPost()).put(pc.getCandidateId().toHexString(), pc.getVotes());
        }

        Result r = new Result();
        r.setResult(result);
        r.setPollId(new ObjectId(pollId));
        poll.setResultAnnounced(true);
        pollService.savePoll(poll);
        return resultService.save(r);
    }

}