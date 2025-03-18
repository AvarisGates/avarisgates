package com.avaris.avarisgates.common.dungeon;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class DungeonManager{
    private HashMap<UUID,Dungeon> currentDungeons = new HashMap<>();
    private long dungeonsCreated = 0;
    private Grid dungeonGrid = new Grid();

    public BlockPos addDungeon(Dungeon dungeon){
        dungeon.setGridPos(dungeonGrid.getNextLocation());
        dungeon.spawn();
        currentDungeons.put(dungeon.getUuid(),dungeon);
        dungeonsCreated++;
        return dungeon.getWorldPosition();
    }

    public boolean removeDungeon(UUID uuid){
        return removeDungeon(uuid,false);
    }

    public boolean removeDungeon(UUID uuid,boolean soft){
        Dungeon dungeon = currentDungeons.get(uuid);
        if(dungeon == null) {
            return false;
        }
        dungeon.onRemove();
        if(!soft){
            currentDungeons.remove(uuid);
        }
        dungeonGrid.addFreeSpace(dungeon.getGridPos());
        return true;
    }

    public void tick(MinecraftServer minecraftServer){
       currentDungeons.forEach((pos,dungeon) -> dungeon.tick());
    }

    public long getDungeonsCreated() {
        return dungeonsCreated;
    }

    public long getLiveDungeonCount() {
        return currentDungeons.size();
    }

    public void removeAllDungeons() {
        if(currentDungeons != null&&!currentDungeons.isEmpty()){
            currentDungeons.forEach((uuid, dungeon) -> {
                this.removeDungeon(uuid,true);
            });
        }
        this.currentDungeons = new HashMap<>();
        this.dungeonGrid = new Grid();
        this.dungeonsCreated = 0;
    }

    public Collection<UUID> dungeonUuids() {
        return this.currentDungeons.keySet();
    }

    public static class Grid{
        private static final int startX = 0;
        private static final int startY = 0;

        private int currentX = startX;
        private int currentY = startY;
        private int idx = 0;
        private int sideLength = 10;
        private final ArrayList<Point> freeSpace = new ArrayList<>();

        public Point getCurrentLocation(){
            return new Point(currentX, currentY);
        }

        public Point getNextLocation(){
            try{
                Point p = freeSpace.getFirst();
                if(p != null){
                    freeSpace.removeFirst();
                    return p;
                }
            }catch (Exception e){
                if(idx == 0){
                    idx++;
                    return getCurrentLocation();
                }
                currentX++;
                if(currentX == sideLength){
                    currentX = 0;
                    currentY++;
                }
                idx++;
            }
            return getCurrentLocation();
        }

        public void addFreeSpace(Point point){
            freeSpace.add(point);
        }

    }
}
