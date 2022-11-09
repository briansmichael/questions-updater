package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.Source;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class SourceService extends BaseService {

    /**
     * Sources.
     */
    public SourceService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting SOURCES table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, Author, Title, Abbreviation, LastMod FROM Sources";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Source source = new Source();
                source.setId(rs.getLong(1));
                source.setAuthor(rs.getString(2));
                source.setTitle(rs.getString(3));
                source.setAbbreviation(rs.getString(4));
                source.setLastModified(rs.getDate(5));
                if (existsSource(source.getId(), mysqlConn)) {
                    final String update = "UPDATE sources SET author, title, abbreviation, last_modified = ? "
                            + "WHERE id = ?";
                    updateCount += store(source, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO sources (author, title, abbreviation, last_modified, id) "
                            + "VALUES (?,?,?,?,?)";
                    insertCount += store(source, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting SOURCES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final Source source, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, source.getAuthor());
            ps.setString(2, source.getTitle());
            ps.setString(3, source.getAbbreviation());
            ps.setTimestamp(4, new java.sql.Timestamp(source.getLastModified().getTime()));
            ps.setLong(5, source.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsSource(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM sources WHERE id = ?";
        ResultSet rs = null;
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return Boolean.TRUE;
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        } finally {
            try { rs.close(); } catch (Exception e) {}
        }
        return Boolean.FALSE;
    }

}

