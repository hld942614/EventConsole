//package com.project.uhdbackend.service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Predicate;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.project.uhdbackend.dto.Condition;
//import com.project.uhdbackend.entity.Group;
//import com.project.uhdbackend.entity.Message;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Service
//public class MessageClassificationService {
//	
//	@Autowired
//	private GroupService groupService;
//
//	public boolean classifyAndSave(List<Group> groups, Message message) {
//		boolean flag = false;
//		for (Group group : groups) {
//			ObjectMapper mapper = new ObjectMapper();
//			String conditionsJson = group.getConditions();
//			if (conditionsJson == null) {
//				continue;
//			}
//			try {
//				String logic = group.getLogic();
//				List<Condition> conditionList = mapper.readValue(conditionsJson, new TypeReference<List<Condition>>() {
//				});
//				Map<Long, Condition> conditionMap = new HashMap<>();
//				for (Condition condition : conditionList) {
//					conditionMap.put(condition.getId(), condition);
//				}
//				Predicate<Message> predicate = PredicateBuilder.build(logic, conditionMap);
//				if (predicate.test(message)) {
//					groupService.addMessageToGroup(group.getGroupId(), message.getMessageId());
//					flag = true;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return flag;
//	}
//}
