package com.mercandalli.core.weather;


import org.junit.Assert;
import org.junit.Test;

public class WeatherConstTest {

    @Test
    public void getAirPollutionUrl() {
        Assert.assertEquals(
                "http://api.openweathermap.org/pollution/v1/co/48.8321071,2.2384558/2018-02-26T11:00:00Z.json?appid=886705b4c1182eb1c69f28eb8c520e20",
                WeatherConst.getAirPollutionUrl("2018-02-26T11:00:00Z"));
    }
}
