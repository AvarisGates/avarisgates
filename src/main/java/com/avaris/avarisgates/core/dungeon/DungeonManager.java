package com.avaris.avarisgates.core.dungeon;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DungeonManager {
    private static final HashMap<UUID,Dungeon> currentDungeons = new HashMap<>();
    private static long dungeonsCreated = 0;
    private static final Grid dungeonGrid = new Grid();

    public static void addDungeon(Dungeon dungeon){
        dungeon.setGridPos(dungeonGrid.getNextLocation());
        dungeon.spawn();
        currentDungeons.put(dungeon.getUuid(),dungeon);
        dungeonsCreated++;
    }

    public static boolean removeDungeon(UUID uuid){
        Dungeon dungeon = currentDungeons.get(uuid);
        if(dungeon == null) {
            return false;
        }
        dungeon.onRemove();
        currentDungeons.remove(uuid);
        dungeonGrid.addFreeSpace(dungeon.getGridPos());
        return true;
    }

    public static long getDungeonsCreated() {
        return dungeonsCreated;
    }

    public static long getLiveDungeonCount() {
        return currentDungeons.size();
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
