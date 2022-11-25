package com.starfireaviation.questions.service;

import com.starfireaviation.common.CommonConstants;
import com.starfireaviation.common.model.ACS;
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
public class ACSService extends BaseService {

    public ACSService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    /**
     * Gets ACS data from remote database.
     */
    public void run() {
        log.info("Getting ACS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT ID, GroupID, ParentID, Code, Description, IsCompletedCode, LastMod FROM ACS";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final ACS acs = new ACS();
                acs.setId(rs.getLong(1));
                acs.setGroupId(rs.getLong(2));
                acs.setParentId(rs.getLong(CommonConstants.THREE));
                acs.setCode(rs.getString(CommonConstants.FOUR));
                acs.setDescription(rs.getString(CommonConstants.FIVE));
                acs.setIsCompletedCode(rs.getLong(CommonConstants.SIX));
                acs.setLastModified(rs.getDate(CommonConstants.SEVEN));
                store(acs);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting ACS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final ACS acs) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/acs")
                .body(Mono.just(acs), ACS.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }
}
