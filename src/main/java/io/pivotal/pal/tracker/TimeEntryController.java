package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates REST-based CRUD ops with injectable dependencies
 */
@RestController
public class TimeEntryController {
    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryController(
            TimeEntryRepository timeEntryRepository)
            {
        this.timeEntryRepository = timeEntryRepository;
    }

    /**
     * Get object from body using @RequestBody
     */
    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) throws SQLException {
        TimeEntry created = this.timeEntryRepository.create(timeEntryToCreate);
        ResponseEntity<TimeEntry> result = new ResponseEntity<TimeEntry>(created, HttpStatus.CREATED);
        return result;
    }

    /**
     * Get scalar from url path using @PathVariable
     */
    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) throws SQLException {
        TimeEntry entry = this.timeEntryRepository.find(timeEntryId);
        if(entry != null){
            return new ResponseEntity(entry, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() throws SQLException {
        return new ResponseEntity<>(this.timeEntryRepository.list(), HttpStatus.OK);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable long timeEntryId, @RequestBody TimeEntry timeEntry) throws SQLException {
        TimeEntry update = this.timeEntryRepository.update(timeEntryId, timeEntry);
        if(update != null) {
            return new ResponseEntity<TimeEntry>(update, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) throws SQLException {
        this.timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}