package ru.example.concurrentcollections;

import java.time.LocalTime;

public class Event {
    private final String id;
    private final String type;
    private final String payload;
    private final LocalTime timestamp;

    public Event(String id, String type, String payload) {
        this.id = id;
        this.type = type;
        this.payload = payload;
        this.timestamp = LocalTime.now();
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public String getPayload() { return payload; }

    @Override
    public String toString() {
        return String.format("[%s] Event{%s, type='%s', payload='%s'}", timestamp, id, type, payload);
    }
}