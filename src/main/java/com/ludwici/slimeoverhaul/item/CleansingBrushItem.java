package com.ludwici.slimeoverhaul.item;

import com.ludwici.slimeoverhaul.block.entities.AncientSlimyBlockEntity;
import com.ludwici.slimeoverhaul.block.slimy.AncientSlimyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CleansingBrushItem extends BrushItem {

    public CleansingBrushItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration >= 0 && livingEntity instanceof Player player) {
            HitResult hitresult = this.calculateHitResult(player);
            if (hitresult instanceof BlockHitResult blockhitresult && hitresult.getType() == HitResult.Type.BLOCK) {
                int i = this.getUseDuration(stack, livingEntity) - remainingUseDuration + 1;
                boolean flag = i % 10 == 5;
                if (flag) {
                    BlockPos blockpos = blockhitresult.getBlockPos();
                    BlockState blockstate = level.getBlockState(blockpos);
                    HumanoidArm humanoidarm = livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND
                            ? player.getMainArm()
                            : player.getMainArm().getOpposite();
                    if (blockstate.shouldSpawnTerrainParticles() && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                        this.spawnDustParticles(level, blockhitresult, blockstate, livingEntity.getViewVector(0.0F), humanoidarm);
                    }

                    SoundEvent soundevent;
                    if (blockstate.getBlock() instanceof AncientSlimyBlock ancientSlimyBlock) {
                        soundevent = ancientSlimyBlock.getBrushSound();
                    } else {
                        soundevent = SoundEvents.BRUSH_GENERIC;
                    }

                    level.playSound(player, blockpos, soundevent, SoundSource.BLOCKS);
                    if (!level.isClientSide() && level.getBlockEntity(blockpos) instanceof AncientSlimyBlockEntity ancientSlimyBlockEntity) {
                        boolean flag1 = ancientSlimyBlockEntity.brush(level.getGameTime(), player);
                        if (flag1) {
                            EquipmentSlot equipmentslot = stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND))
                                    ? EquipmentSlot.OFFHAND
                                    : EquipmentSlot.MAINHAND;
                            stack.hurtAndBreak(1, livingEntity, equipmentslot);
                            if (stack.isEmpty()) {
                                player.getInventory().add(new ItemStack(Items.BRUSH));
                            }
                        }
                    }
                }

                return;
            }

            livingEntity.releaseUsingItem();
        } else {
            livingEntity.releaseUsingItem();
        }
    }

    private HitResult calculateHitResult(Player player) {
        return ProjectileUtil.getHitResultOnViewVector(
                player, p_281111_ -> !p_281111_.isSpectator() && p_281111_.isPickable(), player.blockInteractionRange()
        );
    }

    private void spawnDustParticles(Level level, BlockHitResult hitResult, BlockState state, Vec3 pos, HumanoidArm arm) {
        double d0 = 3.0;
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        int j = level.getRandom().nextInt(7, 12);
        BlockParticleOption blockparticleoption = new BlockParticleOption(ParticleTypes.BLOCK, state);
        Direction direction = hitResult.getDirection();
        CleansingBrushItem.DustParticlesDelta brushitem$dustparticlesdelta = CleansingBrushItem.DustParticlesDelta.fromDirection(pos, direction);
        Vec3 vec3 = hitResult.getLocation();

        for (int k = 0; k < j; k++) {
            level.addParticle(
                    blockparticleoption,
                    vec3.x - (double)(direction == Direction.WEST ? 1.0E-6F : 0.0F),
                    vec3.y,
                    vec3.z - (double)(direction == Direction.NORTH ? 1.0E-6F : 0.0F),
                    brushitem$dustparticlesdelta.xd() * (double)i * 3.0 * level.getRandom().nextDouble(),
                    0.0,
                    brushitem$dustparticlesdelta.zd() * (double)i * 3.0 * level.getRandom().nextDouble()
            );
        }
    }

    record DustParticlesDelta(double xd, double yd, double zd) {
        private static final double ALONG_SIDE_DELTA = 1.0;
        private static final double OUT_FROM_SIDE_DELTA = 0.1;

        public static CleansingBrushItem.DustParticlesDelta fromDirection(Vec3 pos, Direction direction) {
            double d0 = 0.0;

            return switch (direction) {
                case DOWN, UP -> new CleansingBrushItem.DustParticlesDelta(pos.z(), 0.0, -pos.x());
                case NORTH -> new CleansingBrushItem.DustParticlesDelta(1.0, 0.0, -0.1);
                case SOUTH -> new CleansingBrushItem.DustParticlesDelta(-1.0, 0.0, 0.1);
                case WEST -> new CleansingBrushItem.DustParticlesDelta(-0.1, 0.0, -1.0);
                case EAST -> new CleansingBrushItem.DustParticlesDelta(0.1, 0.0, 1.0);
            };
        }
    }
}
