package org.gcnc.calculate.util;

public class MatchUtils {
    public static Integer calculateMatchPoints(Integer goalT1, Integer goalT2) {
        if (goalT1 == null || goalT2 == null) {
            return null;
        }
        if (goalT1 > goalT2) {
            return 3;
        } else if (goalT1.equals(goalT2)) {
            return 1;
        } else {
            return 0;
        }
    }
}
