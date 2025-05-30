package com.ludwici.slimeoverhaul.entity.client;

import com.ludwici.slimeoverhaul.entity.custom.BaseSlime;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class BaseSlimeRenderer extends MobRenderer<BaseSlime, SlimeModel<BaseSlime>> {

    public BaseSlimeRenderer(EntityRendererProvider.Context arg) {
        super(arg, new SlimeModel<>(arg.bakeLayer(ModelLayers.SLIME)), 0.25F);
        this.addLayer(new SlimeOuterLayer(this, arg.getModelSet()));
    }

    public void render(BaseSlime arg, float f, float g, PoseStack arg2, MultiBufferSource arg3, int i) {
        this.shadowRadius = 0.25F * (float)arg.getSize();
        super.render(arg, f, g, arg2, arg3, i);
    }

    protected void scale(BaseSlime arg, PoseStack arg2, float f) {
        float g = 0.999F;
        arg2.scale(0.999F, 0.999F, 0.999F);
        arg2.translate(0.0F, 0.001F, 0.0F);
        float h = (float)arg.getSize();
        float i = Mth.lerp(f, arg.oSquish, arg.squish) / (h * 0.5F + 1.0F);
        float j = 1.0F / (i + 1.0F);
        arg2.scale(j * h, 1.0F / j * h, j * h);
    }

    @Override
    public ResourceLocation getTextureLocation(BaseSlime arg) {
        return ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/slime/" + arg.getSlimeType() + "_slime.png");
    }
}
