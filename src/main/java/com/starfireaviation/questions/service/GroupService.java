package com.starfireaviation.questions.service;

import com.starfireaviation.common.model.Group;
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
public class GroupService extends BaseService {

    /**
     * Groups.
     */
    public GroupService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting GROUPS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        final String query = "SELECT GroupID, GroupName, GroupAbbr, LastMod FROM Groups";
        try (Connection sqlLiteConn = getSQLLiteConnection();
             PreparedStatement ps = sqlLiteConn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Group group = new Group();
                group.setGroupId(rs.getLong(1));
                group.setGroupName(rs.getString(2));
                group.setGroupAbbr(rs.getString(3));
                group.setLastModified(rs.getDate(4));
                store(group);
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting GROUPS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private void store(final Group group) {
        webClient
                .method(HttpMethod.POST)
                .uri("/api/groups")
                .body(Mono.just(group), Group.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();
    }

}
