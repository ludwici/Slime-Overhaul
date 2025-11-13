package com.ludwici.slimeoverhaul.event;

import com.ludwici.slimeoverhaul.block.entities.AncientSlimyBlockEntity;
import com.ludwici.slimeoverhaul.block.slimy.AncientSlimyBlock;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class SlimyBlockExecute extends BlockEvent implements ICancellableEvent {

    private final Player player;
    private final AncientSlimyBlockEntity blockEntity;

    public SlimyBlockExecute(Player player, AncientSlimyBlockEntity blockEntity) {
        super(player.level(), blockEntity.getBlockPos(), blockEntity.getBlockState());
        this.blockEntity = blockEntity;
        this.player = player;
    }

    public AncientSlimyBlock getBlock() {
        return (AncientSlimyBlock) blockEntity.getBlockState().getBlock();
    }

    public Player getPlayer() {
        return player;
    }

    public AncientSlimyBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
