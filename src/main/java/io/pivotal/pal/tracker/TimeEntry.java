package io.pivotal.pal.tracker;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Just a POJO
 */
public class TimeEntry {

    private final long projectId;
    private final long userId;
    private final LocalDate date;
    private long id;
    private final long hours;

    public TimeEntry(long projectId, long userId, LocalDate date, int hours) {

        this.projectId = projectId;
        this.userId = userId;
        this.date = date;
        this.hours = hours;
        this.id = -1L;
    }

    public TimeEntry(long timeEntryId, long projectId, long userId, LocalDate date, long hours) {

        this.id = timeEntryId;
        this.hours = hours;
        this.projectId = projectId;
        this.userId = userId;
        this.date = date;
    }

    public TimeEntry() {
        this.hours = 0;
        this.projectId = 0;
        this.userId = 0;
        this.date = LocalDate.now();
        this.id = -1;
    }

    public long getId() {
       return id;
    }

    public void setId(long id)
    {
        this.id=id;
    }

    public long getProjectId() {
        return projectId;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getHours() {
        return hours;
    }

    /**
     * Determines equality based on properties rather than object pointer
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeEntry timeEntry = (TimeEntry) o;
        return projectId == timeEntry.projectId &&
                userId == timeEntry.userId &&
                id == timeEntry.id &&
                hours == timeEntry.hours &&
                Objects.equals(date, timeEntry.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, userId, date, id, hours);
    }
}