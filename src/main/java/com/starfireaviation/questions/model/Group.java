package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Group.
 */
@Data
public class Group implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Group ID.
     */
    private Long groupId;

    /**
     * Group Name.
     */
    private String groupName;

    /**
     * Group Abbr.
     */
    private String groupAbbr;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
