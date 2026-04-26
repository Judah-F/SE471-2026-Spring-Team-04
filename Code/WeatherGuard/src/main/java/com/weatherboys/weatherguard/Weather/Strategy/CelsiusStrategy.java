package com.weatherboys.weatherguard.Weather.Strategy;

public class CelsiusStrategy implements TemperatureDisplayStrategyIF {

    @Override
    public String format(int tempf, int tempc) {

        return "C";
    }

    @Override
    public String unitSymbol() {
        return "";
    }
}
