package com.starfireaviation.questions.service;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.sql.SQLException;

@Slf4j
public class QuestionReferenceService extends BaseService {

    /**
     * Question References.
     */
    public static final String QUESTION_REFERENCES_QUERY = "SELECT ID, QuestionID, RefID FROM QuestionsReferences";

    public QuestionReferenceService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting QUESTION_REFERENCES table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        log.info("Finished getting QUESTION_REFERENCES table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    /**
     * Gets QuestionReferences from remote database.
     *
     * @param conn Remote database connection
     * @throws SQLException SQLException
     * @throws InvalidCipherTextException InvalidCipherTextException
     */
//    private void getQuestionReferences(final Connection sqlLiteConn, final Connection mysqlConn) {
//        try (PreparedStatement ps = conn.prepareStatement(QUESTION_REFERENCES_QUERY);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                final Long remoteId = rs.getLong(1);
//                final QuestionReferenceEntity questionReference = questionReferencesRepository.findByRemoteId(remoteId)
//                        .orElse(new QuestionReferenceEntity());
//                questionReference.setRemoteId(remoteId);
//                questionReference.setQuestionId(rs.getLong(2));
//                questionReference.setRefId(rs.getLong(CommonConstants.THREE));
//                try {
//                    questionReferencesRepository.save(questionReference);
//                } catch (SQLException e) {
//                    log.error("Unable to save question reference: {}.  Error message: {}",
//                            questionReference.getRemoteId(), e.getMessage());
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error message: {}", e.getMessage());
//        }
//    }

}
