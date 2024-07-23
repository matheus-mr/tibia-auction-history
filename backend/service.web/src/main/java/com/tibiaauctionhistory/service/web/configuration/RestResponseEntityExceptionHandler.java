package com.tibiaauctionhistory.service.web.configuration;

import com.google.common.base.Throwables;
import com.tibiaauctionhistory.module.auctionsearch.exception.AuctionSearchNotFoundException;
import com.tibiaauctionhistory.module.auctionsearch.exception.InvalidSortDirectionException;
import com.tibiaauctionhistory.module.auctionsearch.exception.InvalidSortableFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            IllegalStateException.class,
            InvalidSortableFieldException.class,
            InvalidSortDirectionException.class
    })
    public ResponseEntity<String> handleBadRequest(RuntimeException ex, WebRequest request) {
        final Throwable rootCause = Throwables.getRootCause(ex);
        final String msg;
        if (rootCause instanceof InvalidSortableFieldException || rootCause instanceof InvalidSortDirectionException){
            msg = rootCause.getMessage();
        } else {
            msg = ex.getMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    @ExceptionHandler(value = { AuctionSearchNotFoundException.class })
    public ResponseEntity<String> handleNotFound(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
