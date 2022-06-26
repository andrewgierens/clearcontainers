package com.agierens;

import com.agierens.blocks.*;
import com.agierens.entities.GenericClearableEntity;
import com.agierens.entities.IClearableEntity;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClearContainers implements ModInitializer {
    private List<IClearableBlock> BlocksToEmpty;
    private List<IClearableEntity> EntitiesToEmpty;

    @Override
    public void onInitialize() {
        this.BlocksToEmpty = Arrays.asList(
                new GenericClearableBlock(BlockEntityType.CHEST),
                new GenericClearableBlock(BlockEntityType.TRAPPED_CHEST),
                new GenericClearableBlock(BlockEntityType.HOPPER),
                new GenericClearableBlock(BlockEntityType.DROPPER),
                new GenericClearableBlock(BlockEntityType.DISPENSER),
                new Furnace()
        );

        this.EntitiesToEmpty = Arrays.asList(
                new GenericClearableEntity(EntityType.CHEST_BOAT),
                new GenericClearableEntity(EntityType.HOPPER_MINECART),
                new GenericClearableEntity(EntityType.CHEST_MINECART)
        );

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            if (!environment.dedicated) {
                dispatcher.register(CommandManager.literal("/clearcontainers").executes(this::clearAllContainers));
            }
        });
    }

    private int clearAllContainers(CommandContext<ServerCommandSource> object) throws CommandSyntaxException {
        var source = (ServerCommandSource) object.getSource();
        var serverPlayer = source.getPlayerOrThrow();
        var serverWorld = serverPlayer.world;
        if (serverWorld == null) return 0;

        var manager = WorldEdit.getInstance().getSessionManager();
        var sessionOwner = FabricAdapter.adaptPlayer(serverPlayer);
        var localSession = manager.get(sessionOwner);
        var worldEditWorld = localSession.getSelectionWorld();
        try {
            var region = localSession.getSelection(worldEditWorld);
            var entities = serverWorld.getEntitiesByClass(Entity.class, new Box(
                    region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ(),
                    region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ()
            ), Objects::nonNull);

            int amountTotal = 0;
            for (var point : region) {
                var blockPos = new BlockPos(point.getBlockX(), point.getBlockY(), point.getBlockZ());
                var block = serverWorld.getBlockEntity(blockPos);

                if (block != null) {
                    var blockToEmpty = this.BlocksToEmpty.stream()
                            .filter(z -> z.blockType() == block.getType())
                            .findFirst()
                            .orElse(null);

                    if (blockToEmpty == null) continue;

                    amountTotal += blockToEmpty.clear(serverWorld, serverPlayer, block);
                }
            }

            if (!entities.isEmpty()) {
                for (var entity : entities) {
                    if (!(entity instanceof VehicleInventory vehicleInventory)) continue;
                    var entitiesToEmpty = this.EntitiesToEmpty.stream()
                            .filter(z -> z.blockType() == entity.getType())
                            .findFirst()
                            .orElse(null);

                    if (entitiesToEmpty == null) continue;

                    amountTotal += entitiesToEmpty.clear(serverWorld, vehicleInventory);
                }
            }

            sessionOwner.print(TextComponent.of(String.format("Cleared %d items", amountTotal)));
        } catch (IncompleteRegionException ex) {
            sessionOwner.printError(TextComponent.of("Please make a region selection first."));
            return 0;
        }
        return 1;
    }
}
