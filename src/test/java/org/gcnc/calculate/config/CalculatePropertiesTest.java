package org.gcnc.calculate.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatePropertiesTest {

    private final String URL = "url";
    private final String RANKING = "ranking";
    private final String CALENDAR = "cal";
    @Test
    public void testCalculateProperties() {
        CalculateProperties properties = new CalculateProperties();
        properties.setBaseUrl(URL);
        properties.setRankingSuffix(RANKING);
        properties.setCalendarSuffix(CALENDAR);

        assertEquals(URL, properties.getBaseUrl());
        assertEquals(RANKING, properties.getRankingSuffix());
        assertEquals(CALENDAR, properties.getCalendarSuffix());
    }
}