package de.time_tracking.Time_tracking.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeEntry {
    private long id;
    private long userId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int breakMinutes;

    public TimeEntry() {
    }

    public TimeEntry(long userId, LocalDate date, LocalTime startTime, LocalTime endTime, int breakMinutes) {
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakMinutes = breakMinutes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getBreakMinutes() {
        return breakMinutes;
    }

    public void setBreakMinutes(int breakMinutes) {
        this.breakMinutes = breakMinutes;
    }
}
