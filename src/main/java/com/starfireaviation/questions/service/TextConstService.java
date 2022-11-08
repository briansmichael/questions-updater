package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.TextConst;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class TextConstService extends BaseService {

    /**
     * Text Const.
     */
    public TextConstService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting TEXT_CONST table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, ConstName, ConstValue, GroupID, TestID, LastMod FROM TextConst";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final TextConst textConst = new TextConst();
                textConst.setId(rs.getLong(1));
                textConst.setConstName(rs.getString(2));
                textConst.setConstValue(rs.getString(3));
                textConst.setGroupId(rs.getLong(4));
                textConst.setTestId(rs.getLong(5));
                textConst.setLastModified(rs.getDate(6));
                if (existsTextConst(textConst.getId(), mysqlConn)) {
                    final String update = "UPDATE text_const SET const_name = ?, const_value = ?, group_id = ?, "
                            + "test_id = ?, last_modified = ? WHERE id = ?";
                    updateCount += store(textConst, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO text_const (const_name, const_value, group_id, test_id, "
                            + "last_modified, id) VALUES (?,?,?,?,?,?)";
                    insertCount += store(textConst, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting TEXT_CONST table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final TextConst textConst, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, textConst.getConstName());
            ps.setString(2, textConst.getConstValue());
            ps.setLong(3, textConst.getGroupId());
            ps.setLong(4, textConst.getTestId());
            ps.setTimestamp(5, new java.sql.Timestamp(textConst.getLastModified().getTime()));
            ps.setLong(6, textConst.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsTextConst(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM text_const WHERE id = ?";
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

