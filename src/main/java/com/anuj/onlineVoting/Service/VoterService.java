package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.*;
import com.anuj.onlineVoting.Repository.VoterRepository;
import com.anuj.onlineVoting.Utils.JwtUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class VoterService {

    @Autowired
    VoterRepository voterRepo;

    @Autowired
    PollVoterService pollVoterService;

    @Autowired
    PollCandidateService pollCandidateService;

    @Autowired
    PollService pollService;

    @Autowired
    JwtUtil jwtUtil;

    public Voter findByemail(String email){
        return voterRepo.findByemail(email);
    }

    public Voter saveUpdate(Voter voter){
        return voterRepo.save(voter);
    }

    public ResponseEntity<?> saveNew(Applicant applicant){
        try{
            Voter voter = new Voter();
            BeanUtils.copyProperties(applicant,voter);
            voter.getAppliedPollId().add(applicant.getAppliedPollId());
            return ResponseEntity.ok(voterRepo.save(voter));
        }
        catch(Exception e){
            return ResponseEntity.ok("Some error occured");
        }
    }

    public ResponseEntity<?> generateBallot(String pollId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ObjectId obPollId = new ObjectId(pollId);
        Voter voter = findByemail(username);
        Poll poll = pollService.findPoll(pollId);
        if(voter.getAppliedPollId().contains(obPollId)){
            if(poll.getStartDateTime().isAfter(LocalDateTime.now())){
                return ResponseEntity.ok("Polling is not started yet.");
            }
            if(poll.getEndDateTime().isBefore(LocalDateTime.now())){
                return ResponseEntity.ok("Polling has ended for the poll.");
            }

            PollVoter pollVoter = pollVoterService.findPollVoter(obPollId,voter.getId());
            if(pollVoter.getBallotGenerated()){
                if(pollVoter.getBallotExpirationTime().after(new Date(System.currentTimeMillis()))){
                    return ResponseEntity.ok("Ballot is already generated and not yet expired");
                }
            }
            Map<String, List<ObjectId>> mp = new HashMap<>();
            for(String post : pollVoter.getPosts()){
                if(!pollVoter.getHas_voted().get(post)){
                    mp.put(post, poll.getCandidates().get(post));
                }
            }
//            mp.put("poll", obPollId);

            Ballot ballot = new Ballot();
            ballot.setCandidates(mp);
            Map<String, Object> converted = new HashMap<>();

            for (Map.Entry<String, List<ObjectId>> entry : mp.entrySet()) {
                converted.put(entry.getKey(), entry.getValue());
            }

            ballot.setBallotId(jwtUtil.createToken(converted, voter.getEmail(), obPollId));
            pollVoter.setBallotGenerated(true);
            pollVoter.setBallotExpirationTime(jwtUtil.getExpirationTime(ballot.getBallotId()));
            return ResponseEntity.ok(ballot);
        }
        return ResponseEntity.ok("You are not a voter in this poll");
    }

    public ResponseEntity<?> castVote(Vote vote){

        if(!jwtUtil.isTokenValid(vote.getVoteId())){
            return ResponseEntity.ok(false);
        }
        Set<String> eligiblePosts = jwtUtil.getPosts(vote.getVoteId());
        Set<String> receivedPosts = vote.getVotes().keySet();

        if(!eligiblePosts.containsAll(receivedPosts)){
            return ResponseEntity.ok("Invalid alteration found.");
        }

        ObjectId pollId = jwtUtil.getPollId(vote.getVoteId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Voter voter = findByemail(email);
        PollVoter pollVoter =  (PollVoter) pollCandidateService.castVote(pollId, voter.getId(), vote.getVotes()).getBody();
        pollVoterService.save(pollVoter);
        return ResponseEntity.ok(true);
    }

}