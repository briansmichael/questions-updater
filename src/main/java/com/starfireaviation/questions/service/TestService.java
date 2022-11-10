package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.Test;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class TestService extends BaseService {

    /**
     * Tests.
     */
    public TestService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting TESTS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT TestID, TestName, TestAbbr, GroupID, SortBy, LastMod FROM Tests";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Test test = new Test();
                test.setTestId(rs.getLong(1));
                test.setTestName(rs.getString(2));
                test.setTestAbbr(rs.getString(3));
                test.setGroupId(rs.getLong(4));
                test.setSortBy(rs.getLong(5));
                test.setLastModified(rs.getDate(6));
                if (existsTest(test.getTestId(), mysqlConn)) {
                    final String update = "UPDATE test SET test_name = ?, test_abbr = ?, group_id = ?, sort_by = ?, "
                            + "last_modified = ? WHERE test_id = ?";
                    updateCount += store(test, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO library (test_name, test_abbr, group_id, sort_by, "
                            + "last_modified, test_id) VALUES (?,?,?,?,?,?)";
                    insertCount += store(test, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting TESTS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final Test test, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, test.getTestName());
            ps.setString(2, test.getTestAbbr());
            ps.setLong(3, test.getGroupId());
            ps.setLong(4, test.getSortBy());
            ps.setTimestamp(5, new java.sql.Timestamp(test.getLastModified().getTime()));
            ps.setLong(6, test.getTestId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsTest(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM test WHERE test_id = ?";
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

