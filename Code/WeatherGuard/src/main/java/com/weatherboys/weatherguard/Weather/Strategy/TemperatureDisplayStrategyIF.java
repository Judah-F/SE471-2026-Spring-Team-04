package com.weatherboys.weatherguard.Weather.Strategy;

public interface TemperatureDisplayStrategyIF {
    public String format(int tempF, int tempC);
    public String unitSymbol();

}
