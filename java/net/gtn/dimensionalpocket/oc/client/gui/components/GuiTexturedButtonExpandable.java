package net.gtn.dimensionalpocket.oc.client.gui.components;

public abstract class GuiTexturedButtonExpandable<T extends GuiTexturedButtonExpandable> extends GuiTexturedButton<T> {

    protected final int textureWidth, textureHeight,
            borderTop, borderBottom,
            borderLeft, borderRight,
            borderDiffWidth, borderDiffHeight,
            halfButtonWidth, halfButtonHeight,
            internalWidth, internalHeight,
            xPasses, yPasses,
            xRemainder, yRemainder,
            fillerX, fillerY;

    public GuiTexturedButtonExpandable(int x, int y, int u, int v, int buttonWidth, int buttonHeight, int textureWidth, int textureHeight, int borderTop, int borderBottom, int borderLeft, int borderRight) {
        super(x, y, u, v, buttonWidth, buttonHeight);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.borderTop = borderTop;
        this.borderBottom = borderBottom;
        this.borderLeft = borderLeft;
        this.borderRight = borderRight;

        this.borderDiffWidth = textureWidth - borderRight - borderLeft;
        this.borderDiffHeight = textureHeight - borderBottom - borderTop;

        this.halfButtonWidth = Math.min(textureWidth / 2, buttonWidth / 2);
        this.halfButtonHeight = Math.min(textureHeight / 2, buttonHeight / 2);

        this.internalWidth = Math.max(width - (halfButtonWidth * 2), 0);
        this.internalHeight = Math.max(height - (halfButtonHeight * 2), 0);

        this.xPasses = borderDiffWidth == 0 ? 0 : internalWidth / borderDiffWidth;
        this.yPasses = borderDiffHeight == 0 ? 0 : internalHeight / borderDiffHeight;

        this.xRemainder = internalWidth - (xPasses * borderDiffWidth);
        this.yRemainder = internalHeight - (yPasses * borderDiffHeight);

        this.fillerX = Math.min(borderDiffWidth, internalWidth);
        this.fillerY = Math.min(borderDiffHeight, internalHeight);
    }

    @Override
    public void renderBackground(int mouseX, int mouseY) {
        if (!isVisible())
            return;

        int tempU = u;
        int tempV = v;
        int passLevel = getPassLevel(mouseX, mouseY);

        if (passLevel > 0) {
            tempU += getTextureXShift(passLevel);
            tempV += getTextureYShift(passLevel);
        }
        timedOutClick();
        bindTexture();

        // Top Left
        drawTexturedModalRect(x, y, tempU, tempV, halfButtonWidth, halfButtonHeight);
        // Top Right
        drawTexturedModalRect(x + width - halfButtonWidth, y, tempU + textureWidth - halfButtonWidth, tempV, halfButtonWidth, halfButtonHeight);
        // Bottom Left
        drawTexturedModalRect(x, y + height - halfButtonHeight, tempU, tempV + textureHeight - halfButtonHeight, halfButtonWidth, halfButtonHeight);
        // Bottom Right
        drawTexturedModalRect(x + width - halfButtonWidth, y + height - halfButtonHeight, tempU + textureWidth - halfButtonWidth, tempV + textureHeight - halfButtonHeight, halfButtonWidth, halfButtonHeight);

        // Border rows
        for (int i = 0; i <= xPasses; i++) {
            int offset = halfButtonWidth + borderDiffWidth * i;
            if (i < xPasses) {
                drawTexturedModalRect(x + offset, y, tempU + borderLeft, tempV, borderDiffWidth, halfButtonHeight);
                drawTexturedModalRect(x + offset, y + height - halfButtonHeight, tempU + borderLeft, tempV + textureHeight - halfButtonHeight, borderDiffWidth, halfButtonHeight);
            } else if (xRemainder > 0) {
                drawTexturedModalRect(x + offset, y, tempU + borderLeft, tempV, xRemainder, halfButtonHeight);
                drawTexturedModalRect(x + offset, y + height - halfButtonHeight, tempU + borderLeft, tempV + textureHeight - halfButtonHeight, xRemainder, halfButtonHeight);
            }
        }

        // Border Columns
        for (int i = 0; i <= yPasses; i++) {
            int offset = halfButtonHeight + borderDiffHeight * i;
            if (i < yPasses) {
                drawTexturedModalRect(x, y + offset, tempU, tempV + borderTop, halfButtonWidth, borderDiffHeight);
                drawTexturedModalRect(x + width - halfButtonWidth, y + offset, tempU + textureWidth - halfButtonWidth, tempV + borderTop, halfButtonWidth, borderDiffHeight);
            } else if (yRemainder > 0) {
                drawTexturedModalRect(x, y + offset, tempU, tempV + borderTop, halfButtonWidth, yRemainder);
                drawTexturedModalRect(x + width - halfButtonWidth, y + offset, tempU + textureWidth - halfButtonWidth, tempV + borderTop, halfButtonWidth, yRemainder);
            }
        }

        // Filler
        for (int xPass = 0; xPass <= xPasses; xPass++) {
            int xOffset = halfButtonWidth + fillerX * xPass;
            for (int yPass = 0; yPass <= yPasses; yPass++) {
                int yOffset = halfButtonHeight + fillerY * yPass;
                drawTexturedModalRect(x + xOffset, y + yOffset, tempU + borderLeft, tempV + borderTop, xPass == xPasses ? xRemainder : fillerX, yPass == yPasses ? yRemainder : fillerY);
            }
        }
    }

}
