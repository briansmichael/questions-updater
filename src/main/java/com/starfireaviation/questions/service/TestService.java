package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.Test;
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
public class TestService extends BaseService {

    /**
     * Tests.
     */
    public TestService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting TESTS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT TestID, TestName, TestAbbr, GroupID, SortBy, LastMod FROM Tests";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Test test = new Test();
                test.setTestId(rs.getLong(1));
                test.setTestName(rs.getString(2));
                test.setTestAbbr(rs.getString(3));
                test.setGroupId(rs.getLong(4));
                test.setSortBy(rs.getLong(5));
                test.setLastModified(rs.getDate(6));
                store(test);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting TESTS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final Test test) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/tests")
                .body(Mono.just(test), Test.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}

