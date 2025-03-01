package com.avaris.avarisgates.core.command;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.dim.ModDimensions;
import com.avaris.avarisgates.core.dungeon.Dungeon;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class DungeonCommand {
    public static void init(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("dungeon")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(literal("info").executes(DungeonCommand::sendDungeonInfo))
                .then(literal("test").executes(DungeonCommand::createTestDungeon))
                .then(literal("create").executes(DungeonCommand::createDungeon))
                .then(literal("remove").then(argument("id",UuidArgumentType.uuid()).executes(DungeonCommand::removeDungeon)))
                .then(literal("dim").executes(DungeonCommand::teleportToDim))
        ));
    }

    private static int createDungeon(CommandContext<ServerCommandSource> context) {
        AvarisGates.dungeonManager.removeAllDungeons();
        return createTestDungeon(context);
    }

    private static int removeDungeon(CommandContext<ServerCommandSource> context) {
        UUID id = context.getArgument("id", UUID.class);
        if(AvarisGates.dungeonManager.removeDungeon(id)){
            context.getSource().sendMessage(Text.of("Dungeon Removed: "+id));
        }else{
            context.getSource().sendMessage(Text.of("Dungeon Not Found: "+id));
        }
        return 0;
    }

    private static int teleportToDim(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null){
            return 1;
        }
        player.teleportTo(new TeleportTarget(player.server.getWorld(ModDimensions.DUNGEON_LEVEL_KEY), Vec3d.ZERO,Vec3d.ZERO,player.getYaw(),player.getPitch(),TeleportTarget.NO_OP));
        return 0;
    }

    private static int createTestDungeon(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null){
            return 1;
        }
        ServerWorld world = player.server.getWorld(ModDimensions.DUNGEON_LEVEL_KEY);
        Dungeon dungeon = new Dungeon(world,DungeonGenerator.generateRooms(),0,player.getUuid());
        BlockPos pos = AvarisGates.dungeonManager.addDungeon(dungeon);
        context.getSource().sendMessage(Text.of("New Dungeon Created: "+dungeon.getUuid().toString()+" "+pos));
        return 0;
    }

    private static int sendDungeonInfo(CommandContext<ServerCommandSource> context) {
        context.getSource().sendMessage(Text.literal("Dungeon info:\n   Live Dungeons: "+ AvarisGates.dungeonManager.getLiveDungeonCount()+"\n   Dungeons Created: "+AvarisGates.dungeonManager.getDungeonsCreated()));
        return 0;
    }
}
