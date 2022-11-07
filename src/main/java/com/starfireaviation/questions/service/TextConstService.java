package com.starfireaviation.questions.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextConstService extends BaseService {

    /**
     * Text Const.
     */
    public static final String TEXT_CONST_QUERY = "SELECT ID, ConstName, ConstValue, GroupID, TestID, LastMod "
            + "FROM TextConst";

    public TextConstService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting TEXT_CONST table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        log.info("Finished getting TEXT_CONST table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    /**
     * Gets TextConst from remote database.
     *
     * @param sqlLiteConn Remote database connection
     */
//    private void getTextConst(final Connection sqlLiteConn, final Connection mysqlConn) {
//        try (PreparedStatement ps = sqlLiteConn.prepareStatement(TEXT_CONST_QUERY);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                final Long remoteId = rs.getLong(1);
//                final TextConstEntity textConst =
//                        textConstRepository.findByRemoteId(remoteId).orElse(new TextConstEntity());
//                textConst.setRemoteId(remoteId);
//                textConst.setConstName(rs.getString(2));
//                textConst.setConstValue(rs.getString(CommonConstants.THREE));
//                textConst.setGroupId(rs.getLong(CommonConstants.FOUR));
//                textConst.setTestId(rs.getLong(CommonConstants.FIVE));
//                textConst.setLastModified(rs.getDate(CommonConstants.SIX));
//                try {
//                    textConstRepository.save(textConst);
//                } catch (SQLException e) {
//                    log.error("Unable to save text const: {}.  Error message: {}",
//                            textConst.getConstName(), e.getMessage());
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error message: {}", e.getMessage());
//        }
//    }

}
