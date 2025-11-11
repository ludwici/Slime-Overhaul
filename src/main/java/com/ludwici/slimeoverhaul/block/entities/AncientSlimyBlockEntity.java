package com.ludwici.slimeoverhaul.block.entities;

import com.ludwici.slimeoverhaul.block.slimy.AncientSlimyBlock;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import static com.ludwici.slimeoverhaul.Content.ANCIENT_SLIMY_BLOCK_ENTITY;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class AncientSlimyBlockEntity extends BlockEntity {
    private static final int BRUSH_COOLDOWN_TICKS = 10;
    private static final int BRUSH_RESET_TICKS = 40;
    private static final int REQUIRED_BRUSHES_TO_BREAK = 10;
    private int brushCount;
    private long brushCountResetsAtTick;
    private long coolDownEndsAtTick;

    public AncientSlimyBlockEntity(BlockPos pos, BlockState blockState) {
        super(ANCIENT_SLIMY_BLOCK_ENTITY.get(), pos, blockState);
    }

    public boolean brush(long startTick, Player player) {
        this.brushCountResetsAtTick = startTick + BRUSH_RESET_TICKS;
        if (startTick >= this.coolDownEndsAtTick && this.level instanceof ServerLevel) {
            this.coolDownEndsAtTick = startTick + BRUSH_COOLDOWN_TICKS;
            int i = this.getCompletionState();
            if (++this.brushCount >= REQUIRED_BRUSHES_TO_BREAK) {
                this.brushingCompleted(player);
                return true;
            } else {
                this.level.scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 2);
                int j = this.getCompletionState();
                if (i != j) {
                    BlockState blockstate = this.getBlockState();
                    BlockState blockstate1 = blockstate.setValue(BlockStateProperties.DUSTED, j);
                    this.level.setBlock(this.getBlockPos(), blockstate1, 3);
                }

                return false;
            }
        } else {
            return false;
        }
    }

    private void brushingCompleted(Player player) {
        if (this.level != null && this.level.getServer() != null) {
            BlockState blockstate = this.getBlockState();
            this.level.levelEvent(3008, this.getBlockPos(), Block.getId(blockstate));
            Block block;
            if (this.getBlockState().getBlock() instanceof AncientSlimyBlock ancientSlimyBlock) {
                dropContent(player);
                ancientSlimyBlock.applyEffect(player, this);
                block = ancientSlimyBlock.getTurnsInto();
            } else {
                block = Blocks.AIR;
            }

            this.level.setBlock(this.worldPosition, block.defaultBlockState(), 3);
        }
    }

    private void dropContent(Player player) {
        if (this.level == null || this.level.getServer() == null) {
            return;
        }

        ItemStack item = unpackLootTable(player);
        if (item.isEmpty()) {
            return;
        }

        Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), item);
    }

    private ItemStack unpackLootTable(Player player) {
        ResourceKey<LootTable> lootTableKey = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(MODID, "ancient/" + BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock()).getPath()));
        LootTable table = level.getServer().reloadableRegistries().getLootTable(lootTableKey);
        if (player instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.GENERATE_LOOT.trigger(serverplayer, lootTableKey);
        }
        LootParams lootparams = new LootParams.Builder((ServerLevel)this.level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition))
                .withLuck(player.getLuck())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .create(LootContextParamSets.CHEST);
        ObjectArrayList<ItemStack> objectarraylist = table.getRandomItems(lootparams);
        ItemStack item = switch (objectarraylist.size()) {
            case 0 -> ItemStack.EMPTY;
            default -> objectarraylist.getFirst();
        };

        setChanged();

        return item;
    }

    public void checkReset() {
        if (this.level != null) {
            if (this.brushCount != 0 && this.level.getGameTime() >= this.brushCountResetsAtTick) {
                int i = this.getCompletionState();
                this.brushCount = Math.max(0, this.brushCount - 2);
                int j = this.getCompletionState();
                if (i != j) {
                    this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(BlockStateProperties.DUSTED, j), 3);
                }

                this.brushCountResetsAtTick = this.level.getGameTime() + 4L;
            }

            if (this.brushCount == 0) {
                this.brushCountResetsAtTick = 0L;
                this.coolDownEndsAtTick = 0L;
            } else {
                this.level.scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 2);
            }
        }
    }

    private int getCompletionState() {
        if (this.brushCount == 0) {
            return 0;
        } else if (this.brushCount < 3) {
            return 1;
        } else {
            return this.brushCount < 6 ? 2 : 3;
        }
    }
}
