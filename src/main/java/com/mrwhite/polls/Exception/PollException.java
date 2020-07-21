package com.mrwhite.polls.Exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ToString
public class PollException extends Exception {
    private static final long serialVersionUID = 1L;
    @Getter
    private PollErrorCode code;

    public PollException(PollErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public PollException(PollErrorCode code, String message, Throwable e) {
        super(message);
        this.setStackTrace(e.getStackTrace());
        this.code = code;
    }

    public PollException(PollErrorCode code, Throwable e) {
        super(e.getMessage());
        this.setStackTrace(e.getStackTrace());
        this.code = code;
    }
}
