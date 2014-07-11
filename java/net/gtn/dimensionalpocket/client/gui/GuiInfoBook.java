package net.gtn.dimensionalpocket.client.gui;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import net.gtn.dimensionalpocket.client.ClientProxy;
import net.gtn.dimensionalpocket.client.utils.Colour;
import net.gtn.dimensionalpocket.client.utils.GuiSheet;
import net.gtn.dimensionalpocket.client.utils.RecipeHelper;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiInfoBook extends GuiContainer {

    private GuiCustomButton leftArrow, rightArrow;

    private int currentPage;
    private int prevLines = 0;

    private int MAX_PAGE;
    private int CRAFTING_RECIPE_0;
    private int CRAFTING_RECIPE_1;
    private int CRAFTING_RECIPE_2;

    public GuiInfoBook() {
        super(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return true;
            }
        });

        currentPage = ClientProxy.currentPage;

        xSize = 154;
        ySize = 180;

        String num = StatCollector.translateToLocal("info.page.maxPage");
        String craftingRecipe0 = StatCollector.translateToLocal("info.page.recipe.block.id");
        String craftingRecipe1 = StatCollector.translateToLocal("info.page.recipe.ender.id");
        String craftingRecipe2 = StatCollector.translateToLocal("info.page.recipe.nether.id");
        try {
            MAX_PAGE = Integer.parseInt(num);
            CRAFTING_RECIPE_0 = Integer.parseInt(craftingRecipe0);
            CRAFTING_RECIPE_1 = Integer.parseInt(craftingRecipe1);
            CRAFTING_RECIPE_2 = Integer.parseInt(craftingRecipe2);
        } catch (NumberFormatException exception) {
            DPLogger.severe("Error in current .lang file. Please make sure that all page numbers are a proper number.");
            MAX_PAGE = 9;
            CRAFTING_RECIPE_0 = 5;
            CRAFTING_RECIPE_0 = 6;
            CRAFTING_RECIPE_0 = 7;
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        initArrows();
    }

    private void initArrows() {
        int x = guiLeft + (xSize / 2) - (GuiArrow.state.getWidth() / 2) + 2;
        int y = guiTop + ySize - (GuiArrow.state.getHeight() * 2);

        leftArrow = new GuiArrow(x - 44, y).setVisible(currentPage != 0);
        leftArrow.getTextureState().addTexY(13);
        rightArrow = new GuiArrow(x + 44, y).setVisible(currentPage != MAX_PAGE);
    }

    @Override
    public void onGuiClosed() {
        ClientProxy.currentPage = currentPage;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        mc.renderEngine.bindTexture(GuiSheet.GUI_INFO_BOOK);

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if (shouldDrawRecipe())
            drawTexturedModalRect(x + 52, y + 37, 155, 0, 54, 106);

        leftArrow.render(mouseX, mouseY);
        rightArrow.render(mouseX, mouseY);

        if (shouldDrawRecipe())
            renderRecipe(getRecipeType(), mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        glPushMatrix();

        float scale = 0.75F;
        glScalef(scale, scale, scale);

        String tempString = StatCollector.translateToLocal(shouldDrawRecipe() ? getRecipeString() : ("info.page." + currentPage));

        if (tempString == null)
            tempString = "";

        drawCentredString(tempString, 0, 0, 140, new Colour(0.2F, 0.2F, 0.2F, 1.0F));

        glPopMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int t) {
        if (leftArrow.canClick(mouseX, mouseY) && leftArrow.onClick(mouseX, mouseY))
            currentPage = t == 2 ? 0 : currentPage - 1;
        if (rightArrow.canClick(mouseX, mouseY) && rightArrow.onClick(mouseX, mouseY))
            currentPage = t == 2 ? MAX_PAGE : currentPage + 1;
        currentPage = MathHelper.clamp_int(currentPage, 0, MAX_PAGE);
        leftArrow.setVisible(currentPage != 0);
        rightArrow.setVisible(currentPage != MAX_PAGE);
    }

    protected void drawCentredString(String string, int xOffset, int yOffset, int length, Colour colour) {
        prevLines = 0;
        for (String str : string.split("<br>")) {
            int x = (xSize - 135) / 2 + 30;
            int y = ySize / 2 - 70;

            fontRendererObj.drawSplitString(str, x + xOffset, y + yOffset + (fontRendererObj.FONT_HEIGHT * prevLines), length, colour.getInt());
            prevLines += (int) Math.ceil((float) (str.length() * 7) / (float) 165);
        }
    }

    private String getRecipeString() {
        StringBuilder sb = new StringBuilder();
        sb.append("info.page.recipe.");

        int recipeType = getRecipeType();
        switch (recipeType) {
            case 0:
                sb.append("block");
                break;
            case 1:
                sb.append("ender");
                break;
            case 2:
                sb.append("nether");
                break;
            default:
                break;
        }

        return sb.toString();
    }

    private boolean shouldDrawRecipe() {
        return getRecipeType() >= 0;
    }

    private int getRecipeType() {
        int type = -1;

        if (currentPage == CRAFTING_RECIPE_0)
            type = 0;
        else if (currentPage == CRAFTING_RECIPE_1)
            type = 1;
        else if (currentPage == CRAFTING_RECIPE_2)
            type = 2;

        return type;
    }

    private void renderRecipe(int type, int mouseX, int mouseY) {
        switch (type) {
            case 0:
                renderRecipeGrid(RecipeHelper.getBlockRecipe(), new ItemStack(ModBlocks.dimensionalPocket), mouseX, mouseY);
                break;
            case 1:
                renderRecipeGrid(RecipeHelper.getEnderRecipe(), ModItems.getEnderCrystal(), mouseX, mouseY);
                break;
            case 2:
                renderRecipeGrid(RecipeHelper.getNetherRecipe(), ModItems.getNetherCrystal(), mouseX, mouseY);
                break;
            default:
                break;
        }
    }

    private void renderRecipeGrid(ItemStack[] recipeArray, ItemStack result, int mouseX, int mouseY) {
        int xIndex = 0;
        int yIndex = 0;

        ArrayList<ItemStack> toolTipList = new ArrayList<ItemStack>(2);

        for (ItemStack itemStack : recipeArray) {
            ItemStack tempStack = new GuiItemStack(itemStack, guiLeft + 52 + (xIndex * 18), guiTop + 37 + (yIndex * 18)).doRender(mouseX, mouseY);
            if (tempStack != null)
                toolTipList.add(tempStack);
            if (++xIndex >= 3) {
                xIndex = 0;
                yIndex++;
            }
        }

        ItemStack tempStack = new GuiItemStack(result, guiLeft + 70, guiTop + 125).doRender(mouseX, mouseY);
        if (tempStack != null)
            toolTipList.add(tempStack);

        for (ItemStack itemStack : toolTipList)
            renderToolTip(itemStack, mouseX, mouseY);
    }
}
