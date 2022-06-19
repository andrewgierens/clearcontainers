package com.agierens.clearcontainers;

import com.agierens.clearcontainers.blocks.*;
import com.agierens.clearcontainers.blocks.Hopper;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class ClearContainers implements ModInitializer {
    private List<ClearableBlock> BlocksToEmpty;

    @Override
    public void onInitialize() {
        this.BlocksToEmpty = Arrays.asList(new Chest(), new Dispenser(), new Dropper(), new Furnace(), new Hopper());

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            dispatcher.register(CommandManager.literal("clearcontainers").executes(context -> {
                var serverPlayer = context.getSource().getPlayerOrThrow();
                var serverWorld = serverPlayer.world;
                if (serverWorld == null) return 0;

                var manager = WorldEdit.getInstance().getSessionManager();
                var sessionOwner = FabricAdapter.adaptPlayer(serverPlayer);
                var localSession = manager.get(sessionOwner);
                var worldEditWorld = localSession.getSelectionWorld();
                try {
                    var region = localSession.getSelection(worldEditWorld);
                    int amountTotal = 0;
                    for (var point : region) {
                        var blockPos = new BlockPos(point.getBlockX(), point.getBlockY(), point.getBlockZ());
                        var block = serverWorld.getBlockEntity(blockPos);
                        if (block == null) continue;

                        var blockToEmpty = this.BlocksToEmpty.stream()
                                .filter(z -> z.getBlockType() == block.getType())
                                .findFirst()
                                .orElse(null);

                        if (blockToEmpty == null) continue;

                        amountTotal += blockToEmpty.clear(serverWorld, serverPlayer, blockPos);
                    }

                    sessionOwner.print(TextComponent.of(String.format("Cleared %d items", amountTotal)));
                } catch (IncompleteRegionException ex) {
                    sessionOwner.printError(TextComponent.of("Please make a region selection first."));
                    return 0;
                }
                return 1;
            }));
        });
    }
}
