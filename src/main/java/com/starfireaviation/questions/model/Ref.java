package com.starfireaviation.questions.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Ref.
 */
@Data
public class Ref implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ref ID.
     */
    private Long refId;

    /**
     * RefText.
     */
    private String refText;

    /**
     * Last Modified.
     */
    private Date lastModified;

}
