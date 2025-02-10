package com.soundbrew.soundbrew.handler.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResourceOwnershipException extends RuntimeException {
    public ResourceOwnershipException(String message) {
        super(message);
    }
}
