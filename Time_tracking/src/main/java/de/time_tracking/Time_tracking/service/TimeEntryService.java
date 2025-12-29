package de.time_tracking.Time_tracking.service;

import de.time_tracking.Time_tracking.model.TimeEntry;
import de.time_tracking.Time_tracking.repository.TimeEntryRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TimeEntryService {

    private final TimeEntryRepository repo;

    public TimeEntryService(TimeEntryRepository repo) {
        this.repo = repo;
    }

    public long create(long userId, LocalDate date, LocalTime start, LocalTime end, int breakMinutes) {
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        if (breakMinutes < 0) {
            throw new IllegalArgumentException("Break minutes must be 0 or greater.");
        }

        long workMinutes = Duration.between(start, end).toMinutes();
        if (breakMinutes > workMinutes) {
            throw new IllegalArgumentException("Break minutes cannot be greater than working time.");
        }

        if (repo.hasOverlap(userId, date, start, end)) {
            throw new IllegalArgumentException("Time entry overlaps with an existing entry.");
        }

        TimeEntry entry = new TimeEntry(userId, date, start, end, breakMinutes);
        return repo.create(entry);
    }

    public long calculateNetMinutes(LocalTime start, LocalTime end, int breakMinutes) {
        return Duration.between(start, end).toMinutes() - breakMinutes;
    }

    public List<TimeEntry> getEntriesForDate(long userId, LocalDate date) {
    return repo.findByUserAndDate(userId, date);
}

}
