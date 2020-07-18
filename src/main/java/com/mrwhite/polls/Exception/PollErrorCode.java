package com.mrwhite.polls.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum PollErrorCode {
    NOT_FOUND("NOT_FOUND", HttpStatus.NOT_FOUND), BAD_REQUEST("BAD_REQUEST", HttpStatus.BAD_REQUEST), UNKNOWN("UNKNOWN"),
    METHOD_ARGUMENT_NOT_VALID("UNPROCESSABLE_ENTITY", HttpStatus.UNPROCESSABLE_ENTITY), INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    DOWNSTREAM_ERROR("DOWNSTREAM_ERROR", HttpStatus.INTERNAL_SERVER_ERROR), DB_ERROR("DB_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    RESPONSE_NULL("RESPONSE_NULL", HttpStatus.INTERNAL_SERVER_ERROR), UNPROCESSABLE_ENTITY("UNPROCESSABLE_ENTITY", HttpStatus.UNPROCESSABLE_ENTITY),
    UNAUTHORISED("UNAUTHORISED", HttpStatus.UNAUTHORIZED);

    @Getter
    private final String name;

    @Getter
    private Integer errorCode;

    @Getter
    private HttpStatus httpStatus;

    PollErrorCode(String name, Integer errorCode) {
        this.name = name;
        this.errorCode = errorCode;
    }

    PollErrorCode(String name, HttpStatus httpStatus) {
        this.name = name;
        this.httpStatus = httpStatus;
    }

    PollErrorCode(String name, HttpStatus httpStatus, Integer errorCode) {
        this.name = name;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }


    PollErrorCode(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
