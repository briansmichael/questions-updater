package com.starfireaviation.questions.service;

import com.starfireaviation.common.CommonConstants;
import com.starfireaviation.common.model.Answer;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jsoup.Jsoup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class AnswerService extends BaseService {

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
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Answer answer = new Answer();
                answer.setId(rs.getLong(1));
                answer.setText(Jsoup.parse(decrypt(rs.getString(2))).text());
                answer.setQuestionId(rs.getLong(CommonConstants.THREE));
                answer.setCorrect(rs.getBoolean(CommonConstants.FOUR));
                answer.setLastModified(rs.getDate(CommonConstants.FIVE));
                store(answer);
            }
        } catch (SQLException | InvalidCipherTextException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting ANSWERS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final Answer answer) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/answers")
                .body(Mono.just(answer), Answer.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}
