package com.starfireaviation.questions.service;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.sql.SQLException;

@Slf4j
public class BinaryDataService extends BaseService {

    public BinaryDataService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    /**
     * Binary Data.
     */
    public static final String BINARY_DATA_QUERY = "SELECT ID, Category, GroupID, ImageName, "
            + "Desc, FileName, BinType, BinData, LastMod FROM BinaryData";

    public void run() {
        log.info("Getting BINARY_DATA table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        log.info("Finished getting BINARY_DATA table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    /**
     * Gets binary data from remote database.
     *
     * @param conn Remote database connection
     * @throws SQLException SQLException
     * @throws InvalidCipherTextException InvalidCipherTextException
     */
//    private void getBinaryData(final Connection sqlLiteConn, final Connection mysqlConn) {
//        try (PreparedStatement ps = conn.prepareStatement(BINARY_DATA_QUERY);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                final Long remoteId = rs.getLong(1);
//                final BinaryDataEntity binaryData =
//                        binaryDataRepository.findByRemoteId(remoteId).orElse(new BinaryDataEntity());
//                binaryData.setRemoteId(remoteId);
//                binaryData.setCategory(rs.getLong(2));
//                binaryData.setGroupId(rs.getLong(CommonConstants.THREE));
//                binaryData.setImageName(rs.getString(CommonConstants.FOUR));
//                binaryData.setDescription(rs.getString(CommonConstants.FIVE));
//                binaryData.setFileName(rs.getString(CommonConstants.SIX));
//                binaryData.setBinType(rs.getLong(CommonConstants.SEVEN));
//                binaryData.setBinData(rs.getBytes(CommonConstants.EIGHT));
//                binaryData.setLastModified(rs.getDate(CommonConstants.NINE));
//                try {
//                    binaryDataRepository.save(binaryData);
//                } catch (SQLException e) {
//                    log.error("Unable to save binary data: {}.  Error message: {}",
//                            binaryData.getRemoteId(), e.getMessage());
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error message: {}", e.getMessage());
//        }
//    }

}
