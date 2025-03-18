package com.avaris.avarisgates.common.dungeon;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class DungeonRoom {
    private final HashMap<BlockPos, BlockState> blockStates;
    private ServerWorld world;
    private BlockPos basePos;
    public static final int SIDE_LENGTH = 32 + 1; // 8x8 chunks
    public DungeonRoom(HashMap<BlockPos, BlockState> blockStates) {
        this.blockStates = blockStates;
    }

    public void spawn() {
        for(Map.Entry<BlockPos, BlockState> entry : blockStates.entrySet()){
            BlockPos pos = entry.getKey().add(basePos);
            world.setBlockState(pos,entry.getValue());
        }
    }

    public void setBasePos(ServerWorld world,BlockPos basePos) {
        this.world = world;
        this.basePos = basePos;
    }

    public void despawn() {
        for(Map.Entry<BlockPos, BlockState> entry : blockStates.entrySet()){
            BlockPos pos = entry.getKey().add(basePos);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }
}
