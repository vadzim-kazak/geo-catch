package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.model.Email;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 5/9/14
 * Time: 4:15 AM
 */
public interface MailService {

    /**
     *
     * @param email
     */
    public void sendMail(Email email) throws Exception;

}
