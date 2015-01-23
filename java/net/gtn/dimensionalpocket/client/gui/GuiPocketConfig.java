package net.gtn.dimensionalpocket.client.gui;

import me.jezza.oc.client.gui.GuiContainerAbstract;
import me.jezza.oc.client.gui.components.GuiWidget;
import me.jezza.oc.client.gui.interfaces.IGuiRenderHandler;
import net.gtn.dimensionalpocket.client.gui.components.GuiExitButton;
import net.gtn.dimensionalpocket.client.gui.components.GuiSideButton;
import net.gtn.dimensionalpocket.client.gui.components.GuiToggleProcess;
import net.gtn.dimensionalpocket.client.utils.GuiSheet;
import net.gtn.dimensionalpocket.common.core.container.ContainerPocketConfig;
import net.gtn.dimensionalpocket.common.core.pocket.PocketSideState;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

public class GuiPocketConfig extends GuiContainerAbstract implements IGuiRenderHandler {

    private int flag = 0;
    private int yOffset = 0;

    private ForgeDirection lookDirection;

    private Pocket pocket;

    private ForgeDirection currentDirection = ForgeDirection.UNKNOWN;
    private PocketSideState flowState = PocketSideState.NONE;

    private GuiToggleProcess flowToggle;

    public GuiPocketConfig(EntityPlayer player, TileDimensionalPocket tile, int sideHit, int sideLooking) {
        super(player, new ContainerPocketConfig(tile));
        setMainTexture(GuiSheet.GUI_CONFIG);

        currentDirection = ForgeDirection.getOrientation(sideHit);
        flag = 1;
        this.lookDirection = Utils.getDirectionFromBitMask(sideLooking);
        pocket = tile.getPocket();
        flowState = pocket.getFlowState(currentDirection);

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
        switch (currentDirection) {
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

        addButton(new GuiSideButton(x + (2 * 20) - 11, y + (1 * 20) - 11, 16, 16).setDirection(currentDirection.getRotation(lookDirection.getRotation(currentDirection))));
        addButton(new GuiSideButton(x + (1 * 20) - 11, y + (2 * 20) - 11, 16, 16).setDirection(currentDirection.getRotation(sideRotation)));
        addButton(new GuiSideButton(x + (2 * 20) - 11, y + (2 * 20) - 11, 16, 16).setDirection(currentDirection));
        addButton(new GuiSideButton(x + (3 * 20) - 11, y + (2 * 20) - 11, 16, 16).setDirection(currentDirection.getRotation(sideRotation.getOpposite())));
        addButton(new GuiSideButton(x + (2 * 20) - 11, y + (3 * 20) - 11, 16, 16).setDirection(currentDirection.getRotation(lookDirection.getRotation(currentDirection).getOpposite())));
        addButton(new GuiSideButton(x + (3 * 20) - 11, y + (3 * 20) - 11, 16, 16).setDirection(currentDirection.getOpposite()));

        flowToggle = new GuiToggleProcess(x + 80, y + 13, 208);
        flowToggle.setTypeState(flowState.ordinal());
        addButton(flowToggle);
//        addButton(new GuiStateType(x + 80, y + 46, 74, 0, 16, 16).setTypeState(1));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float tick, int mouseX, int mouseY) {
        bindTexture();

        if (yOffset > 1)
            drawTexturedModalRect(middleX + 11, middleY + 59 + yOffset, 122, 0, 52, 16);

        drawTexturedModalRect(middleX - 19, middleY + 4, 0, 108, 19, 21);

        drawTexturedModalRect(middleX + 74, middleY + 4, 0, 74, 31, 34);
//        drawTexturedModalRect(middleX + 76, middleY + 9, 74, 32, 24, 24);

//        drawTexturedModalRect(middleX + 74, middleY + 37, 0, 74, 31, 34);
//        drawTexturedModalRect(middleX + 76, middleY + 42, 74, 32, 24, 24);

        drawTexturedModalRect(middleX, middleY, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(tick, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (currentDirection != ForgeDirection.UNKNOWN && yOffset >= 15)
            drawCenteredString(fontRendererObj, currentDirection.name(), xSize / 2, ySize + 2, -2);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        int step = 5;
        if (flag < 0) {
            yOffset -= step;
            if (yOffset <= 1) {
                yOffset = 1;
                flag = 0;
            }
        } else if (flag > 0) {
            yOffset += step;
            if (yOffset >= 15) {
                yOffset = 15;
                flag = 0;
            }
        }
    }

    @Override
    public void onActionPerformed(GuiWidget widget, int mouse) {
        int id = widget.getId();
        if (id == 0) {
            if (currentDirection != ForgeDirection.UNKNOWN) {
                currentDirection = ForgeDirection.UNKNOWN;
                flag = -1;
            }
            sendMessage(-1, 0);
            return;
        }

        if (0 < id && id < 7) {
            GuiSideButton sideButton = (GuiSideButton) getButtonList().get(id);
            ForgeDirection direction = sideButton.getDirection();
            if (currentDirection == direction) {
                currentDirection = ForgeDirection.UNKNOWN;
                flowState = PocketSideState.NONE;
                flowToggle.setTypeState(flowState.ordinal());
                flag = -1;
            } else {
                if (currentDirection == ForgeDirection.UNKNOWN)
                    flag = 1;
                currentDirection = direction;
                flowState = pocket.getFlowState(direction);
                flowToggle.setTypeState(flowState.ordinal());
            }
            return;
        }

        if (currentDirection == ForgeDirection.UNKNOWN)
            return;

        if (widget.getId() == flowToggle.getId()) {
            flowToggle.processMouseClick(mouse);
            flowState = PocketSideState.values()[flowToggle.typeState];
            sendMessage(currentDirection.ordinal(), flowState.ordinal());
        }
    }
}
