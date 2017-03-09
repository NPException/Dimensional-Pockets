package net.gtn.dimensionalpocket.oc.client.lib;

import org.lwjgl.opengl.GL11;

/**
 * A class representing an RGBA colour and various helper functions. Mainly to improve readability elsewhere.
 *
 * @author MachineMuse
 */
public class Colour implements Cloneable {
    public static final Colour WHITE = new Colour(1.0, 1.0, 1.0, 1.0);

    public static final Colour LIGHT_BLUE = new Colour(0.5, 0.5, 1.0, 1.0);
    public static final Colour BLUE = new Colour(0.0, 0.0, 1.0, 1.0);
    public static final Colour DARK_BLUE = new Colour(0.0, 0.0, 0.5, 1.0);

    public static final Colour LIGHT_RED = new Colour(1.0, 0.5, 0.5, 1.0);
    public static final Colour RED = new Colour(1.0, 0.2, 0.2, 1.0);
    public static final Colour DARK_RED = new Colour(0.5, 0.0, 0.0, 1.0);

    public static final Colour LIGHT_GREEN = new Colour(0.5, 1.0, 0.5, 1.0);
    public static final Colour GREEN = new Colour(0.0, 1.0, 0.0, 1.0);

    public static final Colour YELLOW = new Colour(1.0, 1.0, 0.0, 1.0);
    public static final Colour CYAN = new Colour(0.0, 1.0, 1.0, 1.0);
    public static final Colour PINK = new Colour(1.0, 0.0, 1.0, 1.0);

    public static final Colour NEON_BLUE = new Colour(0.4, 0.8, 1.0, 1.0);
    public static final Colour ORANGE = new Colour(0.9, 0.6, 0.2, 1.0);

    public static final Colour PURPLE = new Colour(0.5, 0.0, 1.0, 1.0);

    public static final Colour LIGHT_GREY = new Colour(0.8, 0.8, 0.8, 1.0);
    public static final Colour GREY = new Colour(0.5, 0.5, 0.5, 1.0);
    public static final Colour DARK_GREY = new Colour(0.2, 0.2, 0.2, 1.0);

    public static final Colour BLACK = new Colour(0.0, 0.0, 0.0, 1.0);

    public double r, g, b, a;

    public Colour(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Colour(int colour) {
        this.a = (colour >> 24 & 255) / 255.0F;
        this.r = (colour >> 16 & 255) / 255.0F;
        this.g = (colour >> 8 & 255) / 255.0F;
        this.b = (colour & 255) / 255.0F;

    }

    public Colour add(Colour o) {
        r += o.r;
        g += o.g;
        b += o.b;
        a += o.a;
        return this;
    }

    public Colour sub(Colour o) {
        r -= o.r;
        g -= o.g;
        b -= o.b;
        a -= o.a;
        return this;
    }

    public Colour invert() {
        r = 1.0 - r;
        g = 1.0 - g;
        b = 1.0 - b;
        a = 1.0 - a;
        return this;
    }

    public Colour multiply(Colour o) {
        r *= o.r;
        g *= o.g;
        b *= o.b;
        a *= o.a;
        return this;
    }

    public Colour multiply(double num) {
        r *= num;
        g *= num;
        b *= num;
        a *= num;
        return this;
    }

    public Colour copy() {
        return clone();
    }

    @Override
    protected final Colour clone() {
        try {
            return (Colour) super.clone();
        } catch (CloneNotSupportedException ignored) {
        }
        return new Colour(r, g, b, a);
    }

    public int getInt() {
        return getInt(r, g, b, a);
    }

    public void doGLColor4() {
        GL11.glColor4d(r, g, b, a);
    }

    public boolean equals(Colour o) {
        return o != null && r == o.r && g == o.g && b == o.b && a == o.a;
    }

    public static int getInt(double r, double g, double b, double a) {
        int temp = 0;
        temp = temp | ((int) (a * 255) << 24);
        temp = temp | ((int) (r * 255) << 16);
        temp = temp | ((int) (g * 255) << 8);
        temp = temp | ((int) (b * 255));
        return temp;
    }
}