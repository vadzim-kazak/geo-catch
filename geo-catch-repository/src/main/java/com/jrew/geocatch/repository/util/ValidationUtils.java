package com.jrew.geocatch.repository.util;

import com.jrew.geocatch.repository.exception.ValidationException;
import com.jrew.geocatch.repository.model.DomainProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/23/13
 * Time: 2:33 PM
 */
public class ValidationUtils {


    /**
     *
     * @param object
     * @throws ValidationException
     */
    public static void validate(Object object, Validator validator) throws ValidationException {

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        if (constraintViolations != null && !constraintViolations.isEmpty()) {
            List<String> errors = new ArrayList<String>();
            for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
                String propertyPath = constraintViolation.getPropertyPath().toString();
                String message = constraintViolation.getMessage();
                errors.add(propertyPath + ' ' + message);
            }
            throw new ValidationException(errors);
        }
    }

    /**
     *
     * @param domainProperty
     * @return
     */
    private boolean validateDomainProperty(DomainProperty domainProperty) {
        if (domainProperty != null) {

            if (domainProperty.getId() > 0) {
                return true;
            }

            if (domainProperty.getType() > 0 && !StringUtils.isEmpty(domainProperty.getValue()) &&
                    !StringUtils.isEmpty(domainProperty.getLocale())) {
                return true;
            }
        }

        return false;
    }

}
