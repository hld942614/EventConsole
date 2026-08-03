package com.project.uhd.service;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.project.uhd.entity.UserLoginLog;
import com.project.uhd.repository.UserLoginLogRepository;

@Service
public class UserLoginLogService {

    private final UserLoginLogRepository userLoginLogRepository;

    public UserLoginLogService(UserLoginLogRepository userLoginLogRepository) {
        this.userLoginLogRepository = userLoginLogRepository;
    }

    @Transactional
    public void record(String userId, JSONObject response) {
    	String actionName = response.optString("action","N");
    	String rtnMsg = response.optString("rtnMsg","");
    	String errorMsg = response.optString("errorMsg",""); 
        UserLoginLog log = new UserLoginLog();
        log.setUserId(userId);
        log.setActionName(actionName);
        log.setRtnMsg(rtnMsg);
        log.setErrorMsg(errorMsg);
        userLoginLogRepository.save(log);
    }
}
