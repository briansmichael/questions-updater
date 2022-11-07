package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.Group;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class GroupService extends BaseService {

    /**
     * Groups.
     */
    public GroupService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting GROUPS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT GroupID, GroupName, GroupAbbr, LastMod FROM Groups";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Group group = new Group();
                group.setGroupId(rs.getLong(1));
                group.setGroupName(rs.getString(2));
                group.setGroupAbbr(rs.getString(3));
                group.setLastModified(rs.getDate(4));
                if (groupExists(group.getGroupId(), mysqlConn)) {
                    final String update = "UPDATE groups SET group_abbr = ?, group_name = ?, last_modified = ? "
                            + "WHERE group_id = ?";
                    updateCount += store(group, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO groups (group_abbr, group_name, last_modified, group_id) "
                            + "VALUES (?,?,?,?)";
                    insertCount += store(group, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting GROUPS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final Group group, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, group.getGroupAbbr());
            ps.setString(2, group.getGroupName());
            ps.setTimestamp(3, new java.sql.Timestamp(group.getLastModified().getTime()));
            ps.setLong(4, group.getGroupId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean groupExists(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM groups WHERE group_id = ?";
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
