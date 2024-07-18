package ru.bkmz1994.http.exceptions.classes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RegulatorException extends RuntimeException {
    public RegulatorException(String message) {
        super(message);
    }
}
