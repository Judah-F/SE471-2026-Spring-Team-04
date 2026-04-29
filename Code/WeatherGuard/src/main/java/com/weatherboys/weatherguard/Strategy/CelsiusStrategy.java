package com.weatherboys.weatherguard.Strategy;

public class CelsiusStrategy implements TemperatureDisplayStrategyIF {

    @Override
    public String format(int tempf, int tempc) {

        return tempc + "°C";
    }

    @Override
    public String unitSymbol() {
        return "C";
    }
}
