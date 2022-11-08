package com.starfireaviation.questions.service;

import com.starfireaviation.questions.model.BinaryData;
import lombok.extern.slf4j.Slf4j;

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
             Connection mysqlConn = getMySQLConnection();
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
                if (existsBinaryData(binaryData.getId(), mysqlConn)) {
                    final String update = "UPDATE binary_data SET category = ?, group_id = ?, image_name = ?, "
                            + "description = ?, file_name = ?, bin_type = ?, last_modified = ? WHERE id = ?";
                    updateCount += store(binaryData, update, mysqlConn);
                } else {
                    final String insert = "INSERT INTO binary_data (category, group_id, image_name, description, "
                            + "file_name, bin_type, last_modified, id) VALUES (?,?,?,?,?,?,?,?)";
                    insertCount += store(binaryData, insert, mysqlConn);
                }
            }
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        log.info("Finished getting BINARY_DATA table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    private long store(final BinaryData binaryData, final String query, final Connection mysqlConn) {
        try (PreparedStatement ps = mysqlConn.prepareStatement(query)) {
            ps.setLong(1, binaryData.getCategory());
            ps.setLong(2, binaryData.getGroupId());
            ps.setString(3, binaryData.getImageName());
            ps.setString(4, binaryData.getDescription());
            ps.setString(5, binaryData.getFileName());
            ps.setLong(6, binaryData.getBinType());
            ps.setTimestamp(7, new java.sql.Timestamp(binaryData.getLastModified().getTime()));
            ps.setLong(8, binaryData.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error message: {}", e.getMessage());
        }
        return 0;
    }

    private boolean existsBinaryData(final Long id, final Connection mysqlConn) {
        final String query = "SELECT 1 FROM binary_data WHERE id = ?";
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

