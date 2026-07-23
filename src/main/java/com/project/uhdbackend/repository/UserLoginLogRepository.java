package com.project.uhdbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.uhdbackend.entity.UserLoginLog;

@Repository
public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {

}
