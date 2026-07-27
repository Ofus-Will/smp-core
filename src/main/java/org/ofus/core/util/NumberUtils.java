package org.ofus.core.util;

public class NumberUtils {

    public static boolean isNumber(String input, boolean positive) {
        try {
            int num = Integer.parseInt(input);
            return num >= 0 || !positive;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
