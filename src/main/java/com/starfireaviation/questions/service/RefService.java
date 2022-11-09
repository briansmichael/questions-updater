package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.Ref;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class RefService extends BaseService {

    /**
     * Refs.
     */
    public RefService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting REFS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT RefID, RefText, LastMod FROM Refs";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Long remoteId = rs.getLong(1);
                final Ref ref = new Ref();
                ref.setRefId(remoteId);
                ref.setRefText(rs.getString(2));
                ref.setLastModified(rs.getDate(3));
                if (existsRef(ref.getRefId(), mysqlConn)) {
                    final String update = "UPDATE refs SET text = ?, last_modified = ? WHERE ref_id = ?";
                    updateCount += store(ref, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO refs (text, last_modified, ref_id) VALUES (?,?,?)";
                    insertCount += store(ref, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting REFS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final Ref ref, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, ref.getRefText());
            ps.setTimestamp(2, new java.sql.Timestamp(ref.getLastModified().getTime()));
            ps.setLong(3, ref.getRefId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsRef(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM refs WHERE ref_id = ?";
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

