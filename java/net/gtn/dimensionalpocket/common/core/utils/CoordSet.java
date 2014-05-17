package net.gtn.dimensionalpocket.common.core.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class CoordSet {

    private int x, y, z;

    public CoordSet(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CoordSet(double x, double y, double z) {
        this.x = (int) Math.round(x);
        this.y = (int) Math.round(y);
        this.z = (int) Math.round(z);
    }

    public CoordSet(int[] array) {
        this(array[0], array[1], array[2]);
    }

    public CoordSet(EntityPlayer player) {
        this((int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
    }

    public CoordSet setX(int x) {
        this.x = x;
        return this;
    }

    public CoordSet setY(int y) {
        this.y = y;
        return this;
    }

    public CoordSet setZ(int z) {
        this.z = z;
        return this;
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

    public CoordSet addCoordSet(CoordSet coordSet) {
        return addX(coordSet.x).addY(coordSet.y).addZ(coordSet.z);
    }

    public CoordSet swapAxis(Axis first, Axis second) {
        if (first == Axis.UNKNOWN || second == Axis.UNKNOWN || (first == second))
            return copy();

        int temp = 0;

        switch(first){
            case X:
                break;
            case Y:
                break;
            case Z:
                break;
            default:
                break;
        }

        return copy();
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

    @Override
    public String toString() {
        return x + ":" + y + ":" + z;
    }

    public int[] toArray() {
        return new int[] { x, y, z };
    }

    public CoordSet copy() {
        return new CoordSet(x, y, z);
    }

    public CoordSet asChunkCoords() {
        x = x >> 4;
        y = y >> 4;
        z = z >> 4;

        return this;
    }

    public CoordSet toChunkCoords() {
        return copy().asChunkCoords();
    }

    public CoordSet asBlockCoords() {
        x *= 16;
        y *= 16;
        z *= 16;

        return this;
    }

    public CoordSet toBlockCoords() {
        return copy().asBlockCoords();
    }

    public CoordSet asSpawnPoint() {
        x = x % 16;
        y = y % 16;
        z = z % 16;

        return this;
    }

    public static enum Axis {
        X, Y, Z, UNKNOWN;

        public static Axis getAxisFromDirection(ForgeDirection direction) {
            if (direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH)
                return Z;
            if (direction == ForgeDirection.EAST || direction == ForgeDirection.WEST)
                return X;
            if (direction == ForgeDirection.UP || direction == ForgeDirection.DOWN)
                return Y;
            return UNKNOWN;
        }

    }
}
