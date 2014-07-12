package net.gtn.dimensionalpocket.common.core.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class MovingObjectPositionUtil {

    public static MovingObjectPosition getCurrentMovingObjectPosition(EntityPlayer player) {

        double distance = player.capabilities.isCreativeMode ? 5.0F : 4.5F;
        Vec3 posVec = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
        Vec3 lookVec = player.getLook(1);
        posVec.yCoord += player.getEyeHeight();
        lookVec = posVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
        return player.worldObj.rayTraceBlocks(posVec, lookVec);
    }

    public static int getCurrentMousedOverSide(EntityPlayer player) {

        MovingObjectPosition mouseOver = getCurrentMovingObjectPosition(player);
        return mouseOver == null ? 0 : mouseOver.sideHit;
    }
}
