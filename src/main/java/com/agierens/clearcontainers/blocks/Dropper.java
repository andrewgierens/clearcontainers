package com.agierens.clearcontainers.blocks;

import com.agierens.clearcontainers.InventoryHelper;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Dropper implements ClearableBlock {
    @Override
    public BlockEntityType<? extends LockableContainerBlockEntity> getBlockType() {
        return BlockEntityType.DROPPER;
    }

    @Override
    public int clear(World world, ServerPlayerEntity player, BlockPos blockPos) {
        return InventoryHelper.SimpleClear(world, player, blockPos);
    }
}