package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Image.
 */
@Data
public class Image implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long id;

    /**
     * PicType.
     */
    private Long picType;

    /**
     * GroupID.
     */
    private Long groupId;

    /**
     * Test ID.
     */
    private Long testId;

    /**
     * ImageName.
     */
    private String imageName;

    /**
     * Description.
     */
    private String description;

    /**
     * FileName.
     */
    private String fileName;

    /**
     * Figure Section ID.
     */
    private Long figureSectionId;

    /**
     * Pixels per NM.
     */
    private Double pixelsPerNM;

    /**
     * Sort By.
     */
    private Long sortBy;

    /**
     * Image Library ID.
     */
    private Long imageLibraryId;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
