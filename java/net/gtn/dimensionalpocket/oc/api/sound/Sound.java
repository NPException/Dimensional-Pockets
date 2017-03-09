package net.gtn.dimensionalpocket.oc.api.sound;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class Sound implements ISound {
    private String name;

    public Sound(String name) {
        this.name = name;
    }

    public Sound(String modID, String name) {
        this.name = modID + name;
    }

    @Override
    public void play(Entity entity) {
        play(entity, 1.0F, 1.0F);
    }

    @Override
    public void play(Entity entity, float volume, float pitch) {
        entity.worldObj.playSoundAtEntity(entity, name, volume, pitch);
    }

    @Override
    public void play(World world, double x, double y, double z, float volume, float pitch) {
        world.playSound(x, y, z, name, volume, pitch, false);
    }

    @Override
    public void play(World world, double x, double y, double z) {
        play(world, x, y, z, 1.0F, 1.0F);
    }

    @Override
    public void play(World world, int x, int y, int z, float volume, float pitch) {
        play(world, x + 0.5F, y + 0.5F, z + 0.5F, volume, pitch);
    }

    @Override
    public void play(World world, int x, int y, int z) {
        play(world, x + 0.5F, y + 0.5F, z + 0.5F, 1.0F, 1.0F);
    }
}