package com.brandon3055.draconicevolution.client.render;

import codechicken.lib.gui.modular.lib.GuiRender;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;

/**
 * Renders 3D models into a GUI area, in GUI coordinates (z towards the viewer), as the old GuiRender buffers allowed.
 */
public class GuiModelRenderer extends PictureInPictureRenderer<GuiModelRenderer.State> {

    public GuiModelRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
    }

    public static void register(RegisterPictureInPictureRenderersEvent event) {
        event.register(State.class, GuiModelRenderer::new);
    }

    /**
     * Bounds and model are in {@code guiRender.pose()} space.
     */
    public static void submit(GuiRender guiRender, int x0, int y0, int x1, int y1, ModelRender render) {
        GuiGraphicsExtractor graphics = guiRender.guiGraphics();
        Matrix4f m = guiRender.pose().last().pose();
        Matrix3x2f pose = new Matrix3x2f(graphics.pose()).mul(new Matrix3x2f(m.m00(), m.m01(), m.m10(), m.m11(), m.m30(), m.m31()));
        ScreenRectangle scissor = graphics.peekScissorStack();
        ScreenRectangle bounds = new ScreenRectangle(x0, y0, x1 - x0, y1 - y0).transformMaxBounds(pose);
        graphics.submitPictureInPictureRenderState(new State(render, x0, y0, x1, y1, pose, scissor, scissor != null ? scissor.intersection(bounds) : bounds));
    }

    @Override
    public Class<State> getRenderStateClass() {
        return State.class;
    }

    @Override
    protected void renderToTexture(State state, PoseStack poseStack) {
        Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
        poseStack.translate(-(state.x1() - state.x0()) / 2F, 0, 0);
        poseStack.scale(1, 1, -1);
        poseStack.translate(-state.x0(), -state.y0(), 0);
        state.render().render(poseStack, bufferSource);
    }

    @Override
    protected float getTranslateY(int height, int guiScale) {
        return 0;
    }

    @Override
    protected String getTextureLabel() {
        return "draconicevolution model";
    }

    public interface ModelRender {
        void render(PoseStack poseStack, MultiBufferSource.BufferSource buffers);
    }

    public record State(ModelRender render, int x0, int y0, int x1, int y1, Matrix3x2f pose, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState {
        @Override
        public float scale() {
            return 1;
        }
    }
}
