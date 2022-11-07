package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * QuestionTest.
 */
@Data
public class QuestionTest implements Serializable {

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
     * Test ID.
     */
    private Long testId;

    /**
     * Is Linked.
     */
    private Long isLinked;

    /**
     * Sort By.
     */
    private Long sortBy;

    /**
     * Link Chapter.
     */
    private Long linkChapter;

    /**
     * Is Important.
     */
    private Long isImportant;

}
