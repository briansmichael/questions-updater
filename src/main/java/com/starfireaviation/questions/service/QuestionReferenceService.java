package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.QuestionReference;
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
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final QuestionReference questionReference = new QuestionReference();
                questionReference.setId(rs.getLong(1));
                questionReference.setQuestionId(rs.getLong(2));
                questionReference.setRefId(rs.getLong(3));
                store(questionReference);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting QUESTION_REFERENCES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final QuestionReference questionReference) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/questionreference")
                .body(Mono.just(questionReference), QuestionReference.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}

