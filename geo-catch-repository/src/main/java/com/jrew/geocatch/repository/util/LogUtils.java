package com.jrew.geocatch.repository.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 9/20/13
 * Time: 2:11 PM
 *
 * Used for Log utils methods
 */
public class LogUtils {

    /**
     * Get methods arguments from provided {@link JoinPoint}.
     *
     * @param joinPoint
     * @return String representation of method arguments
     */
    public static String getMethodArguments(JoinPoint joinPoint) {
        StringBuilder message = new StringBuilder();
        message.append(" ( ");

        Object[] arguments = joinPoint.getArgs();
        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            if (argument != null) {
                message.append(argument.getClass().getName())
                       .append(" : ")
                       .append(argument.toString());

                // Do not apply ',' to last argument
                if (i < arguments.length - 1) {
                    try {
                        message.append(", ");
                    } catch(NullPointerException e) {
                        message.append("null, ");
                    }
                }
            }
        }
        message.append (" );");

        return message.toString();
    }

    /**
     * Gets method name from provided {@link JoinPoint}.
     *
     * @param joinPoint
     * @return
     */
    public static String getMethodName(JoinPoint joinPoint) {
        Signature methodSignature = joinPoint.getSignature();

        StringBuilder message = new StringBuilder();
        message.append(joinPoint.getTarget().getClass())
               .append('#')
               .append(methodSignature.getName());

        return  message.toString();
    }

    /**
     * Gets logger
     *
     * @param joinPoint
     * @return
     */
    public static Logger getLogger(JoinPoint joinPoint) {
        return LogManager.getLogger(joinPoint.getTarget().getClass());
    }

    /**
     * Gets current date time stamp
     *
     * @param dateFormat
     * @return
     */
    public static String getDateTimeStamp(SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date());
    }

}
