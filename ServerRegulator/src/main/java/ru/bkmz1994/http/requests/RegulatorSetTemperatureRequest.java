package ru.bkmz1994.http.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegulatorSetTemperatureRequest {
    @JsonProperty("temperature")
    private float temperature;
    @JsonProperty("limit")
    private int limit;
    @JsonProperty("offset")
    private int offset;
}
