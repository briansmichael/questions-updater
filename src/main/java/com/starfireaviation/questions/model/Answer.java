package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Answer.
 */
@Data
public class Answer implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long answerId;

    /**
     * Answer Text.
     */
    private String text;

    /**
     * Question ID.
     */
    private Long questionId;

    /**
     * Is Correct.
     */
    private Boolean correct;

    /**
     * Choice.
     */
    private String choice;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
