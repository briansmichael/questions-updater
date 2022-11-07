package com.starfireaviation.questions.service;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.sql.SQLException;

@Slf4j
public class RefService extends BaseService {

    /**
     * Refs.
     */
    public static final String REFS_QUERY = "SELECT RefID, RefText, LastMod FROM Refs";

    public RefService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting REFS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        log.info("Finished getting REFS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    /**
     * Gets Refs from remote database.
     *
     * @param conn Remote database connection
     * @throws SQLException SQLException
     * @throws InvalidCipherTextException InvalidCipherTextException
     */
//    private void getRefs(final Connection sqlLiteConn, final Connection mysqlConn) {
//        try (PreparedStatement ps = conn.prepareStatement(REFS_QUERY);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                final Long remoteId = rs.getLong(1);
//                final RefEntity ref = refsRepository.findByRefId(remoteId).orElse(new RefEntity());
//                ref.setRefId(remoteId);
//                ref.setRefText(rs.getString(2));
//                ref.setLastModified(rs.getDate(CommonConstants.THREE));
//                try {
//                    refsRepository.save(ref);
//                } catch (SQLException e) {
//                    log.error("Unable to save ref: {}.  Error message: {}",
//                            ref.getRefText(), e.getMessage());
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error message: {}", e.getMessage());
//        }
//    }


}
