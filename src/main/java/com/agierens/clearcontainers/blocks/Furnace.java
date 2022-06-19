package com.agierens.clearcontainers.blocks;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Furnace implements ClearableBlock {
    @Override
    public BlockEntityType<? extends LockableContainerBlockEntity> getBlockType() {
        return BlockEntityType.FURNACE;
    }

    @Override
    public int clear(World world, ServerPlayerEntity player, BlockPos blockPos) {
        var blockState = world.getBlockState(blockPos);
        var inventory = HopperBlockEntity.getInventoryAt(player.world, blockPos);
        if (inventory == null) return 0;

        var newBlockState = (BlockState)blockState.with(AbstractFurnaceBlock.LIT, false);
        world.setBlockState(blockPos, newBlockState);

        int amount = inventory.size();
        inventory.clear();

        return amount;
    }
}
