package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.QuestionACS;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class QuestionACSService extends BaseService {

    /**
     * Question ACS.
     */
    public QuestionACSService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting QUESTIONS_ACS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, QuestionID, ACSID FROM QuestionsACS";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final QuestionACS questionACS = new QuestionACS();
                questionACS.setId(rs.getLong(1));
                questionACS.setQuestionId(rs.getLong(2));
                questionACS.setAcsId(rs.getLong(3));
                if (existsQuestionACS(questionACS.getId(), mysqlConn)) {
                    final String update = "UPDATE questions_acs SET acs_id = ?, question_id = ? WHERE id = ?";
                    updateCount += store(questionACS, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO questions_acs (acs_id, question_id, id) VALUES (?,?,?)";
                    insertCount += store(questionACS, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting QUESTIONS_ACS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final QuestionACS questionACS, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setLong(1, questionACS.getAcsId());
            ps.setLong(2, questionACS.getQuestionId());
            ps.setLong(3, questionACS.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsQuestionACS(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM questions_acs WHERE id = ?";
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
