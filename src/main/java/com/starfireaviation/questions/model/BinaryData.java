package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ACS.
 */
@Data
public class BinaryData implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long id;

    /**
     * Category.
     */
    private Long category;

    /**
     * Group ID.
     */
    private Long groupId;

    /**
     * Image name.
     */
    private String imageName;

    /**
     * Description.
     */
    private String description;

    /**
     * Filename.
     */
    private String fileName;

    /**
     * Bin Type.
     */
    private Long binType;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
