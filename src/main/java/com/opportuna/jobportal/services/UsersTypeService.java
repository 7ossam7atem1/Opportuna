package com.opportuna.jobportal.services;

import com.opportuna.jobportal.entity.UsersType;
import com.opportuna.jobportal.repository.UsersTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class UsersTypeService {
    private final UsersTypeRepository usersTypeRepository;

    @Autowired
    public UsersTypeService(UsersTypeRepository usersTypeRepository) {
        this.usersTypeRepository = usersTypeRepository;
    }


    public List<UsersType> getAll(){
        return usersTypeRepository.findAll();
    }
}
