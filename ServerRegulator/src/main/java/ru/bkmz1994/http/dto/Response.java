package ru.bkmz1994.http.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Response<V> {
    public V data;
    public String message;
}
