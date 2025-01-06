package com.opportuna.jobportal.controller;

import com.opportuna.jobportal.entity.Users;
import com.opportuna.jobportal.entity.UsersType;
import com.opportuna.jobportal.services.UsersService;
import com.opportuna.jobportal.services.UsersTypeService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UsersController {
    private final UsersTypeService usersTypeService;
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersTypeService usersTypeService, UsersService usersService) {
        this.usersTypeService = usersTypeService;
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model){
       List<UsersType> userTypes = usersTypeService.getAll();
       model.addAttribute("getAllTypes" , userTypes);
       model.addAttribute("user", new Users());
       return "register";
    }

    @PostMapping("/register/new")
    public String userRegisteration(@Valid Users users){
        double startTime = System.currentTimeMillis();
        System.out.println("User: "+users);
        usersService.addNew(users);
        double endTime = System.currentTimeMillis();
        System.out.println("Time taken: "+(endTime-startTime) + "ms");
        return "dashboard";
    }
}
