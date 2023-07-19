package dev.tripdraw.exception.trip;

import dev.tripdraw.exception.common.BaseException;
import dev.tripdraw.exception.common.ExceptionType;

public class TripException extends BaseException {

    public TripException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
