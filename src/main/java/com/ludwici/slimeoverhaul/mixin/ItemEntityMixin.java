package com.ludwici.slimeoverhaul.mixin;

import com.ludwici.slimeoverhaul.entity.custom.elementals.FlameSlime;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.ludwici.slimeoverhaul.Content.*;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public abstract ItemStack getItem();

    @Shadow public abstract boolean fireImmune();

    @Shadow public abstract boolean hurt(DamageSource source, float amount);

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "setUnderwaterMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"), cancellable = true)
    protected void checkEarthSlimeItemsInWater(CallbackInfo ci) {
        if (getItem().is(EARTH_SLIME_BALL.get()) || getItem().is(EARTH_SLIME_BLOCK.get().asItem())) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x * (double)0.99F, vec3.y - (double)(vec3.y < (double)-0.06F ? 5.0E-4F : 0.0F), vec3.z * (double)0.99F);
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    protected void checkFireSlimeBallInWater(CallbackInfo ci) {
        if (!level().isClientSide()) {
            if (getItem().is(FIRE_SLIME_BALL.get())) {
                if (isInWater()) {
                    discard();
                }
            }

            if (!this.fireImmune()) {
                for (var entity : this.level().getEntitiesOfClass(FlameSlime.class, this.getBoundingBox().inflate(0.5, 0.0, 0.5), flameSlime -> !flameSlime.isTiny())) {
                    this.igniteForSeconds(5);
                }
            }
        }
    }
}
