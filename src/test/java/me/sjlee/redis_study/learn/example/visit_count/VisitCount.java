package me.sjlee.redis_study.learn.example.visit_count;

public interface VisitCount {
    void addVisit(String eventId);
    int getVisitCount(String eventId);
    int getTotalVisitCount();
}
