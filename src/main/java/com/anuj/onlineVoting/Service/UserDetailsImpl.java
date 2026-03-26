package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.Candidate;
import com.anuj.onlineVoting.Entities.User;
import com.anuj.onlineVoting.Entities.Voter;
import com.anuj.onlineVoting.Repository.CandidateRepository;
import com.anuj.onlineVoting.Repository.UserRepository;
import com.anuj.onlineVoting.Repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    CandidateRepository candidateRepo;

    @Autowired
    VoterRepository voterRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

        User user = userRepo.findByemail(email);
        if(user != null){
            try{
                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .roles(user.getRoles().toArray(new String[0]))
                        .password(user.getPassword())
                        .build();
                return userDetails;
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        Candidate candidate = candidateRepo.findByemail(email);
        if(candidate != null){
            try{
                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                        .username(candidate.getEmail())
                        .roles(candidate.getRole())
                        .password(candidate.getPassword())
                        .build();
                System.out.println("user built");
                return userDetails;
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        Voter voter = voterRepo.findByemail(email);
        if(voter != null){
            try{
                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                        .username(voter.getEmail())
                        .roles(voter.getRole())
                        .password(voter.getPassword())
                        .build();
                System.out.println("user built");
                return userDetails;
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        throw new UsernameNotFoundException("no user found with username " + email);
    }
}