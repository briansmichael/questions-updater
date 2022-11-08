package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.FigureSection;
import lombok.extern.slf4j.Slf4j;

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
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final FigureSection figureSection = new FigureSection();
                figureSection.setFigureSectionId(rs.getLong(1));
                figureSection.setFigureSection(rs.getString(2));
                figureSection.setLastModified(rs.getDate(3));
                if (existsFigureSection(figureSection.getFigureSectionId(), mysqlConn)) {
                    final String update = "UPDATE figure_section SET figure_section = ?, last_modified = ? "
                            + "WHERE figure_section_id = ?";
                    updateCount += store(figureSection, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO binary_data (figure_section, last_modified, "
                            + "figure_section_id) VALUES (?,?,?)";
                    insertCount += store(figureSection, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting FIGURE_SECTIONS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final FigureSection figureSection, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, figureSection.getFigureSection());
            ps.setTimestamp(2, new java.sql.Timestamp(figureSection.getLastModified().getTime()));
            ps.setLong(3, figureSection.getFigureSectionId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsFigureSection(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM figure_section WHERE id = ?";
        ResultSet rs = null;
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return Boolean.TRUE;
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        } finally {
            try { rs.close(); } catch (Exception e) {}
        }
        return Boolean.FALSE;
    }

}

