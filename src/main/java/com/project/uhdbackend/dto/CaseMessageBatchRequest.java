package com.project.uhdbackend.dto;

import java.util.List;

public class CaseMessageBatchRequest {
    private Long caseId;
    private List<Long> messageIds;

    public CaseMessageBatchRequest() {}

    public CaseMessageBatchRequest(Long caseId, List<Long> messageIds) {
        this.caseId = caseId;
        this.messageIds = messageIds;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public List<Long> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<Long> messageIds) {
        this.messageIds = messageIds;
    }
}
