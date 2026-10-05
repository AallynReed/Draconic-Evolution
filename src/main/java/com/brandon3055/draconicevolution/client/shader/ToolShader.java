package com.brandon3055.draconicevolution.client.shader;

import com.brandon3055.brandonscore.client.shader.BCShader;
import com.brandon3055.brandonscore.client.shader.BCUniform;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.Identifier;

import java.util.Objects;

/**
 * Created by covers1624 on 1/10/22.
 */
public final class ToolShader extends BCShader<ToolShader> {

    private BCUniform simpleLightUniform;
    private BCUniform disableLightUniform;
    private BCUniform disableOverlayUniform;

    private BCUniform uv1OverrideUniform;
    private BCUniform uv2OverrideUniform;

    private BCUniform baseColorUniform;

    public ToolShader(String path, VertexFormat format) {
        this(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, path), format);
    }

    public ToolShader(Identifier location, VertexFormat format) {
        super(location, format);
        withVertexShader("tools/tool_base");
        simpleLightUniform = uniform("SimpleLight", BCUniform.Type.BOOL);
        disableLightUniform = uniform("DisableLight", BCUniform.Type.BOOL);
        disableOverlayUniform = uniform("DisableOverlay", BCUniform.Type.BOOL);
        uv1OverrideUniform = uniform("UV1Override", BCUniform.Type.IVEC2);
        uv2OverrideUniform = uniform("UV2Override", BCUniform.Type.IVEC2);
        baseColorUniform = uniform("BaseColor", BCUniform.Type.VEC4);
        uniform("Activation", BCUniform.Type.FLOAT);
        disableLightUniform.glUniform1b(true);
        disableOverlayUniform.glUniform1b(true);
        uv1OverrideUniform.glUniform2i(-1, -1);
        uv2OverrideUniform.glUniform2i(-1, -1);
        baseColorUniform.glUniform4f(1F, 1F, 1F, 1F);
    }

    // @formatter:off
    public final BCUniform getSimpleLightUniform() { return Objects.requireNonNull(simpleLightUniform, missingUniformMessage("SimpleLight")); }
    public final boolean hasSimpleLightUniform() { return simpleLightUniform != null; }
    public final BCUniform getDisableLightUniform() { return Objects.requireNonNull(disableLightUniform, missingUniformMessage("DisableLight")); }
    public final boolean hasDisableLightUniform() { return disableLightUniform != null; }
    public final BCUniform getDisableOverlayUniform() { return Objects.requireNonNull(disableOverlayUniform, missingUniformMessage("DisableOverlay")); }
    public final boolean hasDisableOverlayUniform() { return disableOverlayUniform != null; }
    public final BCUniform getUv1OverrideUniform() { return Objects.requireNonNull(uv1OverrideUniform, missingUniformMessage("UV1Override")); }
    public final boolean hasUv1OverrideUniform() { return uv1OverrideUniform != null; }
    public final BCUniform getUv2OverrideUniform() { return Objects.requireNonNull(uv2OverrideUniform, missingUniformMessage("UV2Override")); }
    public final boolean hasUv2OverrideUniform() { return uv2OverrideUniform != null; }
    public final BCUniform getBaseColorUniform() { return Objects.requireNonNull(baseColorUniform, missingUniformMessage("BaseColor")); }
    public final boolean hasBaseColorUniform() { return baseColorUniform != null; }
    // @formatter:on
}
