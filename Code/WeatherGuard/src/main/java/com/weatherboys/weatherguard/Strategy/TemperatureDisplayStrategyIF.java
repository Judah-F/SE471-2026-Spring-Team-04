package com.weatherboys.weatherguard.Strategy;

public interface TemperatureDisplayStrategyIF {
    public String format(int tempF, int tempC);
    public String unitSymbol();

}
