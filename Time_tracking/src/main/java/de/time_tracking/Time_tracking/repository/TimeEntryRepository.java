package de.time_tracking.Time_tracking.repository;

import de.time_tracking.Time_tracking.model.TimeEntry;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeEntryRepository {

    public long create(TimeEntry entry) {
        String sql = """
                INSERT INTO time_entries (user_id, date, start_time, end_time, break_minutes)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = Database.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, entry.getUserId());
            ps.setDate(2, Date.valueOf(entry.getDate()));
            ps.setTime(3, Time.valueOf(entry.getStartTime()));
            ps.setTime(4, Time.valueOf(entry.getEndTime()));
            ps.setInt(5, entry.getBreakMinutes());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getLong(1);
            }
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Time entry could not be created.", e);
        }
    }

    public List<TimeEntry> findByUserAndDate(long userId, LocalDate date) {
        String sql = """
                SELECT id, user_id, date, start_time, end_time, break_minutes
                FROM time_entries
                WHERE user_id = ? AND date = ?
                ORDER BY start_time
                """;

        List<TimeEntry> entries = new ArrayList<>();

        try (Connection con = Database.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TimeEntry e = new TimeEntry();
                    e.setId(rs.getLong("id"));
                    e.setUserId(rs.getLong("user_id"));
                    e.setDate(rs.getDate("date").toLocalDate());
                    e.setStartTime(rs.getTime("start_time").toLocalTime());
                    e.setEndTime(rs.getTime("end_time").toLocalTime());
                    e.setBreakMinutes(rs.getInt("break_minutes"));
                    entries.add(e);
                }
            }
            return entries;

        } catch (SQLException e) {
            throw new RuntimeException("Time entries could not be loaded.", e);
        }
    }

    public boolean hasOverlap(long userId, LocalDate date, LocalTime newStart, LocalTime newEnd) {
        String sql = """
                SELECT COUNT(*) AS cnt
                FROM time_entries
                WHERE user_id = ?
                  AND date = ?
                  AND (? < end_time AND ? > start_time)
                """;

        try (Connection con = Database.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setDate(2, Date.valueOf(date));
            ps.setTime(3, Time.valueOf(newStart));
            ps.setTime(4, Time.valueOf(newEnd));

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("cnt") > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Overlap check failed.", e);
        }
    }
}
