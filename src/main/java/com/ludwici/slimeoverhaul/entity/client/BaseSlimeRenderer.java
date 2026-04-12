package com.ludwici.slimeoverhaul.entity.client;

import com.ludwici.slimeoverhaul.entity.custom.BaseSlime;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.entity.AxolotlRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class BaseSlimeRenderer extends MobRenderer<BaseSlime, BaseSlimeRenderState, SlimeModel> {
    public BaseSlimeRenderer(EntityRendererProvider.Context arg) {
        super(arg, new SlimeModel(arg.bakeLayer(ModelLayers.SLIME)), 0.25F);
        this.addLayer(new BaseSlimeOuterLayer(this, arg.getModelSet()));
    }

    protected float getShadowRadius(BaseSlimeRenderState state) {
        return state.size * 0.25F;
    }

    @Override
    protected void scale(BaseSlimeRenderState state, PoseStack poseStack) {
        float s = 0.999F;
        poseStack.scale(s, s, s);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        float size = state.size;
        float ss = state.squish / (size * 0.5F + 1.0F);
        float w = 1.0F / (ss + 1.0F);
        poseStack.scale(w * size, 1.0F / w * size, w * size);
    }

    @Override
    public Identifier getTextureLocation(BaseSlimeRenderState arg) {
        return Identifier.fromNamespaceAndPath(MODID, "textures/entity/slime/" + arg.type + "_slime.png");
    }

    public static Identifier getTextureLocationByState(BaseSlimeRenderState arg) {
        return Identifier.fromNamespaceAndPath(MODID, "textures/entity/slime/" + arg.type + "_slime.png");
    }

    @Override
    public BaseSlimeRenderState createRenderState() {
        return new BaseSlimeRenderState();
    }

    @Override
    public void extractRenderState(BaseSlime entity, BaseSlimeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.squish = Mth.lerp(partialTicks, entity.oSquish, entity.squish);
        state.size = entity.getSize();
        state.type = entity.getSlimeType();
    }
}
