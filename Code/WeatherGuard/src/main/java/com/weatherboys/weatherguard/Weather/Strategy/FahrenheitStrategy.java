package com.weatherboys.weatherguard.Weather.Strategy;

public class FahrenheitStrategy implements TemperatureDisplayStrategyIF {
    @Override
    public String unitSymbol() {
        return "";
    }
    @Override
    public String format(int tempf, int tempC) {

        //assuming that farenheit strategy returns farenheit


        return "";
    }
}
