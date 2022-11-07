package com.starfireaviation.questions.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestService extends BaseService {

    /**
     * Tests.
     */
    public static final String TESTS_QUERY = "SELECT TestID, TestName, TestAbbr, GroupID, SortBy, LastMod "
            + "FROM Tests";

    public TestService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting TESTS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        log.info("Finished getting TESTS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    /**
     * Gets Tests from remote database.
     *
     * @param conn Remote database connection
     */
//    private void getTests(final Connection sqlLiteConn, final Connection mysqlConn) {
//        try (PreparedStatement ps = conn.prepareStatement(TESTS_QUERY);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                final Long remoteId = rs.getLong(1);
//                final TestEntity test = testsRepository.findByTestId(remoteId).orElse(new TestEntity());
//                test.setTestId(remoteId);
//                test.setTestName(rs.getString(2));
//                test.setTestAbbr(rs.getString(CommonConstants.THREE));
//                test.setGroupId(rs.getLong(CommonConstants.FOUR));
//                test.setSortBy(rs.getLong(CommonConstants.FIVE));
//                test.setLastModified(rs.getDate(CommonConstants.SIX));
//                try {
//                    testsRepository.save(test);
//                } catch (SQLException e) {
//                    log.error("Unable to save test: {}.  Error message: {}",
//                            test.getTestName(), e.getMessage());
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error message: {}", e.getMessage());
//        }
//    }


}
