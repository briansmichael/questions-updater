package com.starfireaviation.questions.service;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.sql.SQLException;

@Slf4j
public class FigureSectionService extends BaseService {

    /**
     * Figure Sections.
     */
    public static final String FIGURE_SECTIONS_QUERY = "SELECT FigureSectionID, FigureSection, LastMod "
            + "FROM FigureSections";

    public FigureSectionService(final String course, final String host, final String user, final String pass) {
        super(course, host, user, pass);
    }

    public void run() {
        log.info("Getting FIGURE_SECTIONS table data for course: {}", course);
        long updateCount = 0;
        long insertCount = 0;
        log.info("Finished getting FIGURE_SECTIONS table data for course: {}; Inserted: {}; Updated: {}",
                course, insertCount, updateCount);
    }

    /**
     * Gets figure sections from remote database.
     *
     * @param conn Remote database connection
     * @throws SQLException SQLException
     * @throws InvalidCipherTextException InvalidCipherTextException
     */
//    private void getFigureSections(final Connection sqlLiteConn, final Connection mysqlConn) {
//        try (PreparedStatement ps = conn.prepareStatement(FIGURE_SECTIONS_QUERY);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                final Long remoteId = rs.getLong(1);
//                final FigureSectionEntity figureSection = figureSectionsRepository.findByFigureSectionId(remoteId)
//                        .orElse(new FigureSectionEntity());
//                figureSection.setFigureSectionId(remoteId);
//                figureSection.setFigureSection(rs.getString(2));
//                figureSection.setLastModified(rs.getDate(CommonConstants.THREE));
//                try {
//                    figureSectionsRepository.save(figureSection);
//                } catch (SQLException e) {
//                    log.error("Unable to save figure section: {}.  Error message: {}",
//                            figureSection.getFigureSectionId(), e.getMessage());
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error message: {}", e.getMessage());
//        }
//    }


}
