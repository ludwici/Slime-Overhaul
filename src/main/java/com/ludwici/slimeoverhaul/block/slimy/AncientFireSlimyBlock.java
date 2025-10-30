package com.ludwici.slimeoverhaul.block.slimy;

import com.ludwici.slimeoverhaul.block.entities.AncientSlimyBlockEntity;
import com.ludwici.slimeoverhaul.effect.HandBurnEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import static com.ludwici.slimeoverhaul.Content.HAND_BURN_EFFECT;

public class AncientFireSlimyBlock extends AncientSlimyBlock {
    public AncientFireSlimyBlock(Properties properties) {
        super(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, SoundEvents.BRUSH_GRAVEL, SoundEvents.BRUSH_GRAVEL_COMPLETED, properties);
    }

    @Override
    public void applyEffect(Player player, AncientSlimyBlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        int behaviour = level.random.nextIntBetweenInclusive(1, 3);
        if (behaviour == 1) {
            var effect = player.getEffect(HAND_BURN_EFFECT.getHolder());
            int duration = 1200;
            if (effect != null) {
                int amplifier = effect.getAmplifier();
                duration = HandBurnEffect.getDuration(amplifier);

                player.addEffect(new MobEffectInstance(HAND_BURN_EFFECT.getHolder(), duration, Math.min(amplifier + 1, 2)));
            } else {
                player.addEffect(new MobEffectInstance(HAND_BURN_EFFECT.getHolder(), duration, 0));
            }
        } else if (behaviour == 2) {
            BlockPos playerPos = player.blockPosition();
            if (level.getBlockState(playerPos).isAir()) {
                level.setBlock(playerPos, Blocks.FIRE.defaultBlockState(), 3);
            }
        } else if (behaviour == 3) {
            int posX = level.random.nextIntBetweenInclusive(-2, 3);
            int posZ = level.random.nextIntBetweenInclusive(-2, 3);

            BlockPos groundPos = blockEntity.getBlockPos().offset(posX, 0, posZ);
            while (level.getBlockState(groundPos).isAir() && groundPos.getY() > level.getMinBuildHeight()) {
                groundPos = groundPos.below();
            }

            if (level.getBlockState(groundPos.above()).isAir()) {
                level.setBlock(groundPos.above(), Blocks.FIRE.defaultBlockState(), 3);
            }
        }
    }
}
