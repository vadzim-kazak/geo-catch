package com.jrew.geocatch.repository.model;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 5/9/14
 * Time: 4:31 AM
 */
public class Email {

    /** **/
    private String from;

    /** **/
    private String to;

    /** **/
    private String subject;

    /** **/
    private String message;

    /** **/
    private String name;

    /**
     *
     * @return
     */
    public String getFrom() {
        return from;
    }

    /**
     *
     * @param from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     *
     * @return
     */
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    /**
     *
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
