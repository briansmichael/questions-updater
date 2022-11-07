package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Ref.
 */
@Data
public class Test implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Test ID.
     */
    private Long testId;

    /**
     * TestName.
     */
    private String testName;

    /**
     * TestAbbr.
     */
    private String testAbbr;

    /**
     * Group ID.
     */
    private Long groupId;

    /**
     * Sort By.
     */
    private Long sortBy;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
