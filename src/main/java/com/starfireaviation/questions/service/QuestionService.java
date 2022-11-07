package com.starfireaviation.questions.service;

import com.starfireaviation.questions.CommonConstants;
import com.starfireaviation.questions.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jsoup.Jsoup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class QuestionService extends BaseService {

    public QuestionService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    /**
     * Gets questions from remote database.
     */
    public void run() {
        log.info("Getting QUESTIONS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT QuestionID, QuestionText, ChapterID, SMCID, SourceID, LastMod, Explanation, "
                + "OldQID, LSCID FROM Questions";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Question question = new Question();
                question.setQuestionId(rs.getLong(1));
                question.setText(decrypt(rs.getString(2)));
                question.setChapterId(rs.getLong(CommonConstants.THREE));
                question.setSmcId(rs.getLong(CommonConstants.FOUR));
                question.setSource(rs.getString(CommonConstants.FIVE));
                question.setLastModified(rs.getDate(CommonConstants.SIX));
                question.setExplanation(Jsoup.parse(decrypt(rs.getString(CommonConstants.SEVEN))).text());
                question.setOldQuestionId(rs.getLong(CommonConstants.EIGHT));
                question.setLscId(rs.getLong(CommonConstants.NINE));
                if (existsQuestion(question.getQuestionId(), mysqlConn)) {
                    final String update = "UPDATE questions SET chapter_id = ?, explanation = ?, last_modified = ?, "
                            + "lsc_id = ?, old_question_id = ?, smc_id = ?, source = ?, text = ? WHERE question_id = ?";
                    updateCount += store(question, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO questions (chapter_id, explanation, last_modified, "
                            + "lsc_id, old_question_id, smc_id, source, text, question_id) VALUES (?,?,?,?,?,?,?,?,?)";
                    insertCount += store(question, insert, mysqlConn);
                }
            }
        } catch (SQLException | InvalidCipherTextException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting QUESTIONS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final Question question, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setLong(1, question.getChapterId());
            ps.setString(2, question.getExplanation().replaceAll("\\P{Print}", ""));
            ps.setTimestamp(3, new java.sql.Timestamp(question.getLastModified().getTime()));
            ps.setLong(4, question.getLscId());
            ps.setLong(5, question.getOldQuestionId());
            ps.setLong(6, question.getSmcId());
            ps.setString(7, question.getSource());
            ps.setString(8, question.getText().replaceAll("\\P{Print}", ""));
            ps.setLong(9, question.getQuestionId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsQuestion(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM questions WHERE question_id = ?";
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
