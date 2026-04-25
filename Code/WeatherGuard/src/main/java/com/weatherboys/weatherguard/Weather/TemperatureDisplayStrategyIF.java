package com.weatherboys.weatherguard.Weather;

public interface TemperatureDisplayStrategyIF {
    public String format(int placeholder);
    public String unitSymbol();
}
