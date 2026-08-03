package com.project.uhd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.uhd.entity.UserLoginLog;

@Repository
public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {

}
