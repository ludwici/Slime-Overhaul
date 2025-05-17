package com.ludwici.slimeoverhaul.mixin;

import com.ludwici.slimeoverhaul.PlayerKeyPressObserver;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.DOUBLE_JUMP_EFFECT;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerKeyPressObserver {
//    @Shadow protected boolean jumping;
//    @Shadow private int noJumpDelay;

//    @Shadow protected abstract void moveCloak();

    @Shadow
    public abstract void jumpFromGround();

    @Shadow
    protected int jumpTriggerTime;
    @Unique
    private boolean canDoubleJump = false;
    @Unique
    private boolean jumpKeyPressed = false;

    @Override
    public void setJumpKeyPressed(boolean pressed) {
        this.jumpKeyPressed = pressed;
    }

    @Override
    public boolean isJumpKeyPressed() {
        return jumpKeyPressed;
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
//        if (!canDoubleJump) {
//            return;
//        }
//
//        if (!jumpKeyPressed) {
//            return;
//        }
//
//        Player self = (Player) (Object) this;
//
//        if (self.isCreative() || self.isSpectator() || self.onGround() || self.onClimbable() || self.isInWaterOrBubble() || ElytraItem.isFlyEnabled(self.getItemBySlot(EquipmentSlot.CHEST)) || self.isPassenger()) {
//            return;
//        }
//
//        if (!self.hasEffect(DOUBLE_JUMP_EFFECT.getHolder())) {
//            return;
//        }
//
//        this.jumpFromGround();
//
//        canDoubleJump = false;
//        jumpKeyPressed = false;

    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"))
    private void onJumpFromGround(CallbackInfo ci) {
//        LivingEntity self = (LivingEntity) (Object) this;
//        this.canDoubleJump = true;

//        if (self.hasEffect(DOUBLE_JUMP_EFFECT.getHolder())) {
//            this.canDoubleJump = true;
////            this.hasDoubleJump = false;
//        }
    }
}
