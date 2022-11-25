package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.BinaryData;
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
public class BinaryDataService extends BaseService {

    public BinaryDataService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    /**
     * Binary Data.
     */
    public void run() {
        log.info("Getting BINARY_DATA table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, Category, GroupID, ImageName, Desc, FileName, BinType, BinData, LastMod FROM BinaryData";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final BinaryData binaryData = new BinaryData();
                binaryData.setId(rs.getLong(1));
                binaryData.setCategory(rs.getLong(2));
                binaryData.setGroupId(rs.getLong(3));
                binaryData.setImageName(rs.getString(4));
                binaryData.setDescription(rs.getString(5));
                binaryData.setFileName(rs.getString(6));
                binaryData.setBinType(rs.getLong(7));
                binaryData.setLastModified(rs.getDate(9));
                store(binaryData);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting BINARY_DATA table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final BinaryData binaryData) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/binarydata")
                .body(Mono.just(binaryData), BinaryData.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}

