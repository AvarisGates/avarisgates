package com.avaris.avarisgates.core.dungeon;

import com.avaris.avarisgates.core.Rarity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.*;

public class Dungeon {
    private String name = "TEST";
    private final UUID owner;
    private Rarity rarity = Rarity.COMMON;
    private int level = 1;
    private Point gridPos;
    private State state;
    private final UUID uuid;
    private final HashSet<UUID> currentPlayers = new HashSet<>();
    private final HashSet<UUID> currentEntities = new HashSet<>();
    private ServerWorld world;
    private final HashMap<BlockPos, BlockState> blockStates;
    private final int y;

    private static final int SIDE_LENGTH = 8 * 16 + 1; // 8x8 chunks

    public Dungeon(ServerWorld world, HashMap<BlockPos, BlockState> blockStates, int y, UUID owner){
        this.owner = owner;
        this.blockStates = blockStates;
        this.world = world;
        this.y = y;

        this.state = State.Started;
        this.uuid = UUID.randomUUID();
    }

    public Point getGridPos() {
        return gridPos;
    }

    public void setGridPos(Point pos) {
        this.gridPos = pos;
    }

    public BlockPos getWorldPosition() {
        return new BlockPos(this.gridPos.x * SIDE_LENGTH, this.y,this.gridPos.y * SIDE_LENGTH);
    }

    public void spawn(){
        BlockPos basePos = getWorldPosition();
        for(Map.Entry<BlockPos, BlockState> entry : blockStates.entrySet()){
            BlockPos pos = entry.getKey().add(basePos);
            world.setBlockState(pos,entry.getValue());
        }
    }

    public void onJoin(UUID playerUUID){
        currentPlayers.add(playerUUID);
        if(playerUUID == owner){
            state = State.Running;
        }
    }

    public void onLeave(UUID playerUUID){
        currentPlayers.remove(playerUUID);
        if(playerUUID == owner){
            state = state == State.Completed ? State.Completed : State.Left;
            if(state == State.Completed){
                DungeonManager.removeDungeon(this.uuid);
            }
        }
    }

    public void onRemove() {
        for(UUID id : currentEntities){
            Objects.requireNonNull(world.getEntity(id)).discard();
        }
        BlockPos basePos = getWorldPosition();
        for(Map.Entry<BlockPos, BlockState> entry : blockStates.entrySet()){
            BlockPos pos = entry.getKey().add(basePos);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    public Collection<UUID> getCurrentPlayers(){
        return currentPlayers;
    }

    public Collection<UUID> getCurrentEntities(){
        return currentEntities;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public enum State{
        Started,
        Running,
        Left,
        Completed
    }
}
