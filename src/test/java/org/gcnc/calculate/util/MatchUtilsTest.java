package org.gcnc.calculate.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchUtilsTest {

    @Test
    void returnsNullWhenEitherGoalIsNull() {
        assertNull(MatchUtils.calculateMatchPoints(null, 1));
        assertNull(MatchUtils.calculateMatchPoints(1, null));
        assertNull(MatchUtils.calculateMatchPoints(null, null));
    }

    @Test
    void returns3WhenTeam1Wins() {
        assertEquals(3, MatchUtils.calculateMatchPoints(2, 1));
        assertEquals(3, MatchUtils.calculateMatchPoints(0, -1));
    }

    @Test
    void returns1WhenDraw() {
        assertEquals(1, MatchUtils.calculateMatchPoints(0, 0));
        assertEquals(1, MatchUtils.calculateMatchPoints(Integer.valueOf(5), Integer.valueOf(5)));
    }

    @Test
    void returns0WhenTeam2Wins() {
        assertEquals(0, MatchUtils.calculateMatchPoints(1, 2));
        assertEquals(0, MatchUtils.calculateMatchPoints(-2, -1));
    }
}