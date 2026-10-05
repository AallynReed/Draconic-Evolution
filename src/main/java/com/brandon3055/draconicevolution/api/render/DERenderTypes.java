package com.brandon3055.draconicevolution.api.render;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * Created by brandon3055 on 22/01/2023
 */
public class DERenderTypes {

//    public static final RenderType MODULE_TYPE = RenderType.create("module_type", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
//            .setShaderState(new RenderStateShard.ShaderStateShard(() -> BCShaders.posColourTexAlpha0))
//            .setTextureState(new RenderStateShard.TextureStateShard(ModuleTextures.LOCATION_MODULE_TEXTURE, false, false))
//            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
//            .createCompositeState(false)
//    );

    //Broken?
//    public static final RenderType TRANS_COLOUR_TYPE = RenderType.create("de_trans_colour", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256,
//            RenderType.CompositeState.builder()
//                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader))
//                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
//                    .setCullState(RenderStateShard.NO_CULL)
//                    .createCompositeState(false)
//    );

    public static final RenderType BOX_NO_DEPTH = RenderType.create("de:box_no_depth", RenderSetup.builder(RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "pipeline/box_no_depth"))
                    .withDepthStencilState(Optional.empty())
                    .build())
            .bufferSize(256)
            .sortOnUpload()
            .createRenderSetup()
    );

    public static final RenderType OUTLINE_TYPE = RenderType.create("de:outline", RenderSetup.builder(RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "pipeline/outline"))
                    .withDepthStencilState(Optional.empty())
                    .build())
            .bufferSize(256)
            .createRenderSetup()
    );

}
