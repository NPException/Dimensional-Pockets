package net.gtn.dimensionalpocket.oc.client.gui;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.oc.client.gui.components.GuiWidget;
import net.gtn.dimensionalpocket.oc.client.gui.interfaces.IGuiRenderHandler;
import net.gtn.dimensionalpocket.oc.client.lib.Colour;
import net.gtn.dimensionalpocket.oc.common.core.network.MessageGuiNotify;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public abstract class GuiContainerAbstract extends GuiContainer implements IGuiRenderHandler {
    public EntityPlayer player;
    public int middleX, middleY;
    public ResourceLocation mainTexture;

    private ArrayList<GuiWidget> buttonList;
    private int id = 0;

    protected GuiContainerAbstract(EntityPlayer player) {
        this(player, new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return true;
            }
        });
    }

    public GuiContainerAbstract(EntityPlayer player, Container container) {
        super(container);
        this.player = player;
        buttonList = new ArrayList<>();
    }

    public GuiContainerAbstract setMainTexture(ResourceLocation mainTexture) {
        this.mainTexture = mainTexture;
        return this;
    }

    public ArrayList<GuiWidget> getButtonList() {
        return buttonList;
    }

    public void bindTexture() {
        if (mainTexture != null)
            bindTexture(mainTexture);
    }

    public void bindTexture(ResourceLocation texture) {
        mc.renderEngine.bindTexture(texture);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        id = 0;

        middleX = (width - xSize) / 2;
        middleY = (height - ySize) / 2;
    }

    public int getNextID() {
        return id++;
    }

    /**
     * Pass through for super.buttonList.
     */
    public int addDefaultButton(GuiButton button) {
        super.buttonList.add(button);
        return id;
    }

    public int addButton(GuiWidget widget) {
        buttonList.add(widget.setID(getNextID()).setRenderHandler(this));
        return id;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        bindTexture();
        for (GuiWidget widget : buttonList)
            widget.renderBackground(mouseX, mouseY);
        for (GuiWidget widget : buttonList)
            widget.renderForeground(mouseX, mouseY);
        bindTexture();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int t) {
        for (GuiWidget widget : buttonList)
            if (widget.canClick(mouseX, mouseY)) {
                widget.onClick(mouseX, mouseY, t);
                onActionPerformed(widget, t);
                //break;
            }
    }

    @Override
    public GuiScreen getGuiScreen() {
        return this;
    }

    @Override
    public void renderTooltip(ItemStack itemStack, int x, int y) {
        if (itemStack != null)
            renderToolTip(itemStack, x, y);
    }

    @Override
    public void renderHoveringText(List list, int x, int y, FontRenderer font) {
        drawHoveringText(list, x, y, font);
    }

    protected void drawCentredText(int xOffset, int yOffset, String text) {
        drawCentredText(xOffset, yOffset, text, Colour.WHITE);
    }

    protected void drawCentredText(int xOffset, int yOffset, String text, Colour colour) {
        fontRendererObj.drawString(text, ((xSize - fontRendererObj.getStringWidth(text)) / 2) + xOffset, (ySize) / 2 + yOffset, colour.getInt());
    }

    /**
     * Used to send a message to the server, notifying it of certain changes.
     * This using an embedded network system, so you have nothing to worry about transport.
     * The only thing you have to do is on the Container on the server, you need to implement {@link net.gtn.dimensionalpocket.oc.client.gui.interfaces.IGuiMessageHandler}
     * Then {@code onClientClick} will be called with the same ID and process that you sent, the only difference, of course, is that it's on the server, as GUIs are on the client.
     * This should give you an easy way to transport simple data based on what the client has done.
     * Be warned, this could be exploited, so keep that in mind, but then so can the entire Minecraft GUI system.
     *
     * @param ID      ID of the message.
     * @param process ID of the process.
     */
    public void sendMessage(int ID, int process) {
        sendMessage(new MessageGuiNotify(ID, process));
    }

    public static void sendMessage(IMessage message) {
        DimensionalPockets.networkDispatcher.sendToServer(message);
    }

    public abstract void onActionPerformed(GuiWidget widget, int mouse);

}