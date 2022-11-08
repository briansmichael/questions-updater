package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.QuestionTest;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class QuestionTestService extends BaseService {

    /**
     * Question Tests.
     */
    public QuestionTestService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting QUESTION_TESTS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, QuestionID, TestID, IsLinked, SortBy, LinkChapter, IsImportant "
                + "FROM QuestionsTests";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final QuestionTest questionTest = new QuestionTest();
                questionTest.setId(rs.getLong(1));
                questionTest.setQuestionId(rs.getLong(2));
                questionTest.setTestId(rs.getLong(3));
                questionTest.setIsLinked(rs.getLong(4));
                questionTest.setSortBy(rs.getLong(5));
                questionTest.setLinkChapter(rs.getLong(6));
                questionTest.setIsImportant(rs.getLong(7));
                if (existsQuestionTest(questionTest.getId(), mysqlConn)) {
                    final String update = "UPDATE question_test SET question_id = ?, test_id = ?, is_linked = ?, "
                            + "sort_by = ?, link_chapter = ?, is_important = ? WHERE id = ?";
                    updateCount += store(questionTest, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO question_test (question_id, test_id, is_linked, sort_by, "
                            + "link_chapter, is_important, id) VALUES (?,?,?,?,?,?,?)";
                    insertCount += store(questionTest, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting QUESTION_TESTS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final QuestionTest questionTest, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setLong(1, questionTest.getQuestionId());
            ps.setLong(2, questionTest.getTestId());
            ps.setLong(3, questionTest.getIsLinked());
            ps.setLong(4, questionTest.getSortBy());
            ps.setLong(5, questionTest.getLinkChapter());
            ps.setLong(6, questionTest.getIsImportant());
            ps.setLong(7, questionTest.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsQuestionTest(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM question_test WHERE id = ?";
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

