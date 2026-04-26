package com.weatherboys.weatherguard.Weather.Strategy;

public class FahrenheitStrategy implements TemperatureDisplayStrategyIF {
    @Override
    public String unitSymbol() {
        return "°F";
    }
    @Override
    public String format(int tempf, int tempC) {

        return tempf+"°F";
    }
}
