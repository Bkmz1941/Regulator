package ru.regulator;

import ru.regulator.interfaces.Regulator;
import ru.regulator.utils.ByteParser;

import java.util.ArrayList;
import java.util.List;

public class ExampleRegulator implements Regulator<ExampleRegulator> {
    private static ExampleRegulator instance;
    private static float CURRENT_TEMPERATURE = 0.0f;
    private static final int IS_CLEAR_HISTORY_BIT = 7;
    private static final int IS_CHANGE_TEMPERATURE_BIT = 6;
    private static final int IS_RETURN_HISTORY_BIT = 5;
    private static final int START_TEMPERATURE_POSITION_BIT = 4;
    private static final int END_TEMPERATURE_POSITION_BIT = 1;
    private static final float MAX_TEMPERATURE = 1000;
    private static final float MIN_TEMPERATURE = -200;
    private static boolean isClearHistory = false;
    private static boolean isChangeTemperature = false;
    private static boolean isReturnHistory = false;
    private static final List<Float> temperatureHistory = new ArrayList<>();

    private ExampleRegulator() {
        temperatureHistory.add(CURRENT_TEMPERATURE);
    }

    public static void main(String[] args) {
        ExampleRegulator regulator = ExampleRegulator.of();
        List<Float> result = new ArrayList<>();
        int code = regulator.adjustTemp((byte) 0b01111111, 25, result, 0);
//        System.out.println(code);
//        System.out.println(result);
    }

    public static ExampleRegulator of() {
        if (ExampleRegulator.instance == null) ExampleRegulator.instance = new ExampleRegulator();
        return ExampleRegulator.instance;
    }

    public int adjustTemp(byte operation, float inData, List<Float> outData, int offsetOut) {
        try {
            ExampleRegulatorHelper.setupEnvironment(operation);

            if (!ExampleRegulatorHelper.checkValidNewTemperature(inData)) return 3;

            if (isClearHistory) {
                temperatureHistory.clear();
                temperatureHistory.add(ExampleRegulator.CURRENT_TEMPERATURE);
            }

            if (isChangeTemperature) ExampleRegulatorHelper.changeTemperature(inData);

            if (isReturnHistory) ExampleRegulatorHelper.writeToOutData(operation, outData, offsetOut);

            return 0;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return 4;
        }
    }

    public static void destroy() {
        ExampleRegulator.instance = null;
    }


    private static class ExampleRegulatorHelper {
        public static void setupEnvironment(byte operation) {
            ExampleRegulator.isClearHistory = ByteParser.isTurnOnBit(operation, ExampleRegulator.IS_CLEAR_HISTORY_BIT);
            ExampleRegulator.isChangeTemperature = ByteParser.isTurnOnBit(operation, ExampleRegulator.IS_CHANGE_TEMPERATURE_BIT);
            ExampleRegulator.isReturnHistory = ByteParser.isTurnOnBit(operation, ExampleRegulator.IS_RETURN_HISTORY_BIT);
        }

        public static boolean checkValidNewTemperature(float inData) {
            if (inData > ExampleRegulator.MAX_TEMPERATURE) return false;
            if (inData < ExampleRegulator.MIN_TEMPERATURE) return false;

            return true;
        }

        public static void writeToOutData(byte operation, List<Float> outData, int offsetOut) {
            int limit = ByteParser.getIntValueFromPositions(operation, START_TEMPERATURE_POSITION_BIT, END_TEMPERATURE_POSITION_BIT);
            int recordsSize = temperatureHistory.size() - offsetOut;
            int max = recordsSize - limit;
            if (max < 0) max = 0;

            for (int i = recordsSize - 1; i >= max; i--) {
                outData.add(temperatureHistory.get(i));
            }
        }

        public static void changeTemperature(float toTemp) {
            int tempPositive = toTemp > 0 ? 1 : -1;
            boolean isUpDirection = tempPositive > 0
                    ? ExampleRegulator.CURRENT_TEMPERATURE < toTemp
                    : ExampleRegulator.CURRENT_TEMPERATURE > toTemp;

            while (ExampleRegulator.CURRENT_TEMPERATURE != toTemp) {
                float addValue = (float) (3 + (Math.random() * 6)) * tempPositive;

                if (isUpDirection) {
                    ExampleRegulator.CURRENT_TEMPERATURE += addValue;
                    if ((ExampleRegulator.CURRENT_TEMPERATURE * tempPositive) > (toTemp * tempPositive)) {
                        ExampleRegulator.CURRENT_TEMPERATURE = toTemp;
                    }
                } else {
                    ExampleRegulator.CURRENT_TEMPERATURE -= addValue;
                    if ((ExampleRegulator.CURRENT_TEMPERATURE * tempPositive) < (toTemp * tempPositive)) {
                        ExampleRegulator.CURRENT_TEMPERATURE = toTemp;
                    }
                }

                ExampleRegulator.temperatureHistory.add(ExampleRegulator.CURRENT_TEMPERATURE);
            }
        }
    }
}