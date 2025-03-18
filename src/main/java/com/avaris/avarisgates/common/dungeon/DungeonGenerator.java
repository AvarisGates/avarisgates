package com.avaris.avarisgates.common.dungeon;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DungeonGenerator {
    private static final HashMap<BlockPos, BlockState> STARTING_ROOM = new HashMap<>();
    static {
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            STARTING_ROOM.put(new BlockPos(i,0,0), Blocks.BASALT.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            STARTING_ROOM.put(new BlockPos(i,0,DungeonRoom.SIDE_LENGTH),Blocks.BASALT.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            STARTING_ROOM.put(new BlockPos(0,0,i),Blocks.NETHER_BRICKS.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            STARTING_ROOM.put(new BlockPos(DungeonRoom.SIDE_LENGTH,0,i),Blocks.NETHER_BRICKS.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH - 1;i++){
            for (int j = 0;j < DungeonRoom.SIDE_LENGTH - 1;j++) {
                STARTING_ROOM.put(new BlockPos(i + 1, 1, j + 1), Blocks.EMERALD_BLOCK.getDefaultState());
            }
        }
    }
    private static final HashMap<BlockPos, BlockState> MONSTER_ROOM = new HashMap<>();
    static {
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            MONSTER_ROOM.put(new BlockPos(i,0,0), Blocks.BASALT.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            MONSTER_ROOM.put(new BlockPos(i,0,DungeonRoom.SIDE_LENGTH),Blocks.BASALT.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            MONSTER_ROOM.put(new BlockPos(0,0,i),Blocks.NETHER_BRICKS.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            MONSTER_ROOM.put(new BlockPos(DungeonRoom.SIDE_LENGTH,0,i),Blocks.NETHER_BRICKS.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH - 1;i++){
            for (int j = 0;j < DungeonRoom.SIDE_LENGTH - 1;j++) {
                MONSTER_ROOM.put(new BlockPos(i + 1, 1, j + 1), Blocks.REDSTONE_BLOCK.getDefaultState());
            }
        }
    }
    private static final HashMap<BlockPos, BlockState> BOSS_ROOM = new HashMap<>();
    static {
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            BOSS_ROOM.put(new BlockPos(i,0,0), Blocks.BASALT.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            BOSS_ROOM.put(new BlockPos(i,0,DungeonRoom.SIDE_LENGTH),Blocks.BASALT.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            BOSS_ROOM.put(new BlockPos(0,0,i),Blocks.NETHER_BRICKS.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH;i++){
            BOSS_ROOM.put(new BlockPos(DungeonRoom.SIDE_LENGTH,0,i),Blocks.NETHER_BRICKS.getDefaultState());
        }
        for (int i = 0;i < DungeonRoom.SIDE_LENGTH - 1;i++){
            for (int j = 0;j < DungeonRoom.SIDE_LENGTH - 1;j++) {
                BOSS_ROOM.put(new BlockPos(i+1, 1, j+1), Blocks.GOLD_BLOCK.getDefaultState());
            }
        }
    }
    public static HashMap<Point, DungeonRoom> generateRooms(){
        HashMap<Point, DungeonRoom> rooms = new HashMap<>();
        Random rng = new Random();
        int bossRoomX = rng.nextInt(0,7);
        int bossRoomY = rng.nextInt(5,7);

        ArrayList<Point> targets = new ArrayList<>();
        targets.add(new Point(bossRoomX,bossRoomY));
        targets.add(new Point(bossRoomX,bossRoomY-4));
        targets.add(new Point(7,7));
        targets.add(new Point(4,4));
        targets.add(new Point(4,6));
        targets.add(new Point(6,4));

        for(Point target : targets){
            int targetX = target.x;
            int targetY = target.y;
            int currX = 0;
            int currY = 0;
            while (true){
                if(currX < targetX&&currY < targetY){
                    if(rng.nextBoolean()) {
                        currX++;
                    }else{
                        currY++;
                    }
                }
                else if(currX < targetX){
                    currX++;
                }else{
                    currY++;
                }
                if(currX == targetX&&currY == targetY){
                    break;
                }
                rooms.put(new Point(currX,currY),new DungeonRoom(MONSTER_ROOM));
            }
        }

        rooms.put(new Point(0,0),new DungeonRoom(STARTING_ROOM));
        rooms.put(new Point(bossRoomX,bossRoomY),new DungeonRoom(BOSS_ROOM));
        return rooms;
    }
}
