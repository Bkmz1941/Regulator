package ru.regulator.utils;

public class ByteParser {
    public static boolean isTurnOnBit(byte value, int position) {
        return ((value >> position) & 1) == 1;
    }

    public static int getIntValueFromPositions(byte value, int start, int end) {
        value &= ~(1);

        for (int i = 0; i < 8; i++) {
            if (i < end || i > start) {
                value &= (byte) ~(1 << i);
            }
        }
        return value >> 1;
    }
}
