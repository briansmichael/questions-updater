package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Chapter.
 */
@Data
public class Chapter implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long chapterId;

    /**
     * ChapterName.
     */
    private String chapterName;

    /**
     * Group ID.
     */
    private Long groupId;

    /**
     * Sort By.
     */
    private Long sortBy;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
