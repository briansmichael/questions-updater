package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Question.
 */
@Data
public class Question implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long questionId;

    /**
     * Question Text.
     */
    private String text;

    /**
     * Chapter ID.
     */
    private Long chapterId;

    /**
     * SMC ID.
     */
    private Long smcId;

    /**
     * Source.
     */
    private String source;

    /**
     * Last Modified.
     */
    private Date lastModified;

    /**
     * Explanation.
     */
    private String explanation;

    /**
     * Old Question ID.
     */
    private Long oldQuestionId;

    /**
     * LearningStatementCode.
     */
    private Long lscId;

}
