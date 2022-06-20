package com.agierens.clearcontainers.blocks;

import com.agierens.clearcontainers.InventoryHelper;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public record GenericClearableBlock(BlockEntityType<? extends Inventory> blockType) implements IClearableBlock {
    @Override
    public int clear(World world, ServerPlayerEntity player, BlockEntity block) {
        return InventoryHelper.SimpleClear(player, block);
    }
}