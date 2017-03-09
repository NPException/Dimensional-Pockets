package net.gtn.dimensionalpocket.oc.client.gui.interfaces;

public interface ITextAlignment {

    /**
     * @param width Width of the button.
     * @param text The string that is to be drawn.
     */
    public int translateX(int width, String text);

    /**
     * @param height Height of the button.
     * @param text The string that is to be drawn.
     */
    public int translateY(int height, String text);

}
