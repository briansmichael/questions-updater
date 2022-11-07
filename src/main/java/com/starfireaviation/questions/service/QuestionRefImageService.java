package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.QuestionRefImage;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class QuestionRefImageService extends BaseService {

    /**
     * Question Ref Images.
     */
    public QuestionRefImageService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting QUESTION_REF_IMAGES table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, QuestionID, ImageID, Annotation FROM QuestionsRefImages";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final QuestionRefImage questionRefImage = new QuestionRefImage();
                questionRefImage.setId(rs.getLong(1));
                questionRefImage.setQuestionId(rs.getLong(2));
                questionRefImage.setImageId(rs.getLong(3));
                questionRefImage.setAnnotation(rs.getString(3));
                if (existsQuestionRefImage(questionRefImage.getId(), mysqlConn)) {
                    final String update = "UPDATE questions_ref_images SET annotation = ?, image_id = ?, "
                            + "question_id = ? WHERE id = ?";
                    updateCount += store(questionRefImage, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO questions_ref_images (annotation, image_id, question_id, id) "
                            + "VALUES (?,?,?,?)";
                    insertCount += store(questionRefImage, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting QUESTION_REF_IMAGES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final QuestionRefImage questionRefImage, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, questionRefImage.getAnnotation());
            ps.setLong(2, questionRefImage.getImageId());
            ps.setLong(3, questionRefImage.getQuestionId());
            ps.setLong(4, questionRefImage.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsQuestionRefImage(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM questions_ref_images WHERE id = ?";
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
