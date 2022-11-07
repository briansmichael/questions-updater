package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ACS.
 */
@Data
public class ACS implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long id;

    /**
     * Group ID.
     */
    private Long groupId;

    /**
     * Parent ID.
     */
    private Long parentId;

    /**
     * Code.
     */
    private String code;

    /**
     * Description.
     */
    private String description;

    /**
     * IsCompletedCode.
     */
    private Long isCompletedCode;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
