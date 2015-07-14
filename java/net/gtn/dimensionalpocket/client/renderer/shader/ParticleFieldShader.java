package net.gtn.dimensionalpocket.client.renderer.shader;

import net.gtn.dimensionalpocket.client.renderer.shader.ShaderHelper.ShaderCallback;
import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.ARBShaderObjects;

public class ParticleFieldShader {
    private ParticleFieldShader() {
    }

    private static int id;
    private static ShaderCallback callback;

    static void init() {
        id = ShaderHelper.createProgram("particlefield.vert", "particlefield.frag");

        callback = new ShaderCallback() {
            @Override
            public void call(int shader) {
                Minecraft mc = Minecraft.getMinecraft();

                int x = ARBShaderObjects.glGetUniformLocationARB(shader, "yaw");
                ARBShaderObjects.glUniform1fARB(x, (float) ((mc.thePlayer.rotationYaw * 2 * Math.PI) / 360.0));

                int z = ARBShaderObjects.glGetUniformLocationARB(shader, "pitch");
                ARBShaderObjects.glUniform1fARB(z, -(float) ((mc.thePlayer.rotationPitch * 2 * Math.PI) / 360.0));
            }
        };
    }

    public static void use() {
        ShaderHelper.useShader(id, callback);
    }

    public static void release() {
        ShaderHelper.releaseShader();
    }
}
