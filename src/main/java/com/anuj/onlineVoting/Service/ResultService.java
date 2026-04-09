package com.anuj.onlineVoting.Service;

import com.anuj.onlineVoting.Entities.Result;
import com.anuj.onlineVoting.Repository.ResultRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultService {

    @Autowired
    ResultRepository resultRepo;

    public Result findResult(String resultId){
        return resultRepo.findByid(new ObjectId(resultId));
    }

    public Result save(Result result){
        return resultRepo.save(result);
    }
}
