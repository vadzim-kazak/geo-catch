package com.jrew.geocatch.repository.aop;

import com.jrew.geocatch.repository.util.LogUtils;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 9/16/13
 * Time: 7:28 PM
 *
 * Used for logging purposes.
 */

@Aspect
public class AopLogger {

    /** **/
    private final SimpleDateFormat dateFormat;

    /**
     * Constructor
     *
     * @param dateFormatPattern
     */
    public AopLogger(String dateFormatPattern) {
        dateFormat = new SimpleDateFormat(dateFormatPattern);
    }

    /**
     * Logs method invocation time
     *
     * @param pjp
     * @throws Throwable
     */
    @Around("execution(* com.jrew.geocatch.repository..*.*(..))")
    public Object logMethodInvocationTime(ProceedingJoinPoint pjp) throws Throwable {

        Logger logger = LogUtils.getLogger(pjp);

        logMethodInvocation(pjp, logger);

        long timeBefore = System.currentTimeMillis();
        Object result = pjp.proceed();
        long timeAfter = System.currentTimeMillis();

        logMethodInvocationTime(pjp, logger, timeAfter - timeBefore);

        logMethodReturning(pjp, result, logger);

        return result;
    }

    /**
     * Logs exception throwing
     *
     * @param joinPoint
     * @param exception
     * @throws Throwable
     */
    @AfterThrowing(
            pointcut = "execution(* com.jrew.geocatch.repository..*.*(..))",
            throwing = "exception"
    )
    public void logExceptionThrowing(JoinPoint joinPoint, Throwable exception) throws Throwable {
        Logger logger = LogUtils.getLogger(joinPoint);

        StringBuilder message = new StringBuilder();
        message.append(LogUtils.getDateTimeStamp(dateFormat))
                .append(' ')
                .append(exception.getMessage());

        logger.error(message.toString(), exception);
        throw exception;
    }

    /**
     *
     * Logs method invocation event
     *
     * @param joinPoint
     * @param logger
     */
    public void logMethodInvocation(JoinPoint joinPoint, Logger logger) {

        StringBuilder message = new StringBuilder();
        message.append(LogUtils.getDateTimeStamp(dateFormat))
               .append(" Called: ")
               .append(LogUtils.getMethodName(joinPoint));

        logger.info(message.toString());

        logger.debug(LogUtils.getMethodArguments(joinPoint));
    }

    /**
     * Logs methods returning value
     *
     * @param joinPoint
     * @param retVal
     * @param logger
     */
    public void logMethodReturning(JoinPoint joinPoint, Object retVal, Logger logger) {

        StringBuilder message = new StringBuilder();
        message.append(LogUtils.getDateTimeStamp(dateFormat))
                .append(" Finished: ")
                .append(LogUtils.getMethodName(joinPoint));

        logger.info(message.toString());

        String returnValue = null;
        if (retVal != null) {
            returnValue = retVal.toString();
        }
        logger.debug(" ( return value: " + returnValue + " );");
    }

    /**
     * Logs methods invocation time
     *
     * @param joinPoint
     * @param logger
     * @param timeDelta
     */
    public void logMethodInvocationTime(JoinPoint joinPoint, Logger logger, long timeDelta) {
        StringBuilder message = new StringBuilder();
        message.append("Method ")
                .append(LogUtils.getMethodName(joinPoint))
                .append(" execution time: ")
                .append(timeDelta)
                .append(" ms ");

        logger.trace(message);
    }
}
