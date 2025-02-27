package com.avaris.avarisgates.core.command;

import com.avaris.avarisgates.core.dungeon.Dungeon;
import com.avaris.avarisgates.core.dungeon.DungeonManager;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

import static net.minecraft.server.command.CommandManager.literal;

public class DungeonCommand {
    public static void init(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("dungeon")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(literal("info").executes(DungeonCommand::sendDungeonInfo)).then(literal("test").executes(DungeonCommand::spiralTest))
        ));
    }

    private static HashMap<BlockPos, BlockState> TEST = new HashMap<>();
    static {
        for (int i = 0;i < 16 * 8;i++){
            TEST.put(new BlockPos(i,0,0),Blocks.IRON_BLOCK.getDefaultState());
        }
        for (int i = 0;i < 16 * 8;i++){
            TEST.put(new BlockPos(i,0,16*8),Blocks.IRON_BLOCK.getDefaultState());
        }
        for (int i = 0;i < 16 * 8;i++){
            TEST.put(new BlockPos(0,0,i),Blocks.GLOWSTONE.getDefaultState());
        }
        for (int i = 0;i < 16 * 8;i++){
            TEST.put(new BlockPos(16*8,0,i),Blocks.GLOWSTONE.getDefaultState());
        }
    }

    private static int spiralTest(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null){
            return 1;
        }
        ServerWorld world = player.getServerWorld();
        Dungeon dungeon = new Dungeon(world,TEST,((int) player.getY()),player.getUuid());
        DungeonManager.addDungeon(dungeon);
        return 0;
    }

    private static int sendDungeonInfo(CommandContext<ServerCommandSource> context) {
        context.getSource().sendMessage(Text.literal("Dungeon info:\n   Live Dungeons: "+ DungeonManager.getLiveDungeonCount()+"\n   Dungeons Created: "+DungeonManager.getDungeonsCreated()));
        return 0;
    }
}
