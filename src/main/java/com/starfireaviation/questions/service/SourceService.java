package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.Source;
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
public class SourceService extends BaseService {

    /**
     * Sources.
     */
    public SourceService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting SOURCES table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, Author, Title, Abbreviation, LastMod FROM Sources";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Source source = new Source();
                source.setId(rs.getLong(1));
                source.setAuthor(rs.getString(2));
                source.setTitle(rs.getString(3));
                source.setAbbreviation(rs.getString(4));
                source.setLastModified(rs.getDate(5));
                store(source);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting SOURCES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final Source source) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/sources")
                .body(Mono.just(source), Source.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}

