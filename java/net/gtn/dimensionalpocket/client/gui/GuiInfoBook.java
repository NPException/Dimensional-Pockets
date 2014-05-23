package net.gtn.dimensionalpocket.client.gui;

import static org.lwjgl.opengl.GL11.*;
import net.gtn.dimensionalpocket.client.ClientProxy;
import net.gtn.dimensionalpocket.client.utils.Colour;
import net.gtn.dimensionalpocket.client.utils.GuiSheet;
import net.gtn.dimensionalpocket.client.utils.RecipeHelper;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GuiInfoBook extends GuiContainer {

    private GuiArrow arrow1;
    private GuiArrow arrow2;

    private int currentPage;
    private int prevLines = 0;

    private int MAX_NUM;
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
            MAX_NUM = Integer.parseInt(num);
            CRAFTING_RECIPE_0 = Integer.parseInt(craftingRecipe0);
            CRAFTING_RECIPE_1 = Integer.parseInt(craftingRecipe1);
            CRAFTING_RECIPE_2 = Integer.parseInt(craftingRecipe2);
        } catch (NumberFormatException exception) {
            DPLogger.severe("Error in current .lang file. Please correct the info.page.maxPage to a proper number.");
            MAX_NUM = 5;
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        initArrows();
    }

    private void initArrows() {
        int x = guiLeft + (xSize / 2) - (GuiArrow.WIDTH / 2) + 2;
        int y = guiTop + ySize - (GuiArrow.HEIGHT * 2);

        arrow1 = new GuiArrow(1, x + 44, y);
        arrow2 = new GuiArrow(2, x - 44, y);
    }

    @Override
    public void onGuiClosed() {
        ClientProxy.currentPage = currentPage;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        if (shouldDrawRecipe()) {
            mc.renderEngine.bindTexture(GuiSheet.GUI_INFO_BOOK_CRAFTING);
        } else {
            mc.renderEngine.bindTexture(GuiSheet.GUI_INFO_BOOK);
        }

        drawTexturedModalRect(x, y, 12, 1, xSize, ySize);
        if (currentPage != MAX_NUM)
            arrow1.renderArrow(mouseX, mouseY);
        if (currentPage != 0)
            arrow2.renderArrow(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        glPushMatrix();

        float scale = 0.75F;
        glScalef(scale, scale, scale);

        String tempString = "";

        if (shouldDrawRecipe())
            tempString = StatCollector.translateToLocal(getRecipeString());
        else
            tempString = StatCollector.translateToLocal("info.page." + currentPage);

        drawCentredString(tempString, 0, 0, 140, new Colour(0.2F, 0.2F, 0.2F, 1.0F));

        glPopMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int t) {
        if (arrow1.onClick(mouseX, mouseY))
            currentPage++;
        if (arrow2.onClick(mouseX, mouseY))
            currentPage--;

        if (currentPage < 0)
            currentPage = 0;
        if (currentPage > MAX_NUM)
            currentPage = MAX_NUM;
    }

    protected void drawCentredString(String string, int xOffset, int yOffset, int length, Colour colour) {
        prevLines = 0;
        for (String str : string.split("<br>")) {
            int x = (xSize - 135) / 2 + 30;
            int y = ySize / 2 - 70;

            fontRendererObj.drawSplitString(str, x + xOffset, y + yOffset + (fontRendererObj.FONT_HEIGHT * prevLines), length, colour.getInt());
            prevLines += (int) Math.ceil((float) (str.length() * 7) / (float) 165);
        }

        if (currentPage == CRAFTING_RECIPE_0)
            renderRecipe(0);
        if (currentPage == CRAFTING_RECIPE_1)
            renderRecipe(1);
        if (currentPage == CRAFTING_RECIPE_2)
            renderRecipe(2);
    }

    private String getRecipeString() {
        StringBuilder sb = new StringBuilder();
        sb.append("info.page.recipe.");

        if (currentPage == CRAFTING_RECIPE_0)
            sb.append("block");
        if (currentPage == CRAFTING_RECIPE_1)
            sb.append("ender");
        if (currentPage == CRAFTING_RECIPE_2)
            sb.append("nether");

        return sb.toString();
    }

    private boolean shouldDrawRecipe() {
        return currentPage == CRAFTING_RECIPE_0 || currentPage == CRAFTING_RECIPE_1 || currentPage == CRAFTING_RECIPE_2;
    }

    private void renderRecipe(int type) {
        glPushMatrix();
        switch (type) {
            case 0:
                renderRecipeGrid(RecipeHelper.getBlockRecipe());
                renderItemStack(new ItemStack(ModBlocks.dimensionalPocket), 98, 171);
                break;
            case 1:
                renderRecipeGrid(RecipeHelper.getEnderRecipe());
                renderItemStack(new ItemStack(ModItems.miscItems, 1, 0), 98, 171);
                break;
            case 2:
                renderRecipeGrid(RecipeHelper.getNetherRecipe());
                renderItemStack(new ItemStack(ModItems.miscItems, 1, 1), 98, 171);
                break;
            default:
                break;
        }
        glPopMatrix();
    }

    private void renderItemStack(ItemStack itemStack, int x, int y) {
        itemRender.renderItemIntoGUI(fontRendererObj, Minecraft.getMinecraft().renderEngine, itemStack, x, y);
    }

    private void renderRecipeGrid(ItemStack[] array) {
        int xIndex = 0;
        int yIndex = 0;

        for (ItemStack itemStack : array) {
            renderItemStack(itemStack, 74 + (xIndex * 24), 54 + (yIndex * 24));
            if (++xIndex >= 3) {
                xIndex = 0;
                yIndex++;
            }
        }
    }
}
