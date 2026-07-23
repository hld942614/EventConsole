package com.project.uhdbackend.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhdbackend.dto.ApiResponse;
import com.project.uhdbackend.dto.EventDTO;
import com.project.uhdbackend.dto.EventReadRequest;
import com.project.uhdbackend.dto.EventSearchRequest;
import com.project.uhdbackend.dto.EventStatusUpdateRequest;
import com.project.uhdbackend.entity.Event;
import com.project.uhdbackend.service.EventService;
import com.project.uhdbackend.service.EventStatusService;
import com.project.uhdbackend.service.KafkaProduceService;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

	private final String UHD_TOPIC = "UHDEvent";

	private KafkaProduceService kafkaProduceService;
	private EventService eventService;
	private final EventStatusService eventStatusService;

	public EventController(KafkaProduceService kafkaProduceService, EventService eventService,
			EventStatusService eventStatusService) {
		this.kafkaProduceService = kafkaProduceService;
		this.eventService = eventService;
		this.eventStatusService = eventStatusService;
	}

	@PostMapping("/send")
	public ResponseEntity<ApiResponse<Void>> collectEvent(@RequestBody JSONObject data) {
		kafkaProduceService.send(data.toString(), UHD_TOPIC);
		return ResponseEntity.ok(new ApiResponse<>(true, "Query success", null));
	}

	@GetMapping("/{eventId}")
	public ResponseEntity<ApiResponse<EventDTO>> getEventByEventId(@PathVariable String eventId) {
		return eventService.getEventDTO(eventId)
				.map(evt -> ResponseEntity.ok(new ApiResponse<>(true, "Event found", evt))).orElse(ResponseEntity
						.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Event not found", null)));
	}

	@PostMapping("/search")
	public ApiResponse<List<EventDTO>> searchEvents(@RequestBody EventSearchRequest request) {
		List<EventDTO> result = eventService.getEventsByFilters(request.getStatusArray(), request.getSubject(),
				request.getModuleCode(), request.getSender(), request.getContent(), request.getDay());
		return new ApiResponse<>(true, "Query success", result);
	}

	@PatchMapping("/status")
	public ResponseEntity<ApiResponse<Void>> changeEventStatus(@RequestBody EventStatusUpdateRequest request) {

		if (request.getEventId() == null || request.getEventId().isBlank()) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "eventId is required", null));
		}

		if (request.getStatus() == null) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "status is required", null));
		}

		eventService.changeEventStatus(request.getEventId(), request.getStatus());
		return ResponseEntity.ok(new ApiResponse<>(true, "Event status updated successfully", null));
	}

	@PatchMapping("/{eventId}/read")
	public ResponseEntity<ApiResponse<EventDTO>> markAsRead(@PathVariable String eventId,
			@RequestBody EventReadRequest request) {
		Event event = eventStatusService.markAsRead(eventId, request.getReadBy());
		return ResponseEntity.ok(new ApiResponse<>(true, "Event marked as read", new EventDTO(event)));
	}

	@PatchMapping("/{eventId}/resolve")
	public ResponseEntity<ApiResponse<EventDTO>> resolve(@PathVariable String eventId) {
		Event event = eventStatusService.resolve(eventId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Event resolved", new EventDTO(event)));
	}

	@PatchMapping("/{eventId}/close")
	public ResponseEntity<ApiResponse<EventDTO>> close(@PathVariable String eventId) {
		Event event = eventStatusService.close(eventId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Event closed", new EventDTO(event)));
	}
}
