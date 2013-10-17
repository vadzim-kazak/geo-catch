package com.jrew.geocatch.repository.service.generator;

import com.jrew.geocatch.repository.model.Image;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 9/11/13
 * Time: 1:10 PM
 *
 * File name generator interface
 */
public interface FileNameGenerator {

    /**
     *
     *
     * @param image
     * @return
     */
    public String generate(Image image) throws IllegalArgumentException;

}
