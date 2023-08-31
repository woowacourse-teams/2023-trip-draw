package dev.tripdraw.trip.exception;

import dev.tripdraw.common.exception.BaseException;
import dev.tripdraw.common.exception.ExceptionType;

public class TripException extends BaseException {

    public TripException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
