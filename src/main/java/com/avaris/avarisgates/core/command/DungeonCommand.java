package com.avaris.avarisgates.core.command;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.dim.ModDimensions;
import com.avaris.avarisgates.core.dungeon.Dungeon;
import com.avaris.avarisgates.core.dungeon.DungeonManager;
import com.avaris.avarisgates.core.dungeon.DungeonRoom;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class DungeonCommand {
    public static void init(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("dungeon")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(literal("info").executes(DungeonCommand::sendDungeonInfo))
                .then(literal("test").executes(DungeonCommand::spiralTest))
                .then(literal("remove").then(argument("id",UuidArgumentType.uuid()).executes(DungeonCommand::removeDungeon)))
                .then(literal("dim").executes(DungeonCommand::teleportToDim))
        ));
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

    private static HashMap<BlockPos, BlockState> TEST_ROOM = new HashMap<>();
    static {
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            TEST_ROOM.put(new BlockPos(i,0,0),Blocks.BASALT.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            TEST_ROOM.put(new BlockPos(i,0,DungeonRoom.SIDE_LENGTH),Blocks.BASALT.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            TEST_ROOM.put(new BlockPos(0,0,i),Blocks.NETHER_BRICKS.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            TEST_ROOM.put(new BlockPos(DungeonRoom.SIDE_LENGTH,0,i),Blocks.NETHER_BRICKS.getDefaultState());
        }
        for (int i = 1;i < DungeonRoom.SIDE_LENGTH - 1;i++){
            for (int j = 1;j < DungeonRoom.SIDE_LENGTH - 1;j++) {
                TEST_ROOM.put(new BlockPos(i, 1, j), Blocks.STONE_BRICKS.getDefaultState());
            }
        }
    }
    private static final HashMap<Point, DungeonRoom> TEST_ROOMS = new HashMap<>();
    static {
        for (int i = 0;i < (Dungeon.SIDE_LENGTH - 1) / (DungeonRoom.SIDE_LENGTH - 1);i++){
            for (int j = 0;j < (Dungeon.SIDE_LENGTH - 1) / (DungeonRoom.SIDE_LENGTH - 1);j++){
                TEST_ROOMS.put(new Point(i,j),new DungeonRoom(TEST_ROOM));
            }
        }
    }

    private static int spiralTest(CommandContext<ServerCommandSource> context) {
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
