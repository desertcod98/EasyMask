package it.volta.ts.easymask.util;

import java.math.BigDecimal;

public class MathUtil {
    public static float roundDown(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_DOWN);
        return bd.floatValue();
    }
}
