package net.gtn.dimensionalpocket.client.lib;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class TextureMaps {

	public static final String TEXTURE_BLOCK_ROOT = Reference.MOD_IDENTIFIER + "textures/blocks/";
	public static final String TEXTURE_MISC_ROOT = Reference.MOD_IDENTIFIER + "textures/misc/";
	public static final String TEXTURE_PARTICLE_FIELD_ROOT = Reference.MOD_IDENTIFIER + "textures/misc/particleField/";

	public static final ResourceLocation BLANK_TEXTURE = new ResourceLocation(TEXTURE_MISC_ROOT + "blank.png");

}
