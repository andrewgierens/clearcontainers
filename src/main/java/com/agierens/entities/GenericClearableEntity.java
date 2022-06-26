package com.agierens.entities;

import com.agierens.InventoryHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.world.World;

public record GenericClearableEntity(EntityType<? extends VehicleInventory> blockType) implements IClearableEntity {
    @Override
    public int clear(World world, VehicleInventory entity) {
        return InventoryHelper.SimpleClear(entity);
    }
}
