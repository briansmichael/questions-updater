package com.starfireaviation.questions.service;

import com.starfireaviation.common.CommonConstants;
import com.starfireaviation.common.model.Image;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                store(image);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        } catch (IOException | IllegalArgumentException e) {
            log.error("Unable to save image.  Error message: {}", e.getMessage());
        }
        log.info("Finished getting IMAGES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final Image image) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/images")
                .body(Mono.just(image), Image.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}
