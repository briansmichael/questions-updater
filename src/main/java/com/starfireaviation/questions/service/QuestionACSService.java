package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.QuestionACS;
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
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final QuestionACS questionACS = new QuestionACS();
                questionACS.setId(rs.getLong(1));
                questionACS.setQuestionId(rs.getLong(2));
                questionACS.setAcsId(rs.getLong(3));
                store(questionACS);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting QUESTIONS_ACS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final QuestionACS questionACS) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/questionacs")
                .body(Mono.just(questionACS), QuestionACS.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}
