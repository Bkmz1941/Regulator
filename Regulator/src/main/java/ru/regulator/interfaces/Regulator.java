package ru.regulator.interfaces;

import java.util.List;

public interface Regulator<T> {
    int adjustTemp(byte operation, float inData, List<Float> outData, int offsetOut);
}
