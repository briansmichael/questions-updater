package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.Library;
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
public class LibraryService extends BaseService {

    /**
     * Library.
     */
    public LibraryService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting LIBRARY table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, Region, ParentID, Name, Description, IsSection, Source, Ordinal, LastMod "
                + "FROM Library";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Library library = new Library();
                library.setId(rs.getLong(1));
                library.setRegion(rs.getString(2));
                library.setParentId(rs.getLong(3));
                library.setName(rs.getString(4));
                library.setDescription(rs.getString(5));
                library.setIsSection(rs.getLong(6));
                library.setSource(rs.getString(7));
                library.setOrdinal(rs.getLong(8));
                library.setLastModified(rs.getDate(9));
                store(library);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting LIBRARY table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final Library library) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/library")
                .body(Mono.just(library), Library.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}

