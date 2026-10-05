package com.brandon3055.draconicevolution.client.shader;

import com.brandon3055.brandonscore.client.shader.BCShader;
import com.brandon3055.brandonscore.client.shader.BCUniform;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.Identifier;

import java.util.Objects;

/**
 * Created by brandon3055 on 13/11/2022
 */
public class ShieldShader extends BCShader<ShieldShader> {

    private BCUniform activationUniform;
    private BCUniform baseColourUniform;

    public ShieldShader(String path, VertexFormat format) {
        this(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, path), format);
    }

    public ShieldShader(Identifier location, VertexFormat format) {
        super(location, format);
        withVertexShader("tools/tool_base");
        uniform("SimpleLight", BCUniform.Type.BOOL);
        uniform("DisableLight", BCUniform.Type.BOOL).glUniform1b(true);
        uniform("DisableOverlay", BCUniform.Type.BOOL).glUniform1b(true);
        uniform("UV1Override", BCUniform.Type.IVEC2).glUniform2i(-1, -1);
        uniform("UV2Override", BCUniform.Type.IVEC2).glUniform2i(-1, -1);
        baseColourUniform = uniform("BaseColor", BCUniform.Type.VEC4);
        activationUniform = uniform("Activation", BCUniform.Type.FLOAT);
        baseColourUniform.glUniform4f(1F, 1F, 1F, 1F);
    }

    public BCUniform getActivationUniform() {
        return Objects.requireNonNull(activationUniform, missingUniformMessage("Activation"));
    }

    public BCUniform getBaseColourUniform() {
        return Objects.requireNonNull(baseColourUniform, missingUniformMessage("BaseColor"));
    }
}
