package net.gtn.dimensionalpocket.oc.api.sound;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface ISound {

    public void play(Entity entity);

    public void play(Entity entity, float volume, float pitch);

    public void play(World world, double x, double y, double z, float volume, float pitch);

    public void play(World world, double x, double y, double z);

    public void play(World world, int x, int y, int z, float volume, float pitch);

    public void play(World world, int x, int y, int z);

}