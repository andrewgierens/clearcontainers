package com.agierens.clearcontainers;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InventoryHelper {
    public static int SimpleClear(World world, ServerPlayerEntity player, BlockPos blockPos) {
        var inventory = HopperBlockEntity.getInventoryAt(player.world, blockPos);
        var block = world.getBlockEntity(blockPos);
        if (block == null || inventory == null) return 0;

        int amount = inventory.size() - inventory.count(Items.AIR);
        inventory.clear();

        return amount;
    }
}
