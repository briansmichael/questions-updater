package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.Library;
import lombok.extern.slf4j.Slf4j;

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
             Connection mysqlConn = getMySQLConnection();
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
                if (existsLibrary(library.getId(), mysqlConn)) {
                    final String update = "UPDATE library SET region = ?, parent_id = ?, name = ?, description = ?, "
                            + "is_section = ?, source = ?, ordinal = ?, last_modified = ? WHERE id = ?";
                    updateCount += store(library, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO library (region, parent_id, name, description, is_section, "
                            + "source, ordinal, last_modified, id) VALUES (?,?,?,?,?,?,?,?,?)";
                    insertCount += store(library, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting LIBRARY table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final Library library, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, library.getRegion());
            ps.setLong(2, library.getParentId());
            ps.setString(3, library.getName());
            ps.setString(4, library.getDescription());
            ps.setLong(5, library.getIsSection());
            ps.setString(6, library.getSource());
            ps.setLong(7, library.getOrdinal());
            ps.setTimestamp(8, new java.sql.Timestamp(library.getLastModified().getTime()));
            ps.setLong(9, library.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsLibrary(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM library WHERE id = ?";
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

