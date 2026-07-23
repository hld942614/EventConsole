//package com.project.uhdbackend.entity;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//public class CaseMessageId implements Serializable {
//	
//	private static final long serialVersionUID = 1L;
//	
//    private Long caseId;
//    private Long messageId;
//
//    public CaseMessageId() {}
//
//    public CaseMessageId(Long caseId, Long messageId) {
//        this.caseId = caseId;
//        this.messageId = messageId;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof CaseMessageId)) return false;
//        CaseMessageId that = (CaseMessageId) o;
//        return Objects.equals(caseId, that.caseId) &&
//               Objects.equals(messageId, that.messageId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(caseId, messageId);
//    }
//}
