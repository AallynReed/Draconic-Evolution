package com.brandon3055.draconicevolution.client.render.item;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.model.OBJParser;
import codechicken.lib.vec.Matrix4;
import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.draconicevolution.DraconicEvolution;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

/**
 * Created by brandon3055 on 22/5/20.
 */
public class RenderModularAxe extends ToolRenderBase {

    private final ToolPart basePart;
    private final ToolPart materialPart;
    private final ToolPart gemPart;
    private final ToolPart tracePart;
    private final ToolPart bladePart;

    public RenderModularAxe(TechLevel techLevel) {
        super(techLevel, "axe");
        Map<String, CCModel> model = new OBJParser(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "models/item/equipment/axe.obj")).ignoreMtl().parse();
        basePart = basePart(model.get("handle").backfacedCopy());
        materialPart = materialPart(model.get("head").backfacedCopy());
        gemPart = gemPart(model.get("gem").backfacedCopy());
        tracePart = tracePart(model.get("trace").backfacedCopy());
        bladePart = bladePart(model.get("blade").backfacedCopy());
    }

    @Override
    public void renderTool(CCRenderState ccrs, ItemStack stack, ItemDisplayContext context, Matrix4 mat, SubmitNodeCollector collector, boolean gui) {
        transform(mat, 0.25, 0.25, 0.5, gui ? 1.05 : 1.125);

        basePart.render(context, collector, mat);
        materialPart.render(context, collector, mat);
        tracePart.render(context, collector, mat);
        gemPart.render(context, collector, mat);
        bladePart.render(context, collector, mat);
    }

    //@formatter:off //This is not cursed at all! idk what your talking about!
    public static class AXE_WYVERN extends RenderModularAxe { public AXE_WYVERN() {super(TechLevel.WYVERN);}}
    public static class AXE_DRACONIC extends RenderModularAxe { public AXE_DRACONIC() {super(TechLevel.DRACONIC);}}
    public static class AXE_CHAOTIC extends RenderModularAxe { public AXE_CHAOTIC() {super(TechLevel.CHAOTIC);}}
    //@formatter::on
}
