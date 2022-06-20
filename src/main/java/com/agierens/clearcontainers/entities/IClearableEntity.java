package com.agierens.clearcontainers.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public interface IClearableEntity {
    EntityType<? extends VehicleInventory> blockType();
    int clear(World world, VehicleInventory entity);
}