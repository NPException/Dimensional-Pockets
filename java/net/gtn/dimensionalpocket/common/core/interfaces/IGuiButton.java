package net.gtn.dimensionalpocket.common.core.interfaces;

public interface IGuiButton {

    public boolean onClick(int mouseX, int mouseY, int mouseClick);

    public boolean canClick(int mouseX, int mouseY);

    public boolean isHoveringOver(int mouseX, int mouseY);

    public void render(int mouseX, int mouseY);

}
