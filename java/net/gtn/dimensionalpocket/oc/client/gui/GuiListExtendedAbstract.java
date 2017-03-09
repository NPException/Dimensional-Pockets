package net.gtn.dimensionalpocket.oc.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiListExtendedAbstract<T extends IGuiListEntry> extends GuiListExtended {

    protected final Minecraft minecraft;
    protected final GuiScreen parent;
    protected List<T> entries;

//    private int listWidth =

    public GuiListExtendedAbstract(Minecraft minecraft, GuiScreen parent, int topBorder, int bottomBorder, int slotHeight) {
        super(minecraft, parent.width, parent.height, topBorder, parent.height - bottomBorder, slotHeight);
        this.minecraft = minecraft;
        this.parent = parent;
        entries = createList();


    }

    protected List<T> createList(){
        return new ArrayList<>();
    }


    @Override
    public IGuiListEntry getListEntry(int slot) {
        return entries.get(slot);
    }

    @Override
    protected int getSize() {
        return entries.size();
    }
}
