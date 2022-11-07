package com.starfireaviation.questions.service;

import com.starfireaviation.questions.CommonConstants;
import com.starfireaviation.questions.model.Answer;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jsoup.Jsoup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AnswerService extends BaseService {

    /**
     * Answer Choices List.
     */
    public static final String ANSWER_CHOICES = "A,B,C,D,E,F,G,H";

    public AnswerService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    /**
     * Gets answers from remote database.
     */
    public void run() {
        log.info("Getting ANSWERS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT AnswerID, AnswerText, QuestionID, IsCorrect, LastMod FROM Answers";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Answer answer = new Answer();
                answer.setAnswerId(rs.getLong(1));
                answer.setText(Jsoup.parse(decrypt(rs.getString(2))).text());
                answer.setQuestionId(rs.getLong(CommonConstants.THREE));
                answer.setCorrect(rs.getBoolean(CommonConstants.FOUR));
                answer.setLastModified(rs.getDate(CommonConstants.FIVE));
                answer.setChoice(deriveChoice(answer.getChoice(), answer.getQuestionId(), mysqlConn));
                if (existsAnswer(answer.getAnswerId(), mysqlConn)) {
                    final String update = "UPDATE answers SET choice = ?, last_modified = ?, question_id = ?, "
                            + "text = ?, correct = ? WHERE answer_id = ?";
                    updateCount += store(answer, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO answers (choice, last_modified, question_id, text, correct, "
                            + "answer_id) VALUES (?,?,?,?,?,?)";
                    insertCount += store(answer, insert, mysqlConn);
                }
            }
        } catch (SQLException | InvalidCipherTextException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting ANSWERS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final Answer answer, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, answer.getChoice());
            ps.setTimestamp(2, new java.sql.Timestamp(answer.getLastModified().getTime()));
            ps.setLong(3, answer.getQuestionId());
            ps.setString(4, answer.getText());
            ps.setBoolean(5, answer.getCorrect());
            ps.setLong(6, answer.getAnswerId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsAnswer(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM answers WHERE answer_id = ?";
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

    private Optional<List<Answer>> getAnswers(final Long id, final Connection mysqlConn) {
        final String query = "SELECT answer_id, choice, correct, last_modified, question_id, text "
                + "FROM answers WHERE question_id = ?";
        ResultSet rs = null;
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            List<Answer> answers = new ArrayList<>();
            while (rs.next()) {
                final Answer answer = new Answer();
                answer.setAnswerId(rs.getLong(1));
                answer.setChoice(rs.getString(2));
                answer.setCorrect(rs.getBoolean(3));
                answer.setLastModified(rs.getDate(4));
                answer.setQuestionId(rs.getLong(5));
                answer.setText(rs.getString(6));
                answers.add(answer);
            }
            return Optional.of(answers);
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        } finally {
            try { rs.close(); } catch (Exception e) {}
        }
        return Optional.empty();
    }

    /**
     * Derives choice for answer, if not already set.
     *
     * @param choice prior value
     * @param questionId question ID
     * @return derived choice value
     */
    private String deriveChoice(final String choice, final Long questionId, final Connection mysqlConn) {
        if (choice != null) {
            return choice;
        }
        final ArrayList<String> choices = new ArrayList<>(Arrays.asList(ANSWER_CHOICES.split(",")));
        getAnswers(questionId, mysqlConn)
                .ifPresent(answerEntities -> answerEntities
                        .stream()
                        .filter(answer -> answer.getChoice() != null)
                        .forEach(answer -> choices.remove(answer.getChoice())));
        return choices.get(0);
    }

}
