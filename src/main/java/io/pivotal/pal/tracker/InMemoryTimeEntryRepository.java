package io.pivotal.pal.tracker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Simple in-memory repository, not much to learn here.
 */
public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private final HashMap<Long, TimeEntry> timeEntries = new HashMap<Long, TimeEntry>();

    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(this.list().size() + 1);
        timeEntries.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    public List<TimeEntry> list() {
        return Collections.list(Collections.enumeration(timeEntries.values()));
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry existing = this.find(id);
        if (existing == null) {
            return null;
        }
        timeEntry.setId(id);
        this.timeEntries.put(id, timeEntry);
        return this.find(id);
    }

    public TimeEntry find(long id) {

        return this.timeEntries.get(id);
    }

    public void delete(long id) {
        this.timeEntries.remove(id);
    }
}