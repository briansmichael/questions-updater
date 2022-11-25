package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.QuestionTest;
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
                store(questionTest);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting QUESTION_TESTS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final QuestionTest questionTest) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/questiontest")
                .body(Mono.just(questionTest), QuestionTest.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}

