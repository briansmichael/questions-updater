package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.Ref;
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
public class RefService extends BaseService {

    /**
     * Refs.
     */
    public RefService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting REFS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT RefID, RefText, LastMod FROM Refs";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Long remoteId = rs.getLong(1);
                final Ref ref = new Ref();
                ref.setRefId(remoteId);
                ref.setRefText(rs.getString(2));
                ref.setLastModified(rs.getDate(3));
                store(ref);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting REFS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final Ref ref) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/ref")
                .body(Mono.just(ref), Ref.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}

