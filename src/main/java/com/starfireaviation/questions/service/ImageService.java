package com.starfireaviation.questions.service;

import com.starfireaviation.questions.CommonConstants;
import com.starfireaviation.questions.model.Image;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Slf4j
public class ImageService extends BaseService {

    public static final String IMAGE_DIR = "/mnt/public/html/media";

    public ImageService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    /**
     * Gets images from remote database.
     */
    public void run() {
        log.info("Getting IMAGES table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, PicType, GroupID, TestID, ImageName, Desc, FileName, BinImage, LastMod, " +
                "FigureSectionID, PixelsPerNM, SortBy, ImageLibraryID FROM Images";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             Connection mysqlConn = getMySQLConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Image image = new Image();
                image.setId(rs.getLong(1));
                image.setPicType(rs.getLong(2));
                image.setGroupId(rs.getLong(CommonConstants.THREE));
                image.setTestId(rs.getLong(CommonConstants.FOUR));
                image.setImageName(rs.getString(CommonConstants.FIVE));
                image.setDescription(rs.getString(CommonConstants.SIX));
                image.setFileName(rs.getString(CommonConstants.SEVEN));
                if (image.getFileName() != null) {
                    image.setFileName(image.getFileName().replaceAll(" ", "_"));
                }
                image.setLastModified(rs.getDate(CommonConstants.NINE));
                image.setFigureSectionId(rs.getLong(CommonConstants.TEN));
                image.setPixelsPerNM(rs.getDouble(CommonConstants.ELEVEN));
                image.setSortBy(rs.getLong(CommonConstants.TWELVE));
                image.setImageLibraryId(rs.getLong(CommonConstants.THIRTEEN));
                if (image.getFileName() != null && !"".equals(image.getFileName())) {
                    //final String fileName = IMAGE_DIR + "/" + image.getFileName();
                    final String fileName = IMAGE_DIR + "/" + image.getId() + ".png";
                    FileUtils.writeByteArrayToFile(new File(fileName), rs.getBytes(CommonConstants.EIGHT));
                }
                if (existsImage(image.getId(), mysqlConn)) {
                    final String update = "UPDATE images SET description = ?, figure_section_id = ?, file_name = ?, "
                            + "group_id = ?, image_library_id = ?, image_name = ?, last_modified = ?, pic_type = ?, "
                            + "pixels_pernm = ?, sort_by = ?, test_id = ? WHERE id = ?";
                    updateCount += store(image, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO images (description, figure_section_id, file_name, group_id, "
                            + "image_library_id, image_name, last_modified, pic_type, pixels_pernm, sort_by, test_id, id) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                    insertCount += store(image, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        } catch (IOException | IllegalArgumentException e) {
            log.error("Unable to save image.  Error message: {}", e.getMessage());
        }
        log.info("Finished getting IMAGES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final Image image, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            if (image.getDescription() != null) {
                ps.setString(1, image.getDescription().replaceAll("\\P{Print}", ""));
            } else {
                ps.setNull(1, Types.NULL);
            }
            ps.setLong(2, image.getFigureSectionId());
            ps.setString(3, image.getFileName());
            ps.setLong(4, image.getGroupId());
            ps.setLong(5, image.getImageLibraryId());
            ps.setString(6, image.getImageName().replaceAll("\\P{Print}", ""));
            ps.setTimestamp(7, new java.sql.Timestamp(image.getLastModified().getTime()));
            ps.setLong(8, image.getPicType());
            ps.setDouble(9, image.getPixelsPerNM());
            ps.setLong(10, image.getSortBy());
            ps.setLong(11, image.getTestId());
            ps.setLong(12, image.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsImage(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM images WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = mysqlConn.prepareStatement(query);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return Boolean.TRUE;
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        } finally {
            try { rs.close(); } catch (Exception e) {}
            try { ps.close(); } catch (Exception e) {}
        }
        return Boolean.FALSE;
    }

}
