package edu.ithaca.dragonlab.ckc.util;

/**
 * Created by tdragon on 5/3/17.
 */
public class DataUtil {
    public static float OK_FLOAT_MARGIN = (float) 0.001;

    public static boolean equalsDoubles(double one, double two) {
        return (Math.abs(one - two) < OK_FLOAT_MARGIN);
    }
}
