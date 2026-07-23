package com.project.uhdbackend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

public class PredicateBuilder {

	private static final Map<String, String> FIELD_MAP = new HashMap<>();
	private static final Map<String, String> OPERATOR_MAP = new HashMap<>();

	static {
		// 前端 field 對應實體欄位名稱
		FIELD_MAP.put("subject", "subject");
		FIELD_MAP.put("content", "data");
		FIELD_MAP.put("time", "alertTimestamp");

		// 前端 operator 對應實際比較符號
		OPERATOR_MAP.put("equals", "=");
		OPERATOR_MAP.put("greater", ">");
		OPERATOR_MAP.put("less", "<");
		OPERATOR_MAP.put("greater_or_equal", ">=");
		OPERATOR_MAP.put("less_or_equal", "<=");
		OPERATOR_MAP.put("contains", "LIKE");
		OPERATOR_MAP.put("is_empty", "IS_EMPTY");
	}

//	private static String normalizeField(String field) {
//		return FIELD_MAP.getOrDefault(field.toLowerCase(), field);
//	}
//
//	private static String normalizeOperator(String operator) {
//		return OPERATOR_MAP.getOrDefault(operator.toLowerCase(), operator);
//	}

//    public static Predicate<Message> build(String logic, Map<Long, Condition> conditionMap) {
//        List<String> postfix = toPostfix(logic);
//        Stack<Predicate<Message>> stack = new Stack<>();
//
//        for (String token : postfix) {
//            switch (token.toUpperCase()) {
//                case "AND": {
//                    Predicate<Message> right = stack.pop();
//                    Predicate<Message> left = stack.pop();
//                    stack.push(left.and(right));
//                    break;
//                }
//                case "OR": {
//                    Predicate<Message> right = stack.pop();
//                    Predicate<Message> left = stack.pop();
//                    stack.push(left.or(right));
//                    break;
//                }
//                default: {
//                    Long id = Long.parseLong(token);
//                    Condition condition = conditionMap.get(id);
//                    stack.push(buildPredicate(condition));
//                    break;
//                }
//            }
//        }
//
//        return stack.pop();
//    }
//
//    private static Predicate<Message> buildPredicate(Condition condition) {
//        return message -> {
//            try {
//                String fieldName = normalizeField(condition.getField());
//                Field field = Message.class.getDeclaredField(fieldName);
//                field.setAccessible(true);
//                Object fieldValue = field.get(message);
//
//                String operator = normalizeOperator(condition.getOperator());
//                String value = condition.getValue();
//
//                if (fieldValue == null)
//                    return false;
//                String fieldStr = fieldValue.toString();
//
//                if ("alertTimestamp".equals(fieldName)) {
//                    ZonedDateTime conditionTime = ZonedDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
//                    ZonedDateTime messageTime = ZonedDateTime.parse(fieldStr, DateTimeFormatter.ISO_DATE_TIME);
//
//                    switch (operator) {
//                        case "=":
//                            return messageTime.isEqual(conditionTime);
//                        case ">":
//                            return messageTime.isAfter(conditionTime);
//                        case "<":
//                            return messageTime.isBefore(conditionTime);
//                        case "<=":
//                            return messageTime.isBefore(conditionTime)||messageTime.isEqual(conditionTime);
//                        case ">=":
//                            return messageTime.isAfter(conditionTime)||messageTime.isEqual(conditionTime);
//                        default:
//                            return false;
//                    }
//                }
//
//                // 處理其他字段
//                switch (operator) {
//                    case "=":
//                        return fieldStr.equals(value);
//                    case "LIKE":
//                        return fieldStr.contains(value);
//                    case ">":
//                        return Double.parseDouble(fieldStr) > Double.parseDouble(value);
//                    case "<":
//                        return Double.parseDouble(fieldStr) < Double.parseDouble(value);
//                    case "IS_EMPTY":
//                        return fieldStr.isEmpty();
//                    default:
//                        return false;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        };
//    }

	public static List<String> toPostfix(String logic) {
		List<String> output = new ArrayList<>();
		Stack<String> stack = new Stack<>();
		StringTokenizer tokenizer = new StringTokenizer(logic, " ()", true);

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken().trim();
			if (token.isEmpty())
				continue;

			if (token.matches("\\d+")) {
				output.add(token);
			} else if (token.equals("(")) {
				stack.push(token);
			} else if (token.equals(")")) {
				while (!stack.isEmpty() && !stack.peek().equals("(")) {
					output.add(stack.pop());
				}
				stack.pop(); // remove "("
			} else if (token.equalsIgnoreCase("AND") || token.equalsIgnoreCase("OR")) {
				while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
					output.add(stack.pop());
				}
				stack.push(token);
			}
		}

		while (!stack.isEmpty()) {
			output.add(stack.pop());
		}

		return output;
	}

	private static int precedence(String op) {
		switch (op.toUpperCase()) {
		case "AND":
			return 2;
		case "OR":
			return 1;
		default:
			return 0;
		}
	}
}
