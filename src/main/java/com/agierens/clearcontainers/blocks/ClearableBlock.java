package com.agierens.clearcontainers.blocks;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ClearableBlock {
    BlockEntityType<? extends LockableContainerBlockEntity> getBlockType();
    int clear(World world, ServerPlayerEntity player, BlockPos blockPos);
}
