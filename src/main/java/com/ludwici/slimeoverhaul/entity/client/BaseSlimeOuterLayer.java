package com.ludwici.slimeoverhaul.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BaseSlimeOuterLayer extends RenderLayer<BaseSlimeRenderState, SlimeModel> {
    private final SlimeModel model;

    public BaseSlimeOuterLayer(RenderLayerParent<BaseSlimeRenderState, SlimeModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new SlimeModel(modelSet.bakeLayer(ModelLayers.SLIME_OUTER));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, BaseSlimeRenderState state, float yRot, float xRot) {
        boolean appearsGlowingWithInvisibility = state.appearsGlowing() && state.isInvisible;
        Identifier texture = BaseSlimeRenderer.getTextureLocationByState(state);
        if (!state.isInvisible || appearsGlowingWithInvisibility) {
            int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
            if (appearsGlowingWithInvisibility) {
                submitNodeCollector.order(1)
                        .submitModel(
                                this.model, state, poseStack, RenderTypes.outline(texture), lightCoords, overlayCoords, state.outlineColor, null
                        );
            } else {
                submitNodeCollector.order(1)
                        .submitModel(
                                this.model,
                                state,
                                poseStack,
                                RenderTypes.entityTranslucent(texture),
                                lightCoords,
                                overlayCoords,
                                state.outlineColor,
                                null
                        );
            }
        }
    }
}
