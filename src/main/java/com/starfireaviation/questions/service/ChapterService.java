package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.Chapter;
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
public class ChapterService extends BaseService {

    /**
     * Chapters.
     */
    public ChapterService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting CHAPTERS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ChapterID, ChapterName, GroupID, SortBy, LastMod FROM Chapters";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Chapter chapter = new Chapter();
                chapter.setChapterId(rs.getLong(1));
                chapter.setChapterName(rs.getString(2));
                chapter.setGroupId(rs.getLong(3));
                chapter.setSortBy(rs.getLong(4));
                chapter.setLastModified(rs.getDate(5));
                store(chapter);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting CHAPTERS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final Chapter chapter) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/chapters")
                .body(Mono.just(chapter), Chapter.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}
