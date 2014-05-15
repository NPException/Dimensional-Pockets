package net.gtn.dimensionalpocket.common.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class CoordSet {

    private int x, y, z;

    public CoordSet(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CoordSet(int[] array) {
        this(array[0], array[1], array[2]);
    }

    public CoordSet(EntityPlayer player) {
        this((int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public CoordSet addX(int amount) {
        x += amount;
        return this;
    }

    public CoordSet addY(int amount) {
        y += amount;
        return this;
    }

    public CoordSet addZ(int amount) {
        z += amount;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = this.x;
        hash *= 31 + this.y;
        hash *= 31 + this.z;
        return hash;
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setIntArray("coordSet", toArray());
    }

    public static CoordSet readFromNBT(NBTTagCompound tag) {
        int[] array = tag.getIntArray("coordSet");
        if (array.length == 0)
            return null;
        return new CoordSet(array);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CoordSet))
            return false;
        CoordSet other = (CoordSet) obj;
        return other.x == x && other.y == y && other.z == z;
    }

    public String toPacketString() {
        return x + ":" + y + ":" + z;
    }

    @Override
    public String toString() {
        return " @ " + toPacketString();
    }

    public int[] toArray() {
        return new int[] { x, y, z };
    }

    public CoordSet copy() {
        return new CoordSet(x,y,z);
    }
}
