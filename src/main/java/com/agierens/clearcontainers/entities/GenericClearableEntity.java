package com.agierens.clearcontainers.entities;

import com.agierens.clearcontainers.InventoryHelper;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record GenericClearableEntity(EntityType<? extends VehicleInventory> blockType) implements IClearableEntity {
    @Override
    public int clear(World world, VehicleInventory entity) {
        return InventoryHelper.SimpleClear(entity);
    }
}
