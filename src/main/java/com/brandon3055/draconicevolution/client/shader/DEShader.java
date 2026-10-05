package com.brandon3055.draconicevolution.client.shader;

import com.brandon3055.brandonscore.client.shader.BCShader;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.Identifier;

public final class DEShader extends BCShader<DEShader> {

    public DEShader(String path, VertexFormat format) {
        super(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, path), format);
    }
}
