package com.starfireaviation.questions.service;

import com.starfireaviation.common.CommonConstants;
import com.starfireaviation.common.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jsoup.Jsoup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

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
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Question question = new Question();
                question.setId(rs.getLong(1));
                question.setText(decrypt(rs.getString(2)));
                question.setChapterId(rs.getLong(CommonConstants.THREE));
                question.setSmcId(rs.getLong(CommonConstants.FOUR));
                question.setSource(rs.getString(CommonConstants.FIVE));
                question.setLastModified(rs.getDate(CommonConstants.SIX));
                question.setExplanation(Jsoup.parse(decrypt(rs.getString(CommonConstants.SEVEN))).text());
                question.setOldQuestionId(rs.getLong(CommonConstants.EIGHT));
                question.setLearningStatementCode(rs.getString(CommonConstants.NINE));
                store(question);
            }
        } catch (SQLException | InvalidCipherTextException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting QUESTIONS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final Question question) {
        final ResponseEntity<Void> responseEntity = webClient
                .method(HttpMethod.POST)
                .uri("/api/questions")
                .body(Mono.just(question), Question.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .toBodilessEntity()
                .block();
        if (responseEntity != null) {
            log.info("Response status code: {}", responseEntity.getStatusCode());
        } else {
            log.info("No response received");
        }
    }

}
