package net.gtn.dimensionalpocket.oc.common.utils;

public class MathHelper {

    public static int wrapInt(int value, int max) {
        return wrapInt(value, 0, max);
    }

    public static int wrapInt(int value, int min, int max) {
        return value < min ? max : value > max ? min : value;
    }

    public static float expandAwayFrom(float value, float expansion) {
        return expandAwayFrom(value, 0, expansion);
    }

    public static float expandAwayFrom(float value, float awayFrom, float expansion) {
        return value < awayFrom ? value - expansion : value > awayFrom ? value + expansion : value;
    }

    public static boolean withinValues(float value, float lowerBound, float upperBound) {
        return lowerBound < value && value < upperBound;
    }

    public static boolean withinRange(double value, double target, double tolerance) {
        return (target - tolerance) <= value && value <= (target + tolerance);
    }

    public static boolean withinRange(double value, double tolerance) {
        return withinRange(value, 0.0D, tolerance);
    }

    public static boolean withinRange(float value, float target, float tolerance) {
        return value >= (target - tolerance) && value <= (target + tolerance);
    }

    public static boolean withinRange(int value, int target, int tolerance) {
        return value >= (target - tolerance) && value <= (target + tolerance);
    }

    public static double clipDouble(double value, double max) {
        return clipDouble(value, 0, max);
    }

    public static double clipDouble(double value, double min, double max) {
        return value > max ? max : value < min ? min : value;
    }

    public static int clipInt(int value, int max) {
        return clipInt(value, 0, max);
    }

    public static int clipInt(int value, int min, int max) {
        return value > max ? max : value < min ? min : value;
    }

    public static double interpolate(double a, double b, double d) {
        return a + (b - a) * d;
    }

    public static float interpolate(float a, float b, float d) {
        return a + (b - a) * d;
    }

    public static enum RoundingMethod {
        FLOOR, CEILING, ROUND
    }

}
