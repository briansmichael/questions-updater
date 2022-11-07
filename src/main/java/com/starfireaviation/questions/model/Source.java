package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Source.
 */
@Data
public class Source implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long id;

    /**
     * Author.
     */
    private String author;

    /**
     * Title.
     */
    private String title;

    /**
     * Abbreviation.
     */
    private String abbreviation;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
