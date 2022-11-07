package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * SubjectMatterCode.
 */
@Data
public class SubjectMatterCode implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long id;

    /**
     * Code.
     */
    private String code;

    /**
     * Source ID.
     */
    private Long sourceId;

    /**
     * Description.
     */
    private String description;

    /**
     * Last Modified.
     */
    private Date lastModified;

    /**
     * Is LSC.
     */
    private Long isLSC;

}
