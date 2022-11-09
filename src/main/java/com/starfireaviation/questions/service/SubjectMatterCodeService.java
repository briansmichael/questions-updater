package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.SubjectMatterCode;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class SubjectMatterCodeService extends BaseService {

    /**
     * Subject Matter Codes.
     */
    public SubjectMatterCodeService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting SUBJECT_MATTER_CODES table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, Code, SourceID, Description, LastMod, IsLSC FROM SubjectMatterCodes";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final SubjectMatterCode subjectMatterCode = new SubjectMatterCode();
                subjectMatterCode.setId(rs.getLong(1));
                subjectMatterCode.setCode(rs.getString(2));
                subjectMatterCode.setSourceId(rs.getLong(3));
                subjectMatterCode.setDescription(rs.getString(4));
                subjectMatterCode.setLastModified(rs.getDate(5));
                subjectMatterCode.setIsLSC(rs.getLong(6));
                if (existsSubjectMatterCode(subjectMatterCode.getId(), mysqlConn)) {
                    final String update = "UPDATE subject_matter_codes SET code = ?, source_id = ?, description = ?, "
                            + "last_modified = ?, is_lsc = ? WHERE id = ?";
                    updateCount += store(subjectMatterCode, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO subject_matter_codes (code, source_id, description, "
                            + "last_modified, is_lsc, id) VALUES (?,?,?,?,?,?)";
                    insertCount += store(subjectMatterCode, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting SUBJECT_MATTER_CODES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final SubjectMatterCode subjectMatterCode, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, subjectMatterCode.getCode());
            ps.setLong(2, subjectMatterCode.getSourceId());
            ps.setString(3, subjectMatterCode.getDescription());
            ps.setTimestamp(4, new java.sql.Timestamp(subjectMatterCode.getLastModified().getTime()));
            ps.setLong(5, subjectMatterCode.getIsLSC());
            ps.setLong(6, subjectMatterCode.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsSubjectMatterCode(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM subject_matter_codes WHERE id = ?";
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

