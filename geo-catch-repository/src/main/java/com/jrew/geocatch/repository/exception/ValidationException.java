package com.jrew.geocatch.repository.exception;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/23/13
 * Time: 2:47 PM
 */
public class ValidationException extends RuntimeException {

    /** **/
    private List<String> errors;

    /**
     *
     * @param errors
     */
    public ValidationException(List<String> errors) {
        this.errors = errors;
    }

    /**
     *
     * @return
     */
    public List<String> getErrors() {
        return errors;
    }
}
