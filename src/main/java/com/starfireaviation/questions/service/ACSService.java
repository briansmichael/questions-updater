package com.starfireaviation.questions.service;

import com.starfireaviation.questions.CommonConstants;
import com.starfireaviation.questions.model.ACS;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class ACSService extends BaseService {

    public ACSService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    /**
     * Gets ACS data from remote database.
     */
    public void run() {
        log.info("Getting ACS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, GroupID, ParentID, Code, Description, IsCompletedCode, LastMod FROM ACS";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final ACS acs = new ACS();
                acs.setId(rs.getLong(1));
                acs.setGroupId(rs.getLong(2));
                acs.setParentId(rs.getLong(CommonConstants.THREE));
                acs.setCode(rs.getString(CommonConstants.FOUR));
                acs.setDescription(rs.getString(CommonConstants.FIVE));
                acs.setIsCompletedCode(rs.getLong(CommonConstants.SIX));
                acs.setLastModified(rs.getDate(CommonConstants.SEVEN));
                if (acsExists(acs.getId(), mysqlConn)) {
                    final String update = "UPDATE acs SET code = ?, description = ?, group_id = ?, "
                            + "is_completed_code = ?, last_modified = ?, parent_id = ? WHERE id = ?";
                    updateCount += store(acs, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO acs (code, description, group_id, is_completed_code, "
                            + "last_modified, parent_id, id) VALUES (?,?,?,?,?,?,?)";
                    insertCount += store(acs, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting ACS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private boolean acsExists(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM acs WHERE id = ?";
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

    private long store(final ACS acs, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, acs.getCode());
            ps.setString(2, acs.getDescription());
            ps.setLong(3, acs.getGroupId());
            ps.setLong(4, acs.getIsCompletedCode());
            ps.setTimestamp(5, new java.sql.Timestamp(acs.getLastModified().getTime()));
            ps.setLong(6, acs.getParentId());
            ps.setLong(7, acs.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

}
