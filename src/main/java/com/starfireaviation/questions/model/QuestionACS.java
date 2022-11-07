package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * QuestionACS.
 */
@Data
public class QuestionACS implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long id;

    /**
     * Question ID.
     */
    private Long questionId;

    /**
     * ACS ID.
     */
    private Long acsId;

}
