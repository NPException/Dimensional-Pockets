package net.gtn.dimensionalpocket.client.particles;

import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PlayerStreamFX extends EntityFX {

    public PlayerStreamFX(World world, EntityPlayer player, CoordSet targetSet, int ticksToTake) {
        super(world, player.posX, player.posY, player.posZ, targetSet.getX() + 0.5F, targetSet.getY() + 0.5F, targetSet.getZ() + 0.5F);

        motionX = (targetSet.getX() - player.posX) / ticksToTake;
        motionY = (targetSet.getY() - player.posY) / ticksToTake;
        motionZ = (targetSet.getZ() - player.posZ) / ticksToTake;
        
        
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= 0.04D * (double) this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
    }

    @Override
    public void renderParticle(Tessellator tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {

    }
}
