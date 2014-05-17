package net.gtn.dimensionalpocket.client.utils;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

public class Colour {
    public static final Colour WHITE = new Colour(1.0, 1.0, 1.0, 1.0);
    public static final Colour LIGHTBLUE = new Colour(0.5, 0.5, 1.0, 1.0);
    public static final Colour RED = new Colour(1.0, 0.2, 0.2, 1.0);
    public static final Colour ORANGE = new Colour(0.9, 0.6, 0.2, 1.0);
    public static final Colour YELLOW = new Colour(0.0, 0.0, 0.5, 1.0);
    public static final Colour GREEN = new Colour(0.0, 1.0, 0.0, 1.0);
    public static final Colour DARKGREEN = new Colour(0.0, 0.8, 0.2, 1.0);
    public static final Colour DARKBLUE = new Colour(0.0, 0.0, 0.5, 1.0);
    public static final Colour BLACK = new Colour(0.0, 0.0, 0.0, 1.0);

    public double r, g, b, a;

    public Colour(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Colour(float r, float g, float b) {
        this(r, g, b, 1.0F);
    }

    public Colour(int c) {
        this.a = (c >> 24 & 255) / 255.0F;
        this.r = (c >> 16 & 255) / 255.0F;
        this.g = (c >> 8 & 255) / 255.0F;
        this.b = (c & 255) / 255.0F;
    }

    public int getInt() {
        int val = 0;
        val = val | ((int) (a * 255) << 24);
        val = val | ((int) (r * 255) << 16);
        val = val | ((int) (g * 255) << 8);
        val = val | ((int) (b * 255));

        return val;
    }

    public static int getInt(double r, double g, double b, double a) {
        int val = 0;
        val = val | ((int) (a * 255) << 24);
        val = val | ((int) (r * 255) << 16);
        val = val | ((int) (g * 255) << 8);
        val = val | ((int) (b * 255));

        return val;
    }

    public static Colour getGreyscale(float value, float alpha) {
        return new Colour(value, value, value, alpha);
    }

    /**
     * Returns a colour at an interval along a linear gradient from this to target
     */

    public Colour interpolate(Colour target, double d) {
        double complement = 1 - d;
        return new Colour(this.r * complement + target.r * d, this.g * complement + target.g * d, this.b * complement + target.b * d, this.a * complement + target.a * d);
    }

    public void doGL() {
        GL11.glColor4d(r, g, b, a);
    }

    public static void doGLByInt(int c) {
        double a = (c >> 24 & 255) / 255.0F;
        double r = (c >> 16 & 255) / 255.0F;
        double g = (c >> 8 & 255) / 255.0F;
        double b = (c & 255) / 255.0F;
        GL11.glColor4d(r, g, b, a);
    }

    public Colour withAlpha(double newalpha) {
        return new Colour(this.r, this.g, this.b, newalpha);
    }

    public double[] asArray() {
        return new double[] { r, g, b, a };
    }

    public String hexColour() {
        return hexDigits(r) + hexDigits(g) + hexDigits(b) + (a < 1 ? hexDigits(a) : "");
    }

    public String hexDigits(double x) {
        int y = (int) (x * 255);
        String hexDigits = "0123456789ABCDEF";
        return hexDigits.charAt(y / 16) + "" + hexDigits.charAt(y % 16);
    }

    public Color awtColor() {
        return new Color((float) r, (float) g, (float) b, (float) a);
    }

    public boolean equals(Colour o) {
        return r == o.r && g == o.g && b == o.b && a == o.a;
    }
}