package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Ref.
 */
@Data
public class TextConst implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long id;

    /**
     * ConstName.
     */
    private String constName;

    /**
     * ConstValue.
     */
    private String constValue;

    /**
     * Group ID.
     */
    private Long groupId;

    /**
     * Test ID.
     */
    private Long testId;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
