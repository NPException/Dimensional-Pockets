package net.gtn.dimensionalpocket.client.gui;

import me.jezza.oc.client.gui.GuiContainerAbstract;
import me.jezza.oc.client.gui.components.GuiWidget;
import me.jezza.oc.client.gui.interfaces.IGuiRenderHandler;
import net.gtn.dimensionalpocket.client.gui.components.GuiExitButton;
import net.gtn.dimensionalpocket.client.gui.components.GuiSideButton;
import net.gtn.dimensionalpocket.client.gui.components.GuiStateType;
import net.gtn.dimensionalpocket.client.gui.components.GuiToggleProcess;
import net.gtn.dimensionalpocket.client.utils.GuiSheet;
import net.gtn.dimensionalpocket.common.core.container.ContainerPocketConfig;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

public class GuiPocketConfig extends GuiContainerAbstract implements IGuiRenderHandler {

    private final ForgeDirection side;
    private ForgeDirection lookDirection;

    public GuiPocketConfig(EntityPlayer player, TileDimensionalPocket tile, int sideHit, int sideLooking) {
        super(player, new ContainerPocketConfig(tile));
        setMainTexture(GuiSheet.GUI_CONFIG);

        this.side = ForgeDirection.getOrientation(sideHit);
        this.lookDirection = Utils.getDirectionFromBitMask(sideLooking);

        xSize = 74;
        ySize = 74;
    }

    @Override
    public void initGui() {
        super.initGui();
        initButtons();
    }

    private void initButtons() {
        int x = guiLeft;
        int y = guiTop;

        addButton(new GuiExitButton(x + (20) - 34, y + (20) - 11));

        ForgeDirection sideRotation = ForgeDirection.UP;
        switch (side) {
            case DOWN:
                lookDirection = lookDirection.getOpposite();
            case UP:
                sideRotation = lookDirection;
                break;
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
                lookDirection = ForgeDirection.UP;
            default:
                break;
        }

        addButton(new GuiSideButton(x + (2 * 20) - 11, y + (1 * 20) - 11, 16, 16).setDirection(side.getRotation(lookDirection.getRotation(side))));
        addButton(new GuiSideButton(x + (1 * 20) - 11, y + (2 * 20) - 11, 16, 16).setDirection(side.getRotation(sideRotation)));
        addButton(new GuiSideButton(x + (2 * 20) - 11, y + (2 * 20) - 11, 16, 16).setDirection(side));
        addButton(new GuiSideButton(x + (3 * 20) - 11, y + (2 * 20) - 11, 16, 16).setDirection(side.getRotation(sideRotation.getOpposite())));
        addButton(new GuiSideButton(x + (2 * 20) - 11, y + (3 * 20) - 11, 16, 16).setDirection(side.getRotation(lookDirection.getRotation(side).getOpposite())));
        addButton(new GuiSideButton(x + (3 * 20) - 11, y + (3 * 20) - 11, 16, 16).setDirection(side.getOpposite()));

        addButton(new GuiToggleProcess(x + 80, y + 13, 208));
        addButton(new GuiStateType(x + 80, y + 46, 74, 0, 16, 16).setTypeState(1));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float tick, int mouseX, int mouseY) {
        bindTexture();
        drawTexturedModalRect(middleX - 19, middleY + 4, 0, 108, 19, 21);

        drawTexturedModalRect(middleX + 74, middleY + 4, 0, 74, 31, 34);
        drawTexturedModalRect(middleX + 76, middleY + 9, 74, 32, 24, 24);

        drawTexturedModalRect(middleX + 74, middleY + 37, 0, 74, 31, 34);
        drawTexturedModalRect(middleX + 76, middleY + 42, 74, 32, 24, 24);

        drawTexturedModalRect(middleX, middleY, 0, 0, xSize, ySize);
        super.drawGuiContainerBackgroundLayer(tick, mouseX, mouseY);
    }

    @Override
    public void onActionPerformed(GuiWidget widget, int mouse) {
        int id = widget.getId();
        if (id == 0){
            
            return;
        }

        DPLogger.info(id);
    }
}
