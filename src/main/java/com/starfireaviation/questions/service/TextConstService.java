package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.TextConst;
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
public class TextConstService extends BaseService {

    /**
     * Text Const.
     */
    public TextConstService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting TEXT_CONST table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, ConstName, ConstValue, GroupID, TestID, LastMod FROM TextConst";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final TextConst textConst = new TextConst();
                textConst.setId(rs.getLong(1));
                textConst.setConstName(rs.getString(2));
                textConst.setConstValue(rs.getString(3));
                textConst.setGroupId(rs.getLong(4));
                textConst.setTestId(rs.getLong(5));
                textConst.setLastModified(rs.getDate(6));
                store(textConst);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting TEXT_CONST table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final TextConst textConst) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/textconsts")
                .body(Mono.just(textConst), TextConst.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}

