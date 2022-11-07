package com.starfireaviation.questions.service;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.sql.SQLException;

@Slf4j
public class QuestionTestService extends BaseService {

    /**
     * Question Tests.
     */
    public static final String QUESTION_TESTS_QUERY = "SELECT ID, QuestionID, TestID, IsLinked, SortBy, LinkChapter, "
            + "IsImportant FROM QuestionsTests";

    public QuestionTestService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting QUESTION_TESTS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        log.info("Finished getting QUESTION_TESTS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    /**
     * Gets QuestionTests from remote database.
     *
     * @param conn Remote database connection
     * @throws SQLException SQLException
     * @throws InvalidCipherTextException InvalidCipherTextException
     */
//    private void getQuestionTests(final Connection sqlLiteConn, final Connection mysqlConn) {
//        try (PreparedStatement ps = conn.prepareStatement(QUESTION_TESTS_QUERY);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                final Long remoteId = rs.getLong(1);
//                final QuestionTestEntity questionTest = questionTestsRepository.findByRemoteId(remoteId)
//                        .orElse(new QuestionTestEntity());
//                questionTest.setRemoteId(remoteId);
//                questionTest.setQuestionId(rs.getLong(2));
//                questionTest.setTestId(rs.getLong(CommonConstants.THREE));
//                questionTest.setIsLinked(rs.getLong(CommonConstants.FOUR));
//                questionTest.setSortBy(rs.getLong(CommonConstants.FIVE));
//                questionTest.setLinkChapter(rs.getLong(CommonConstants.SIX));
//                questionTest.setIsImportant(rs.getLong(CommonConstants.SEVEN));
//                try {
//                    questionTestsRepository.save(questionTest);
//                } catch (SQLException e) {
//                    log.error("Unable to save question test: {}.  Error message: {}",
//                            questionTest.getRemoteId(), e.getMessage());
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error message: {}", e.getMessage());
//        }
//    }

}
