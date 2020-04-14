package io.pivotal.pal.tracker;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Demonstrates simple CRUD operations using JDBC
 * */
public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private final DataSource dataSource;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final HashMap<Long, TimeEntry> timeEntries = new HashMap<Long, TimeEntry>();

    public TimeEntry create(TimeEntry timeEntry) throws SQLException {
        PreparedStatement statement = this.dataSource.getConnection().prepareStatement(
                "insert into time_entries " +
                        "(project_id, user_id, date, hours)" +
                        "values (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

        statement.setInt(1, (int) timeEntry.getProjectId());
        statement.setInt(2, (int) timeEntry.getUserId());
        statement.setDate(3, java.sql.Date.valueOf(timeEntry.getDate()));
        statement.setInt(4, (int) timeEntry.getHours());

        statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if(generatedKeys.next()) {
            return this.find(generatedKeys.getLong(1));
        }

        return null;
    }

    public List<TimeEntry> list() throws SQLException {
        PreparedStatement statement = this.dataSource.getConnection().prepareStatement(
                "select id, project_id, user_id, date, hours " +
                "from time_entries");

        ResultSet resultSet = statement.executeQuery();
        List<TimeEntry> result = new ArrayList<>();

        while (resultSet.next()){
            result.add(new TimeEntry(
                    resultSet.getLong("id"),
                    resultSet.getLong("project_id"),
                    resultSet.getLong("user_id"),
                    resultSet.getDate("date").toLocalDate(),
                    resultSet.getLong("hours")
            ));
        }

        return result;
    }

    public TimeEntry update(long id, TimeEntry timeEntry) throws SQLException {
        PreparedStatement statement = this.dataSource.getConnection().prepareStatement(
                "update time_entries " +
                "set project_id = ?, user_id = ?, date = ?, hours = ? " +
                "where id = ?");

        statement.setInt(1, (int) timeEntry.getProjectId());
        statement.setInt(2, (int) timeEntry.getUserId());
        statement.setDate(3, java.sql.Date.valueOf(timeEntry.getDate()));
        statement.setInt(4, (int) timeEntry.getHours());
        statement.setInt(5, (int) id);

        if(statement.executeUpdate() == 1) {
            return this.find(id);
        }

        return null;
    }

    public TimeEntry find(long id) throws SQLException {
        PreparedStatement statement = this.dataSource.getConnection().prepareStatement(
                "select project_id, user_id, date, hours " +
                "from time_entries " +
                "where id = ?");

        statement.setInt(1, (int) id);

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            return new TimeEntry(
                    id,
                    resultSet.getLong("project_id"),
                    resultSet.getLong("user_id"),
                    resultSet.getDate("date").toLocalDate(),
                    resultSet.getLong("hours")
            );
        }

        return null;
    }

    public void delete(long id) throws SQLException {
        PreparedStatement statement = this.dataSource.getConnection().prepareStatement(
                "delete from time_entries " +
                        "where id = ?");

        statement.setInt(1, (int) id);

        statement.executeUpdate();
    }
}