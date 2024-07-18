package ru.bkmz1994.http.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class RegulatorModel {
    public float temperature;
    public List<Float> history;
    public int offset;
    public int limit;
}
