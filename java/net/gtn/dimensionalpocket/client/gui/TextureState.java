package net.gtn.dimensionalpocket.client.gui;

public class TextureState {

    private int texX, texY, width, height;

    public TextureState(int texX, int texY, int width, int height) {
        this.texX = texX;
        this.texY = texY;
        this.width = width;
        this.height = height;
    }

    public void setTexX(int texX) {
        this.texX = texX;
    }

    public void addTexX(int amount) {
        this.texX += amount;
    }

    public int getTexX() {
        return texX;
    }

    public void setTexY(int texY) {
        this.texY = texY;
    }

    public void addTexY(int amount) {
        this.texY += amount;
    }

    public int getTexY() {
        return texY;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void addHeight(int amount) {
        this.height += amount;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void addWidth(int amount) {
        this.width += amount;
    }

    public int getWidth() {
        return width;
    }

    public TextureState copy() {
        return new TextureState(texX, texY, width, height);
    }
}
