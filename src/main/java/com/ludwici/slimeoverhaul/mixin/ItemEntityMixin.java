package com.ludwici.slimeoverhaul.mixin;

import com.ludwici.slimeoverhaul.entity.custom.elementals.FlameSlime;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.ludwici.slimeoverhaul.Content.*;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public abstract ItemStack getItem();

    @Shadow public abstract boolean fireImmune();

    @Shadow public abstract boolean hurt(DamageSource source, float amount);

    @Unique
    private Vec3 yourmod$previousPosition = null;

    @Unique
    private boolean yourmod$hasCollided = false;

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "setUnderwaterMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"), cancellable = true)
    protected void checkEarthSlimeItemsInWater(CallbackInfo ci) {
        if (getItem().is(EARTH_ITEMS_TAG)) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x * (double)0.99F, vec3.y - (double)(vec3.y < (double)-0.06F ? 5.0E-4F : 0.0F), vec3.z * (double)0.99F);
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    protected void checkFireSlimeBallInWater(CallbackInfo ci) {
        if (!level().isClientSide()) {
            if (getItem().is(FIRE_ITEMS_TAG)) {
                if (isInWater()) {
                    discard();
                }
            }

            if (!this.fireImmune()) {
                for (var ignored : this.level().getEntitiesOfClass(FlameSlime.class, this.getBoundingBox().inflate(0.5, 0.0, 0.5), flameSlime -> !flameSlime.isTiny())) {
                    this.igniteForSeconds(5);
                }
            }

            if (getItem().is(SLIME_COAT_ITEMS_TAG)) {
                if (yourmod$previousPosition == null) {
                    yourmod$previousPosition = position();
                }
            }

//            if (onGround()) {
//                System.out.println("OnGround");
//            }
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", shift = At.Shift.AFTER))
    protected void checkCollisions(CallbackInfo ci) {
        ItemEntity self = (ItemEntity)(Object)this;

        if (!self.getItem().is(SLIME_COAT_ITEMS_TAG)) {
            return;
        }

        if (yourmod$hasCollided) {
            return;
        }

        Vec3 currentPos = self.position();
        Vec3 deltaMovement = self.getDeltaMovement();

        boolean hasLanded = self.onGround();
        boolean hitWall = self.horizontalCollision;
        boolean hitCeiling = self.verticalCollision && deltaMovement.y <= 0;

        boolean suddenStop = false;
        if (yourmod$previousPosition != null) {
            double speed = deltaMovement.lengthSqr();
            double distanceMoved = currentPos.distanceToSqr(yourmod$previousPosition);
            suddenStop = speed < 0.001 && distanceMoved < 0.01 && !hasLanded;
        }

        if (hasLanded || hitWall || hitCeiling || suddenStop) {
            yourmod$placeBlock(self);
        }

        yourmod$previousPosition = currentPos;
    }

    @Unique
    private void yourmod$placeBlock(ItemEntity self) {
        if (yourmod$hasCollided) {
            return;
        }

        if (self.level().isClientSide) {
            return;
        }

        BlockPos targetPos;

        if (self.onGround()) {
            targetPos = self.blockPosition();
        } else if (self.horizontalCollision) {
            Vec3 lookDirection = self.getDeltaMovement().normalize();
            targetPos = self.blockPosition().offset(
                    (int)(lookDirection.x),
                    0,
                    (int)(lookDirection.z)
            );
        } else if (self.verticalCollision) {
            targetPos = self.blockPosition();
        } else {
            targetPos = self.blockPosition();
        }

        BlockState blockState = self.level().getBlockState(targetPos);
        if (blockState.isAir() || blockState.canBeReplaced()) {
            self.level().setBlock(targetPos, Blocks.DIRT.defaultBlockState(), 3);
            yourmod$hasCollided = true;
            self.discard();
        }
    }
}
