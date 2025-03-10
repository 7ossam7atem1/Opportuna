package com.opportuna.jobportal.services;

import com.opportuna.jobportal.entity.JobSeekerProfile;
import com.opportuna.jobportal.entity.RecruiterProfile;
import com.opportuna.jobportal.entity.Users;
import com.opportuna.jobportal.repository.JobSeekerProfileRepository;
import com.opportuna.jobportal.repository.RecruiterProfileRepository;
import com.opportuna.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {
    private UsersRepository usersRepository;
    private JobSeekerProfileRepository jobSeekerProfileRepository;
    private RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, JobSeekerProfileRepository jobSeekerProfileRepository, RecruiterProfileRepository recruiterProfileRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users addNew(Users users) {
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users savedUser = usersRepository.save(users);
        int userTypeId = users.getUserTypeId().getUserTypeId();
        if (userTypeId == 1) {
            recruiterProfileRepository.save(new RecruiterProfile(savedUser));
        } else {
            jobSeekerProfileRepository.save(new JobSeekerProfile(savedUser));
        }
        return savedUser;
    }

    public Object getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        String userName = authentication.getName();
        Users users = (Users) usersRepository.findByEmail(userName).orElse(null);
        if (users == null) {
            return null;
        }

        int userId = users.getUserId();
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
            return recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
        } else {
            return jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
        }
    }

    public Optional<Object> getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
           String  username =  authentication.getName();
            return (Users) usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Couldn't find " + username + " user"));
        }
        return null;
    }
}