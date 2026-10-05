package com.brandon3055.draconicevolution.client.render.item;

import codechicken.lib.colour.Colour;
import codechicken.lib.model.PerspectiveModelState;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.render.model.OBJParser;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.api.TimeKeeper;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.blocks.energynet.EnergyCrystal;
import com.brandon3055.draconicevolution.blocks.energynet.EnergyCrystal.CrystalType;
import com.brandon3055.draconicevolution.client.DEShaders;
import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import com.brandon3055.draconicevolution.client.render.tile.RenderTileEnergyCrystal;
import com.brandon3055.draconicevolution.init.DEContent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.brandon3055.draconicevolution.client.render.tile.RenderTileEnergyCrystal.COLOURS;

/**
 * Created by brandon3055 on 21/11/2016.
 */
public class RenderItemEnergyCrystal implements IItemRenderer {
    public static final RenderType crystalBaseType = RenderTypes.entitySolid(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/models/crystal_base.png"));

    private final CrystalType type;
    private final TechLevel techLevel;
    private final CCModel crystalFull;
    private final CCModel crystalHalf;
    private final CCModel crystalBase;

    public RenderItemEnergyCrystal(CrystalType type, TechLevel techLevel) {
        this.type = type;
        this.techLevel = techLevel;
        Map<String, CCModel> map = new OBJParser(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "models/block/crystal.obj")).quads().ignoreMtl().parse();
        crystalFull = CCModel.combine(map.values()).backfacedCopy();
        map = new OBJParser(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "models/block/crystal_half.obj")).quads().ignoreMtl().parse();
        crystalHalf = map.get("Crystal").backfacedCopy();
        crystalBase = map.get("Base").backfacedCopy();
    }

    //region Unused

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    //endregion

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext context, PoseStack mStack, SubmitNodeCollector collector, int packedLight, int packedOverlay) {
        int tier = techLevel.index;
        Matrix4 mat = new Matrix4(mStack);
        mat.translate(0.5, type == CrystalType.CRYSTAL_IO ? 0 : 0.5, 0.5);
        DEShaders.energyCrystalMipmap.glUniform1f(0);
        DEShaders.energyCrystalColour.glUniform3f(COLOURS[tier][0], COLOURS[tier][1], COLOURS[tier][2]);
        RenderType crystalType = RenderTileEnergyCrystal.crystalType.withCurrentUniforms();
        int colour = Colour.packRGBA(r[tier], g[tier], b[tier], 1F);

        if (type == CrystalType.CRYSTAL_IO) {
            collector.cc$submitCCRS(mat, crystalBaseType, (m, ccrs) -> {
                ccrs.brightness = packedLight;
                ccrs.overlay = packedOverlay;
                crystalBase.render(ccrs, m);
            });
            mat.apply(new Rotation(TimeKeeper.getClientTick() / 400F, 0, 1, 0));
            collector.cc$submitCCRS(mat, crystalType, (m, ccrs) -> {
                ccrs.brightness = packedLight;
                ccrs.overlay = packedOverlay;
                ccrs.baseColour = colour;
                crystalHalf.render(ccrs, m);
            });
        } else {
            mat.apply(new Rotation(TimeKeeper.getClientTick() / 400F, 0, 1, 0));
            collector.cc$submitCCRS(mat, crystalType, (m, ccrs) -> {
                ccrs.brightness = packedLight;
                ccrs.overlay = packedOverlay;
                ccrs.baseColour = colour;
                crystalFull.render(ccrs, m);
            });
        }
    }

    @Override
    public @Nullable PerspectiveModelState getModelState() {
        return TransformUtils.DEFAULT_BLOCK;
    }

    private static float[] r = {0.0F, 0.47F, 1.0F};
    private static float[] g = {0.2F, 0.0F, 0.4F};
    private static float[] b = {0.3F, 0.58F, 0.1F};

    //@formatter:off //This is not cursed at all! idk what your talking about!
    public static class ITEM_BASIC_IO_CRYSTAL extends RenderItemEnergyCrystal { public ITEM_BASIC_IO_CRYSTAL() { super(EnergyCrystal.CrystalType.CRYSTAL_IO, TechLevel.DRACONIUM);}}
    public static class ITEM_WYVERN_IO_CRYSTAL extends RenderItemEnergyCrystal { public ITEM_WYVERN_IO_CRYSTAL() { super(EnergyCrystal.CrystalType.CRYSTAL_IO, TechLevel.WYVERN);}}
    public static class ITEM_DRACONIC_IO_CRYSTAL extends RenderItemEnergyCrystal { public ITEM_DRACONIC_IO_CRYSTAL() { super(EnergyCrystal.CrystalType.CRYSTAL_IO, TechLevel.DRACONIC);}}
    public static class ITEM_BASIC_RELAY_CRYSTAL extends RenderItemEnergyCrystal { public ITEM_BASIC_RELAY_CRYSTAL() { super(EnergyCrystal.CrystalType.RELAY, TechLevel.DRACONIUM);}}
    public static class ITEM_WYVERN_RELAY_CRYSTAL extends RenderItemEnergyCrystal { public ITEM_WYVERN_RELAY_CRYSTAL() { super(EnergyCrystal.CrystalType.RELAY, TechLevel.WYVERN);}}
    public static class ITEM_DRACONIC_RELAY_CRYSTAL extends RenderItemEnergyCrystal { public ITEM_DRACONIC_RELAY_CRYSTAL() { super(EnergyCrystal.CrystalType.RELAY, TechLevel.DRACONIC);}}
    public static class ITEM_BASIC_WIRELESS_CRYSTAL extends RenderItemEnergyCrystal { public ITEM_BASIC_WIRELESS_CRYSTAL() { super(EnergyCrystal.CrystalType.WIRELESS, TechLevel.DRACONIUM);}}
    public static class ITEM_WYVERN_WIRELESS_CRYSTAL extends RenderItemEnergyCrystal { public ITEM_WYVERN_WIRELESS_CRYSTAL() { super(EnergyCrystal.CrystalType.WIRELESS, TechLevel.WYVERN);}}
    public static class ITEM_DRACONIC_WIRELESS_CRYSTAL extends RenderItemEnergyCrystal { public ITEM_DRACONIC_WIRELESS_CRYSTAL() { super(EnergyCrystal.CrystalType.WIRELESS, TechLevel.DRACONIC);}}
    //@formatter:on
}
