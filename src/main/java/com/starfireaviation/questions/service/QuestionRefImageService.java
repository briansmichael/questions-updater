package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.QuestionRefImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

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
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final QuestionRefImage questionRefImage = new QuestionRefImage();
                questionRefImage.setId(rs.getLong(1));
                questionRefImage.setQuestionId(rs.getLong(2));
                questionRefImage.setImageId(rs.getLong(3));
                questionRefImage.setAnnotation(rs.getString(3));
                store(questionRefImage);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting QUESTION_REF_IMAGES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final QuestionRefImage questionRefImage) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/questionrefimage")
                .body(Mono.just(questionRefImage), QuestionRefImage.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}
