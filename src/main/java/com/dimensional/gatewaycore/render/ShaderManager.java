package com.dimensional.gatewaycore.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import com.dimensional.gatewaycore.GatewayCore;

import net.minecraft.client.renderer.GlStateManager;

public class ShaderManager {

    private static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
    private static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;

    public static int noJeiBackgroundColor;

    public static void loadShaders() {
        noJeiBackgroundColor = createProgram(null, "/assets/gatewaycore/shaders/no_jei_background_color.frag");
    }

    public static void renderWith(int shader, Runnable f) {
        boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        ARBShaderObjects.glUseProgramObjectARB(shader);
        f.run();
        ARBShaderObjects.glUseProgramObjectARB(0);
        if (lighting)
            GlStateManager.enableLighting();
    }

    // Code copied from Botania, which was itself copied from LWJGL Wiki

    private static int createProgram(String vert, String frag) {
        int vertId = 0, fragId = 0, program;
        if (vert != null)
            vertId = createShader(vert, VERT);
        if (frag != null)
            fragId = createShader(frag, FRAG);

        program = ARBShaderObjects.glCreateProgramObjectARB();
        if (program == 0)
            return 0;

        if (vert != null)
            ARBShaderObjects.glAttachObjectARB(program, vertId);
        if (frag != null)
            ARBShaderObjects.glAttachObjectARB(program, fragId);

        ARBShaderObjects.glLinkProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program,
                ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            GatewayCore.LOGGER.error(getLogInfo(program));
            return 0;
        }

        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program,
                ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            GatewayCore.LOGGER.error(getLogInfo(program));
            return 0;
        }

        return program;
    }

    private static int createShader(String filename, int shaderType) {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

            if (shader == 0)
                return 0;

            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shader);

            if (ARBShaderObjects.glGetObjectParameteriARB(shader,
                    ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

            return shader;
        } catch (Exception e) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            e.printStackTrace();
            return -1;
        }
    }

    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj,
                ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    private static String readFileAsString(String filename) throws Exception {
        InputStream in = ShaderManager.class.getResourceAsStream(filename);

        if (in == null)
            return "";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

}
