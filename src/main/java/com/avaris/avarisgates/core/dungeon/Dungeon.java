package com.avaris.avarisgates.core.dungeon;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.Rarity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.*;

//Represents an instance of a dungeon, to manage dungeons see the DungeonManager class
public class Dungeon {
    private final String name = "TEST";
    private final UUID owner;
    private final Rarity rarity = Rarity.COMMON;
    private final int level = 1;
    private Point gridPos;
    private State state;
    private final UUID uuid;
    private final HashSet<UUID> currentPlayers = new HashSet<>();
    private final HashSet<UUID> currentEntities = new HashSet<>();
    private final ServerWorld world;
    private final HashMap<Point,DungeonRoom> rooms;
    private final int y;
    private Box volume;

    public static final int SIDE_LENGTH = 16 * 16 + 2; // 8x8 chunks

    public Dungeon(ServerWorld world, HashMap<Point,DungeonRoom> rooms, int y, UUID owner){
        this.world = world;
        this.rooms = rooms;
        this.y = y;
        this.owner = owner;

        this.state = State.Started;
        this.uuid = UUID.randomUUID();
    }

    public Point getGridPos() {
        return gridPos;
    }

    public void setGridPos(Point pos) {
        this.gridPos = pos;
        this.volume = new Box(Vec3d.of(this.getWorldPosition()),Vec3d.of(this.getWorldPosition().add(SIDE_LENGTH,SIDE_LENGTH,SIDE_LENGTH)));
        AvarisGates.LOGGER.info("{}",this.volume);
    }

    public BlockPos getWorldPosition() {
        return new BlockPos(this.gridPos.x * SIDE_LENGTH, this.y,this.gridPos.y * SIDE_LENGTH);
    }

    public void spawn(){
        BlockPos basePos = getWorldPosition();
        rooms.forEach((point,room) -> room.setBasePos(world,basePos.add(point.x * 32,0,point.y * 32)));
        rooms.forEach(((point, dungeonRoom) -> dungeonRoom.spawn()));
    }

    public void onRemove() {
        for(UUID id : currentEntities){
            Objects.requireNonNull(world.getEntity(id)).discard();
        }
        BlockPos basePos = getWorldPosition();
        rooms.forEach((point,room) -> room.setBasePos(world,basePos.add(point.x * 32,0,point.y * 32)));
        rooms.forEach(((point, dungeonRoom) -> dungeonRoom.despawn()));
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
            }
        }
    }

    public void tick(){
        for(ServerPlayerEntity player : world.getPlayers()){
            if(this.volume.contains(player.getPos())){
                if(!this.currentPlayers.contains(player.getUuid())){
                    player.sendMessage(Text.of("You entered dungeon: "+this.uuid.toString().substring(0,8)),false);
                }
                this.currentPlayers.add(player.getUuid());
            }else{
                if(this.currentPlayers.contains(player.getUuid())){
                    player.sendMessage(Text.of("You left dungeon: "+this.uuid.toString().substring(0,8)),false);
                }
                this.currentPlayers.remove(player.getUuid());
            }
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
