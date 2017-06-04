package ng.joey.lib.java.error;

import ng.joey.lib.java.util.Value;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Joey Dalughut on 8/9/16 at 2:32 PM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class Exception extends java.lang.Exception {

    public static final class Mappings {
        public static final int BadRequestException = 400;
        public static final int UnauthorizedException = 401;
        public static final int ForbiddenException = 403;
        public static final int NotFoundException = 404;
        public static final int ConflictException = 409;
        public static final int InternalServerErrorException = 500;
        public static final int ServiceUnvailableException = 503;

        public static final int InternetUnavailableException = -1;
        public static final int TimeoutException = -2;
    }

    protected int statusCode = 0;

    public Exception(String statusMessage){
        super(statusMessage);
    }

    public Exception(int statusCode, String statusMessage) {
        super(statusMessage);
        this.statusCode = statusCode;
    }

    public Exception(int statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public Exception(int statusCode, String statusMessage, Throwable cause) {
        super(statusMessage, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public Map<String, String> getHeaders() {
        return null;
    }


    public static enum ServiceException {
        LitigyException(0),
        BadRequestException(Mappings.BadRequestException),
        UnauthorizedException(Mappings.UnauthorizedException),
        ForbiddenException(Mappings.ForbiddenException),
        NotFoundException(Mappings.NotFoundException),
        ConflictException(Mappings.ConflictException),
        InternalServerErrorException(Mappings.InternalServerErrorException),
        ServiceUnavailableException(Mappings.ServiceUnvailableException),
        InternetUnavailableException(Mappings.InternetUnavailableException),
        TimeoutException(Mappings.TimeoutException);

        int statusCode;
        String statusMessage;

        ServiceException(int statusCode){
            this.statusCode = statusCode;
        }

        public ServiceException setStatusMessage(String statusMessage){
            this.statusMessage = statusMessage; return this;
        }

        public int getStatusCode(){
            return this.statusCode;
        }

        public String getStatusMessage(){
            return this.statusMessage;
        }

        /**
         * An Enum class to represent our errors. I used an enum class so I could conveniently have the errors in the exception
         * and also switch through them
         * @param statusCode the status or error code returned by the server
         * @return an instance of {@link ServiceException} with reference to the original error code and the error message
         */
        public static ServiceException toServiceException(int statusCode){
            switch (statusCode){
                case Mappings.BadRequestException: return BadRequestException;
                case Mappings.UnauthorizedException: return UnauthorizedException;
                case Mappings.ForbiddenException: return ForbiddenException;
                case Mappings.NotFoundException: return NotFoundException;
                case Mappings.ConflictException: return ConflictException;
                case Mappings.InternalServerErrorException: return InternalServerErrorException;
                case Mappings.ServiceUnvailableException: return ServiceUnavailableException;
                case Mappings.InternetUnavailableException: return InternetUnavailableException;
                case Mappings.TimeoutException: return TimeoutException;
                default:
                    return LitigyException;
            }
        }

    }

    /**
     * Consume an instance of {@link IOException} and map it to a corresponding {@link Exception}
     *
     * Errors from the server are always returned as {@link IOException}'s with different error codes, so to differenciate them it's
     * best to get the error code from the exception and create a new kind of exception which maps to the kind of error using the error
     * code. For example, error code 404 is known as the error code for "Not Found" or when something is not found. So we can get our IOException
     * and get the error code, then create a new NotFoundException from it just so we know the exact error.
     *
     * @param e the {@link IOException} to be mapped
     * @return an instance of {@link Exception} which contains the error code and enum representing the exact error that was received as an {@link IOException}
     * Use the method toServiceException() to get the exact error, it's an enum value so that you can use it in a switch and case statement.
     */
    public static Exception consumeIOException(IOException e){
        String message = e.getMessage();
        if(Value.IS.emptyValue(message)) return new Exception(0, "void");
        if(message.startsWith("Unable to resolve host"))
            return new Exception(Exception.ServiceException.InternetUnavailableException.getStatusCode(), message);
        Integer statusCode = Value.FIND.integerValueOccuringAt(message, 0);
        if(Value.IS.nullValue(statusCode)) return new Exception(0, "message");
        return new Exception(statusCode, message);
    }

    /**
     * Create an instance of {@link ServiceException} from this {@link Exception}
     *
     * {@link ServiceException} is an enum class that contains both an error code and error message. That way we can
     * still have the details of the error and use them in switch-case statements
     *
     * @return the created instance of {@link ServiceException}
     */
    public ServiceException toServiceException(){
        return Exception.ServiceException.toServiceException(getStatusCode());
    }


}
