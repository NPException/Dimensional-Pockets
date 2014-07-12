package net.gtn.dimensionalpocket.client.gui;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import net.gtn.dimensionalpocket.client.ClientProxy;
import net.gtn.dimensionalpocket.client.gui.components.GuiArrow;
import net.gtn.dimensionalpocket.client.gui.components.GuiItemStack;
import net.gtn.dimensionalpocket.client.gui.framework.GuiWidget;
import net.gtn.dimensionalpocket.client.utils.Colour;
import net.gtn.dimensionalpocket.client.utils.GuiSheet;
import net.gtn.dimensionalpocket.client.utils.RecipeHelper;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiInfoBook extends GuiAbstract {
    private GuiWidget rightArrow, leftArrow;
    private GuiItemStack itemStackArray[] = new GuiItemStack[10];

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
        setMainTexture(GuiSheet.GUI_INFO_BOOK);
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
            DPLogger.severe("Error in current .lang file. Please make sure that all page numbers are proper numbers.");
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

        leftArrow = new GuiArrow(x - 44, y).addV(13).setVisible(currentPage != 0);
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
        bindTexture();
        drawTexturedModalRect(middleX, middleY, 0, 0, xSize, ySize);

        if (shouldDrawRecipe()) {
            drawTexturedModalRect(middleX + 52, middleY + 37, 155, 0, 54, 106);
            renderRecipe(getRecipeType());
        }

        super.drawGuiContainerBackgroundLayer(var1, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        glPushMatrix();

        float scale = 0.75F;
        glScalef(scale, scale, scale);

        String tempString = StatCollector.translateToLocal(shouldDrawRecipe() ? getRecipeString() : ("info.page." + currentPage));

        if (tempString == null)
            tempString = "";

        drawWrappedString(tempString, 0, 0, 140, new Colour(0.2F, 0.2F, 0.2F, 1.0F));

        glPopMatrix();
    }

    protected void drawWrappedString(String string, int xOffset, int yOffset, int length, Colour colour) {
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

    private void renderRecipe(int type) {
        ItemStack[] itemStacks = RecipeHelper.getRecipe(type);
        if (itemStacks == null)
            return;

        int index = 0;
        for (ItemStack itemStack : itemStacks)
            itemStackArray[index++].setItemStack(itemStack);
    }

    @Override
    public void onButtonClicked(GuiWidget widget, int mouseClick) {
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
}
