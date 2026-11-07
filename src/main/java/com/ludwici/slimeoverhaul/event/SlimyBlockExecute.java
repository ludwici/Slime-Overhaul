package com.ludwici.slimeoverhaul.event;

import com.ludwici.slimeoverhaul.block.entities.AncientSlimyBlockEntity;
import com.ludwici.slimeoverhaul.block.slimy.AncientSlimyBlock;
import net.minecraft.world.entity.player.Player;

public final class SlimyBlockExecute {
    private final Player player;
    private final AncientSlimyBlockEntity blockEntity;
    private boolean canceled;

    public SlimyBlockExecute(Player player, AncientSlimyBlockEntity blockEntity) {
        this.player = player;
        this.blockEntity = blockEntity;
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

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
