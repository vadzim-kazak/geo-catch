package com.jrew.geocatch.repository.controller;

import com.jrew.geocatch.repository.model.Email;
import com.jrew.geocatch.repository.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 5/9/14
 * Time: 4:27 AM
 */
@Controller
@RequestMapping("/util")
public class UtilityController {

    @Autowired
    MailService mailService;

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void sendEmail(@RequestBody Email email) {
        try {
            mailService.sendMail(email);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
