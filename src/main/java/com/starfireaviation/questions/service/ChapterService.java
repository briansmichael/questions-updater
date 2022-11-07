package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.Chapter;
import lombok.extern.slf4j.Slf4j;

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
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Chapter chapter = new Chapter();
                chapter.setChapterId(rs.getLong(1));
                chapter.setChapterName(rs.getString(2));
                chapter.setGroupId(rs.getLong(3));
                chapter.setSortBy(rs.getLong(4));
                chapter.setLastModified(rs.getDate(5));
                if (existsChapter(chapter.getChapterId(), mysqlConn)) {
                    final String update = "UPDATE chapters SET chapter_name = ?, group_id = ?, "
                            + "last_modified = ?, sort_by = ? WHERE chapter_id = ?";
                    updateCount =+ store(chapter, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO chapters (chapter_name, group_id, last_modified, sort_by, "
                            + "chapter_id) VALUES (?,?,?,?,?)";
                    insertCount += store(chapter, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting CHAPTERS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final Chapter chapter, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setString(1, chapter.getChapterName());
            ps.setLong(2, chapter.getGroupId());
            ps.setTimestamp(3, new java.sql.Timestamp(chapter.getLastModified().getTime()));
            ps.setLong(4, chapter.getSortBy());
            ps.setLong(5, chapter.getChapterId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsChapter(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM chapters WHERE chapter_id = ?";
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
