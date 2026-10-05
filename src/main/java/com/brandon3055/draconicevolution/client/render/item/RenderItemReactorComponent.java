package com.brandon3055.draconicevolution.client.render.item;

import codechicken.lib.math.MathHelper;
import codechicken.lib.model.PerspectiveModelState;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Vector3;
import com.brandon3055.brandonscore.api.TimeKeeper;
import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import com.brandon3055.draconicevolution.client.render.tile.RenderTileReactorComponent;
import com.brandon3055.draconicevolution.client.render.tile.RenderTileReactorCore;
import com.brandon3055.draconicevolution.init.DEContent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Created by brandon3055 on 21/11/2016.
 */
public class RenderItemReactorComponent implements IItemRenderer {
    public RenderItemReactorComponent() {}

    //region Unused

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    //endregion

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext transformType, PoseStack mStack, SubmitNodeCollector collector, int packedLight, int packedOverlay) {
        Matrix4 mat = new Matrix4(mStack);
        Item item = stack.getItem();

        DeltaTracker delta = Minecraft.getInstance().getDeltaTracker();
        if (item == DEContent.REACTOR_CORE.get().asItem()) {
            mat.translate(0.5, 0.5, 0.5);
            mat.scale(1.5);
            RenderTileReactorCore.renderCore(mat, collector, (TimeKeeper.getClientTick() + delta.getGameTimeDeltaPartialTick(false)) / 100F, 0F, 0.F, 0.5F);
        } else if (item == DEContent.REACTOR_STABILIZER.get().asItem()) {
            float coreRotation = (TimeKeeper.getClientTick() + delta.getGameTimeDeltaPartialTick(false)) * 5F;
            mat.translate(0.5, 0, 0.5);
            RenderTileReactorComponent.renderStabilizer(mat, collector, coreRotation, 1F, packedLight, packedOverlay);
        } else if (item == DEContent.REACTOR_INJECTOR.get().asItem()) {
            mat.translate(0.5, 0.5, 0);
            mat.rotate(90 * MathHelper.torad, Vector3.X_POS);
            RenderTileReactorComponent.renderInjector(mat, collector, 1F, packedLight, packedOverlay);
        } else {
            RenderTileReactorComponent.renderComponent(item, mat, collector, packedLight, packedOverlay);
        }
    }

    @Override
    public @Nullable PerspectiveModelState getModelState() {
        return TransformUtils.DEFAULT_BLOCK;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }
}
