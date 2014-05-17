package net.gtn.dimensionalpocket.client.particles;

import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerStreamFX extends EntityFX {

    private double x, y, z, distance, constantY;
    private CoordSet targetSet;

    public PlayerStreamFX(World world, EntityPlayer player, CoordSet targetSet, int ticksToTake) {
        super(world, player.posX, player.posY, player.posZ);
        this.targetSet = targetSet;
        noClip = true;

        setParticleIcon(ModItems.craftingItems.getIconFromDamage(0));

        particleAge = 0;
        particleMaxAge = ticksToTake + 1;

        double diffX = (targetSet.getX() + 0.5F) - player.posX;
        double diffY = (targetSet.getY() - 1F) - player.posY;
        double diffZ = (targetSet.getZ() + 0.5F) - player.posZ;

        distance = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

        motionX = diffX / ticksToTake;
        motionY = constantY = diffY / ticksToTake;
        motionZ = diffZ / ticksToTake;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        motionY = constantY * getVerticleMovement();

        if (this.particleAge++ >= this.particleMaxAge)
            this.setDead();

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    public double getVerticleMovement() {
        return 8 * Math.sin((Math.PI * particleAge) / (distance / 2));
    }

    @Override
    public int getFXLayer() {
        return 2;
    }

    @Override
    public void renderParticle(Tessellator tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
        float f6 = ((float) this.particleAge + par2) / (float) this.particleMaxAge * 32.0F;

        if (f6 < 0.0F) {
            f6 = 0.0F;
        }

        if (f6 > 1.0F) {
            f6 = 1.0F;
        }

        super.renderParticle(tessellator, par2, par3, par4, par5, par6, par7);
    }
}
