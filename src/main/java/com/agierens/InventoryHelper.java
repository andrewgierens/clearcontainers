package com.agierens;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class InventoryHelper {
    public static int SimpleClear(ServerPlayerEntity player, BlockEntity block) {
        var inventory = HopperBlockEntity.getInventoryAt(player.world, block.getPos());
        if (inventory == null) return 0;

        int amount = inventory.size() - inventory.count(Items.AIR);
        inventory.clear();

        return amount;
    }
    public static int SimpleClear(VehicleInventory entity) {
        if (entity == null) return 0;

        int amount = entity.size() - entity.count(Items.AIR);
        entity.clear();

        return amount;
    }
}
