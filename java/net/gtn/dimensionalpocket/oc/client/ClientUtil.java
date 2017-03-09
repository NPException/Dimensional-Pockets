package net.gtn.dimensionalpocket.oc.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientUtil {

    public static boolean hasPressedShift() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean hasPressedCtrl() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public static boolean hasPressedAlt() {
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }

    public static boolean isPlayer(String name) {
        return Minecraft.getMinecraft().thePlayer.getCommandSenderName().endsWith(name);
    }

}
