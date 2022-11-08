package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.QuestionReference;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class QuestionReferenceService extends BaseService {

    /**
     * Question References.
     */
    public QuestionReferenceService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting QUESTION_REFERENCES table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, QuestionID, RefID FROM QuestionsReferences";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final QuestionReference questionReference = new QuestionReference();
                questionReference.setId(rs.getLong(1));
                questionReference.setQuestionId(rs.getLong(2));
                questionReference.setRefId(rs.getLong(3));
                if (existsQuestionReference(questionReference.getQuestionId(), mysqlConn)) {
                    final String update = "UPDATE question_reference SET question_id = ?, ref_id = ? WHERE id = ?";
                    updateCount += store(questionReference, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO question_reference (question_id, ref_id, id) VALUES (?,?,?)";
                    insertCount += store(questionReference, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting QUESTION_REFERENCES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final QuestionReference questionReference, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setLong(1, questionReference.getQuestionId());
            ps.setLong(2, questionReference.getRefId());
            ps.setLong(3, questionReference.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsQuestionReference(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM question_reference WHERE id = ?";
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

