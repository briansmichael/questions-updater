package com.starfireaviation.questions.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubjectMatterCodeService extends BaseService {

    /**
     * Subject Matter Codes.
     */
    public static final String SUBJECT_MATTER_CODES_QUERY = "SELECT ID, Code, SourceID, Description, LastMod, IsLSC "
            + "FROM SubjectMatterCodes";

    public SubjectMatterCodeService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting SUBJECT_MATTER_CODES table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        log.info("Finished getting SUBJECT_MATTER_CODES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    /**
     * Gets SubjectMatterCodes from remote database.
     *
     * @param conn Remote database connection
     */
//    private void getSubjectMatterCodes(final Connection sqlLiteConn, final Connection mysqlConn) {
//        try (PreparedStatement ps = conn.prepareStatement(SUBJECT_MATTER_CODES_QUERY);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                final Long remoteId = rs.getLong(1);
//                final SubjectMatterCodeEntity subjectMatterCode = subjectMatterCodesRepository
//                        .findByRemoteId(remoteId).orElse(new SubjectMatterCodeEntity());
//                subjectMatterCode.setRemoteId(remoteId);
//                subjectMatterCode.setCode(rs.getString(2));
//                subjectMatterCode.setSourceId(rs.getLong(CommonConstants.THREE));
//                subjectMatterCode.setDescription(rs.getString(CommonConstants.FOUR));
//                subjectMatterCode.setLastModified(rs.getDate(CommonConstants.FIVE));
//                subjectMatterCode.setIsLSC(rs.getLong(CommonConstants.SIX));
//                try {
//                    subjectMatterCodesRepository.save(subjectMatterCode);
//                } catch (SQLException e) {
//                    log.error("Unable to save subject matter code: {}.  Error message: {}",
//                            subjectMatterCode.getCode(), e.getMessage());
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error message: {}", e.getMessage());
//        }
//    }

}
