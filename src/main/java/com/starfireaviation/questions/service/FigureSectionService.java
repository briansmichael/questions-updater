package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.FigureSection;
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
public class FigureSectionService extends BaseService {

    /**
     * Figure Sections.
     */
    public FigureSectionService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting FIGURE_SECTIONS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT FigureSectionID, FigureSection, LastMod FROM FigureSections";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final FigureSection figureSection = new FigureSection();
                figureSection.setFigureSectionId(rs.getLong(1));
                figureSection.setFigureSection(rs.getString(2));
                figureSection.setLastModified(rs.getDate(3));
                store(figureSection);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting FIGURE_SECTIONS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final FigureSection figureSection) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/figuresection")
                .body(Mono.just(figureSection), FigureSection.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}

