package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * QuestionReference.
 */
@Data
public class QuestionReference implements Serializable {

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
     * Ref ID.
     */
    private Long refId;

}
