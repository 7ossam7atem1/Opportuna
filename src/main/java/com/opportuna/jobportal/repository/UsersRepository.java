package com.opportuna.jobportal.repository;


import com.opportuna.jobportal.entity.Users;
import com.opportuna.jobportal.entity.UsersType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> {}
