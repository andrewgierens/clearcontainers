package com.agierens.clearcontainers.blocks;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IClearableBlock {
    BlockEntityType<? extends Inventory> blockType();
    int clear(World world, ServerPlayerEntity player, BlockEntity block);
}
