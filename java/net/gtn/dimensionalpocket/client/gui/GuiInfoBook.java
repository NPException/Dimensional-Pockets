package net.gtn.dimensionalpocket.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.client.gui.GuiContainerAbstract;
import me.jezza.oc.client.gui.components.GuiWidget;
import me.jezza.oc.client.gui.lib.Colour;
import net.gtn.dimensionalpocket.client.ClientProxy;
import net.gtn.dimensionalpocket.client.gui.components.GuiArrow;
import net.gtn.dimensionalpocket.client.gui.components.GuiItemStack;
import net.gtn.dimensionalpocket.client.utils.GuiSheet;
import net.gtn.dimensionalpocket.client.utils.RecipeHelper;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class GuiInfoBook extends GuiContainerAbstract {
    private GuiWidget<GuiArrow> rightArrow, leftArrow;
    private GuiItemStack itemStackArray[] = new GuiItemStack[10];

    private int currentPage;

    private int MAX_PAGE;
    private int CRAFTING_RECIPE_POCKET;
    private int CRAFTING_RECIPE_END_CRYSTAL;
    private int CRAFTING_RECIPE_NETHER_CRYSTAL;

    public GuiInfoBook(EntityPlayer player) {
        super(player);
        setMainTexture(GuiSheet.GUI_INFO_BOOK);
        currentPage = ClientProxy.currentPage;

        xSize = 154;
        ySize = 180;

        String num = StatCollector.translateToLocal("info.page.maxPage");
        String craftingRecipePocket = StatCollector.translateToLocal("info.page.recipe.block.id");
        String craftingRecipeEndCrystal = StatCollector.translateToLocal("info.page.recipe.endCrystal.id");
        String craftingRecipeNetherCrystal = StatCollector.translateToLocal("info.page.recipe.netherCrystal.id");
        try {
            MAX_PAGE = Integer.parseInt(num);
            CRAFTING_RECIPE_POCKET = Integer.parseInt(craftingRecipePocket);
            CRAFTING_RECIPE_END_CRYSTAL = Integer.parseInt(craftingRecipeEndCrystal);
            CRAFTING_RECIPE_NETHER_CRYSTAL = Integer.parseInt(craftingRecipeNetherCrystal);
        } catch (NumberFormatException exception) {
            DPLogger.severe("Error in current .lang file. Please make sure that all page numbers are proper numbers.");
            MAX_PAGE = 9;
            CRAFTING_RECIPE_END_CRYSTAL = 5;
            CRAFTING_RECIPE_NETHER_CRYSTAL = 6;
            CRAFTING_RECIPE_POCKET = 7;
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        initArrows();
        int index = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                itemStackArray[index++] = new GuiItemStack(null, guiLeft + 51 + (j * 18), guiTop + 36 + (i * 18));
        itemStackArray[9] = new GuiItemStack(null, guiLeft + 69, guiTop + 124);

        for (GuiItemStack gui : itemStackArray)
            addButton(gui);
    }

    private void initArrows() {
        int x = guiLeft + (xSize / 2) - (18 / 2) + 2;
        int y = guiTop + ySize - (10 * 2);

        GuiArrow leftGuiArrow = new GuiArrow(x - 44, y);
        leftGuiArrow.v += 13;
        leftArrow = leftGuiArrow.setVisible(currentPage != 0);
        rightArrow = new GuiArrow(x + 44, y).setVisible(currentPage != MAX_PAGE);
        addButton(leftArrow);
        addButton(rightArrow);
    }

    @Override
    public void onGuiClosed() {
        ClientProxy.currentPage = currentPage;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        glPushMatrix();
        
        bindTexture();
        drawTexturedModalRect(middleX, middleY, 0, 0, xSize, ySize);

        if (shouldDrawRecipe()) {
            drawTexturedModalRect(middleX + 52, middleY + 37, 155, 0, 54, 106);
            renderRecipe(getRecipeType());
        }

        super.drawGuiContainerBackgroundLayer(var1, mouseX, mouseY);
        
        glPopMatrix();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        glPushMatrix();

        float scale = 0.75F;
        glScalef(scale, scale, scale);

        String tempString;
        if (shouldDrawRecipe())
            tempString = "§n" + StatCollector.translateToLocal(getRecipeString()) + "§r";
        else
            tempString = StatCollector.translateToLocal("info.page." + currentPage);

        if (tempString == null)
            tempString = "";

        drawWrappedString(tempString, 0, 0, 140, new Colour(0.2, 0.2, 0.2, 1.0));

        glPopMatrix();
    }

    @Override
    public void onActionPerformed(GuiWidget widget, int mouse) {
        int id = widget.getId();

        if (id == leftArrow.getId())
            currentPage--;
        if (id == rightArrow.getId())
            currentPage++;

        currentPage = MathHelper.clamp_int(currentPage, 0, MAX_PAGE);
        rightArrow.setVisible(currentPage != MAX_PAGE);
        leftArrow.setVisible(currentPage != 0);

        if (!shouldDrawRecipe())
            for (GuiItemStack gui : itemStackArray)
                gui.setItemStack(null);
    }

    protected void drawWrappedString(String string, int xOffset, int yOffset, int length, Colour colour) {
        int prevLines = 0;
        for (String str : string.split("<br>")) {
            int x = (xSize - 135) / 2 + 30;
            int y = ySize / 2 - 70;

            if (shouldDrawRecipe()) {
                fontRendererObj.drawString(str, x + xOffset, y + yOffset + (fontRendererObj.FONT_HEIGHT * prevLines), colour.getInt());
            } else {
                fontRendererObj.drawSplitString(str, x + xOffset, y + yOffset + (fontRendererObj.FONT_HEIGHT * prevLines), length, colour.getInt());
            }
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
                sb.append("endCrystal");
                break;
            case 2:
                sb.append("netherCrystal");
                break;
        }

        return sb.toString();
    }

    private boolean shouldDrawRecipe() {
        return getRecipeType() >= 0;
    }

    private int getRecipeType() {
        int type = -1;

        if (currentPage == CRAFTING_RECIPE_POCKET)
            type = 0;
        else if (currentPage == CRAFTING_RECIPE_END_CRYSTAL)
            type = 1;
        else if (currentPage == CRAFTING_RECIPE_NETHER_CRYSTAL)
            type = 2;

        return type;
    }

    private void renderRecipe(int type) {
        ItemStack[] itemStacks = RecipeHelper.getRecipe(type);
        if (itemStacks == null)
            return;

        int index = 0;
        for (ItemStack itemStack : itemStacks)
            itemStackArray[index++].setItemStack(itemStack);
    }
}
