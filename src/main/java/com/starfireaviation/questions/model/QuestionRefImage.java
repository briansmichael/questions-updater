package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * QuestionRefImage.
 */
@Data
public class QuestionRefImage implements Serializable {

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
     * Image ID.
     */
    private Long imageId;

    /**
     * Annotation.
     */
    private String annotation;

}
