package com.starfireaviation.questions.service;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.sql.SQLException;

@Slf4j
public class LibraryService extends BaseService {

    /**
     * Library.
     */
    public static final String LIBRARY_QUERY = "SELECT ID, Region, ParentID, Name, Description, "
            + "IsSection, Source, Ordinal, LastMod FROM Library";

    public LibraryService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting LIBRARY table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        log.info("Finished getting LIBRARY table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    /**
     * Gets library from remote database.
     *
     * @param conn Remote database connection
     * @throws SQLException SQLException
     * @throws InvalidCipherTextException InvalidCipherTextException
     */
//    private void getLibrary(final Connection sqlLiteConn, final Connection mysqlConn) {
//        try (PreparedStatement ps = conn.prepareStatement(LIBRARY_QUERY);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                final Long remoteId = rs.getLong(1);
//                final LibraryEntity library = librarysRepository.findByRemoteId(remoteId).orElse(new LibraryEntity());
//                library.setRemoteId(remoteId);
//                library.setRegion(rs.getString(2));
//                library.setParentId(rs.getLong(CommonConstants.THREE));
//                library.setName(rs.getString(CommonConstants.FOUR));
//                library.setDescription(rs.getString(CommonConstants.FIVE));
//                library.setIsSection(rs.getLong(CommonConstants.SIX));
//                library.setSource(rs.getString(CommonConstants.SEVEN));
//                library.setOrdinal(rs.getLong(CommonConstants.EIGHT));
//                library.setLastModified(rs.getDate(CommonConstants.NINE));
//                try {
//                    librarysRepository.save(library);
//                } catch (SQLException e) {
//                    log.error("Unable to save library: {}.  Error message: {}",
//                            library.getRemoteId(), e.getMessage());
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error message: {}", e.getMessage());
//        }
//    }

}
