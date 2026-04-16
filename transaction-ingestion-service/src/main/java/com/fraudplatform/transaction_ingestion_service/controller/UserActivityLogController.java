package com.fraudplatform.transaction_ingestion_service.controller;

import com.fraudplatform.transaction_ingestion_service.model.UserActivityLog;
import com.fraudplatform.transaction_ingestion_service.service.UserActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class UserActivityLogController {

    private final UserActivityLogService activityLogService;

    // GET /api/activity/{userId}
    @GetMapping("/{userId}")
    public List<UserActivityLog> getByUser(@PathVariable String userId) {
        return activityLogService.getByUser(userId);
    }

    // GET /api/activity/{userId}/range?from=2026-01-01T00:00:00Z&to=2026-12-31T00:00:00Z
    @GetMapping("/{userId}/range")
    public List<UserActivityLog> getByUserInRange(
            @PathVariable String userId,
            @RequestParam Instant from,
            @RequestParam Instant to) {
        return activityLogService.getByUserInDateRange(userId, from, to);
    }

    // GET /api/activity/{userId}/flagged-or-high
    @GetMapping("/{userId}/flagged-or-high")
    public List<UserActivityLog> getFlaggedOrHigh(@PathVariable String userId) {
        return activityLogService.getFlaggedOrHighAmount(userId);
    }

    // GET /api/activity/stats/per-user
    @GetMapping("/stats/per-user")
    public List<Map> getActivityCountPerUser() {
        return activityLogService.getActivityCountPerUser();
    }

    // GET /api/activity/stats/flagged-with-users
    @GetMapping("/stats/flagged-with-users")
    public List<Map> getFlaggedWithUserDetails() {
        return activityLogService.getActivityWithUserDetails();
    }

    // GET /api/activity/{userId}/count
    @GetMapping("/{userId}/count")
    public long getRecentActivityCount(@PathVariable String userId) {
        return activityLogService.countRecentActivity(userId);
    }
}
