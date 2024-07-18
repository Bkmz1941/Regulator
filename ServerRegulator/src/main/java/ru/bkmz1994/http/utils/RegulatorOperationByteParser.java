package ru.bkmz1994.http.utils;

public class RegulatorOperationByteParser {
    public static byte getTemperatureHistoryOperation(int limit) {
        byte limitByte = (byte) limit;
        limitByte = (byte) (limitByte << 1);
        limitByte = (byte) (limitByte | (1));
        limitByte = (byte) (limitByte | (1 << 5));
        return limitByte;
    }
}
