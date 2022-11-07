package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Library.
 */
@Data
public class Library implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long id;

    /**
     * Region.
     */
    private String region;

    /**
     * ParentID.
     */
    private Long parentId;

    /**
     * Name.
     */
    private String name;

    /**
     * Description.
     */
    private String description;

    /**
     * IsSection.
     */
    private Long isSection;

    /**
     * Source.
     */
    private String source;

    /**
     * Ordinal.
     */
    private Long ordinal;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
