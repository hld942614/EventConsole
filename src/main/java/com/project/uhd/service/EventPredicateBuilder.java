package com.project.uhd.service;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.function.Predicate;

import com.project.uhd.dto.Condition;
import com.project.uhd.entity.Event;

/**
 * 對應舊有 PredicateBuilder，但改為對 Event 欄位做 reflection 比對。 與 PredicateBuilder
 * 邏輯完全對稱，差異只在： - FIELD_MAP 目標欄位改成 Event 的 title / messageContent / occurredAt -
 * occurredAt 本身已經是 OffsetDateTime，不用像 Message.alertTimestamp 那樣先 parse 字串
 */
public class EventPredicateBuilder {

	private static final Map<String, String> FIELD_MAP = new HashMap<>();
	private static final Map<String, String> OPERATOR_MAP = new HashMap<>();

	static {
		// 前端 field 對應 Event 實體欄位名稱
		FIELD_MAP.put("subject", "title");
		FIELD_MAP.put("content", "messageContent");
		FIELD_MAP.put("time", "occurredAt");

		OPERATOR_MAP.put("equals", "=");
		OPERATOR_MAP.put("greater", ">");
		OPERATOR_MAP.put("less", "<");
		OPERATOR_MAP.put("greater_or_equal", ">=");
		OPERATOR_MAP.put("less_or_equal", "<=");
		OPERATOR_MAP.put("contains", "LIKE");
		OPERATOR_MAP.put("is_empty", "IS_EMPTY");
	}

	private static String normalizeField(String field) {
		return FIELD_MAP.getOrDefault(field.toLowerCase(), field);
	}

	private static String normalizeOperator(String operator) {
		return OPERATOR_MAP.getOrDefault(operator.toLowerCase(), operator);
	}

	public static Predicate<Event> build(String logic, Map<Long, Condition> conditionMap) {
		List<String> postfix = toPostfix(logic);
		Stack<Predicate<Event>> stack = new Stack<>();

		for (String token : postfix) {
			switch (token.toUpperCase()) {
			case "AND": {
				Predicate<Event> right = stack.pop();
				Predicate<Event> left = stack.pop();
				stack.push(left.and(right));
				break;
			}
			case "OR": {
				Predicate<Event> right = stack.pop();
				Predicate<Event> left = stack.pop();
				stack.push(left.or(right));
				break;
			}
			default: {
				Long id = Long.parseLong(token);
				Condition condition = conditionMap.get(id);
				stack.push(buildPredicate(condition));
				break;
			}
			}
		}

		return stack.pop();
	}

	private static Predicate<Event> buildPredicate(Condition condition) {
		return event -> {
			try {
				String fieldName = normalizeField(condition.getField());
				Field field = Event.class.getDeclaredField(fieldName);
				field.setAccessible(true);
				Object fieldValue = field.get(event);

				String operator = normalizeOperator(condition.getOperator());
				String value = condition.getValue();

				if (fieldValue == null)
					return false;

				if ("occurredAt".equals(fieldName)) {
					OffsetDateTime eventTime = (OffsetDateTime) fieldValue;
					ZonedDateTime conditionTime = ZonedDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
					ZonedDateTime eventZoned = eventTime.toZonedDateTime();

					switch (operator) {
					case "=":
						return eventZoned.isEqual(conditionTime);
					case ">":
						return eventZoned.isAfter(conditionTime);
					case "<":
						return eventZoned.isBefore(conditionTime);
					case "<=":
						return eventZoned.isBefore(conditionTime) || eventZoned.isEqual(conditionTime);
					case ">=":
						return eventZoned.isAfter(conditionTime) || eventZoned.isEqual(conditionTime);
					default:
						return false;
					}
				}

				String fieldStr = fieldValue.toString();
				switch (operator) {
				case "=":
					return fieldStr.equals(value);
				case "LIKE":
					return fieldStr.contains(value);
				case ">":
					return Double.parseDouble(fieldStr) > Double.parseDouble(value);
				case "<":
					return Double.parseDouble(fieldStr) < Double.parseDouble(value);
				case "IS_EMPTY":
					return fieldStr.isEmpty();
				default:
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		};
	}

	// toPostfix / precedence 跟 PredicateBuilder 完全一樣，直接複製過來

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
				stack.pop();
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