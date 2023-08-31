package dev.tripdraw.trip.exception;

import dev.tripdraw.exception.common.BaseException;
import dev.tripdraw.exception.common.ExceptionType;

public class TripException extends BaseException {

    public TripException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
