package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * FigureSection.
 */
@Data
public class FigureSection implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Figure Section ID.
     */
    private Long figureSectionId;

    /**
     * Figure Section.
     */
    private String figureSection;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
