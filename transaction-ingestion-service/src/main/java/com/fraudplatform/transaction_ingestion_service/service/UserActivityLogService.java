package com.fraudplatform.transaction_ingestion_service.service;

import com.fraudplatform.transaction_ingestion_service.model.UserActivityLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserActivityLogService {

    private final MongoTemplate mongoTemplate;

    // ---- WRITE ----

    public void log(String userId, String action, Map<String, Object> metadata) {
        UserActivityLog log = UserActivityLog.builder()
                .userId(userId)
                .action(action)
                .metadata(metadata)
                .timestamp(Instant.now())
                .flagged(false)
                .build();
        mongoTemplate.save(log);
    }

    // ---- SIMPLE CRITERIA QUERY ----

    // find all logs for a user, sorted by timestamp descending
    public List<UserActivityLog> getByUser(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId))
                .with(Sort.by(Sort.Direction.DESC, "timestamp"));
        return mongoTemplate.find(query, UserActivityLog.class);
    }

    // ---- DATE RANGE FILTER ----

    public List<UserActivityLog> getByUserInDateRange(String userId, Instant from, Instant to) {
        Query query = new Query(
                Criteria.where("userId").is(userId)
                        .and("timestamp").gte(from).lte(to)
        ).with(Sort.by(Sort.Direction.DESC, "timestamp"));
        return mongoTemplate.find(query, UserActivityLog.class);
    }

    // ---- COMPOUND CRITERIA (AND + OR) ----

    // find flagged logs OR high amount transactions for a user
    public List<UserActivityLog> getFlaggedOrHighAmount(String userId) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                new Criteria().orOperator(
                        Criteria.where("flagged").is(true),
                        Criteria.where("metadata.amount").gte(10000)
                )
        );
        Query query = new Query(criteria)
                .with(Sort.by(Sort.Direction.DESC, "timestamp"));
        return mongoTemplate.find(query, UserActivityLog.class);
    }

    // ---- VELOCITY CHECK — count logs in last 24 hours ----

    public long countRecentActivity(String userId) {
        Instant twentyFourHoursAgo = Instant.now().minus(24, ChronoUnit.HOURS);
        Query query = new Query(
                Criteria.where("userId").is(userId)
                        .and("timestamp").gte(twentyFourHoursAgo)
        );
        return mongoTemplate.count(query, UserActivityLog.class);
    }

    // ---- AGGREGATION — activity count per user ----

    public List<Map> getActivityCountPerUser() {
        GroupOperation groupByUser = Aggregation.group("userId")
                .count().as("activityCount")
                .last("timestamp").as("lastActivity");

        SortOperation sortByCount = Aggregation.sort(
                Sort.by(Sort.Direction.DESC, "activityCount")
        );

        Aggregation aggregation = Aggregation.newAggregation(groupByUser, sortByCount);

        return mongoTemplate.aggregate(aggregation, "user_activity_logs", Map.class)
                .getMappedResults();
    }

    // ---- LOOKUP — join activity logs with users collection ----

    public List<Map> getActivityWithUserDetails() {
        LookupOperation lookup = LookupOperation.newLookup()
                .from("users")              // join with users collection
                .localField("userId")       // field in user_activity_logs
                .foreignField("userId")     // field in users
                .as("userDetails");         // output array field name

        MatchOperation onlyFlagged = Aggregation.match(
                Criteria.where("flagged").is(true)
        );

        ProjectionOperation project = Aggregation.project()
                .and("userId").as("userId")
                .and("action").as("action")
                .and("timestamp").as("timestamp")
                .and("userDetails.email").as("email");

        Aggregation aggregation = Aggregation.newAggregation(
                onlyFlagged,
                lookup,
                project
        );

        return mongoTemplate.aggregate(aggregation, "user_activity_logs", Map.class)
                .getMappedResults();
    }

    // ---- FLAG high activity users ----

    public void flagHighActivityUser(String userId) {
        long count = countRecentActivity(userId);
        if (count >= 10) {
            Query query = new Query(Criteria.where("userId").is(userId));
            Update update = new Update().set("flagged", true);
            mongoTemplate.updateMulti(query, update, UserActivityLog.class);
            log.warn("Flagged all activity for high-activity user userId={} count={}", userId, count);
        }
    }
}
