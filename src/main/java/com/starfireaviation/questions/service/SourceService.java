package com.starfireaviation.questions.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SourceService extends BaseService {

    /**
     * Sources.
     */
    public static final String SOURCES_QUERY = "SELECT ID, Author, Title, Abbreviation, LastMod "
            + "FROM Sources";

    public SourceService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting SOURCES table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        log.info("Finished getting SOURCES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    /**
     * Gets Sources from remote database.
     *
     * @param conn Remote database connection
     */
//    private void getSources(final Connection sqlLiteConn, final Connection mysqlConn) {
//        try (PreparedStatement ps = conn.prepareStatement(SOURCES_QUERY);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                final Long remoteId = rs.getLong(1);
//                final SourceEntity source = sourcesRepository.findByRemoteId(remoteId).orElse(new SourceEntity());
//                source.setRemoteId(remoteId);
//                source.setAuthor(rs.getString(2));
//                source.setTitle(rs.getString(CommonConstants.THREE));
//                source.setAbbreviation(rs.getString(CommonConstants.FOUR));
//                source.setLastModified(rs.getDate(CommonConstants.FIVE));
//                try {
//                    sourcesRepository.save(source);
//                } catch (SQLException e) {
//                    log.error("Unable to save source: {}.  Error message: {}",
//                            source.getTitle(), e.getMessage());
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error message: {}", e.getMessage());
//        }
//    }

}
