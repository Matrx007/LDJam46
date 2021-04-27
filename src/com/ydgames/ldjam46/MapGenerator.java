package com.ydgames.ldjam46;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.SplittableRandom;

public class MapGenerator {
    
    private SplittableRandom random = new SplittableRandom();
    
    // ### PROPERTIES ###
    
    // World
    public int worldSize = 50;
    
    // Walker
    public int walkerLimitingDistance = 5;
    public float walkerDestroyChance = 0.05f;
    public float walkerSpawnChance = 0.01f;
    public float walkerTurnChance = 0.1f;
    public int maxNumWalkers = 30;
    public int spawnProtectionRadius = 4;
    public int spawnRoomSize = 6;
    
    // Objects
    public float enemyChange = 0.01f;
    
    // Block change - duplicates are allowed to increase
    // the chance of the block getting picked
    
    // 0-23 blocks
    // 24 enemy
    public int[] objects = new int[]{
            
            // -- BLOCKS --
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            
            1, 1, 1, 1, 1, 1, 1, 1,
            2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3, 3, 3,
            4, 4, 4, 4, 4, 4, 4, 4,
            5, 5, 5, 5, 5, 5, 5, 5,
            6, 6, 6, 6, 6, 6,
            7, 7, 7, 7, 7, 7, 7, 7,
            8, 8, 8, 8, 8, 8,
            9,
            10, 10,
            11, 11, 11, 11, 11, 11,
            12, 12, 12, 12,
            13, 13, 13, 13,
            14,
            15,
            16, 16, 16, 16, 16, 16,
            17, 17, 17, 17, 17, 17,
            18, 18,
            19, 19, 19, 19,
            20, 20, 20, 20,
            21, 21, 21, 21, 21, 21,
            22, 22, 22, 22,
            23, 23, 23, 23,
            
            // -- ENEMIES --
            24, 24, 24
    };
    
    // ### GRAPHICS ###
    public PImage[] coloredTiles;
    public PImage[] tileEdgeReflection;
    public PImage tileEdge;
    
    // ### WORLD ###
    public Portal portal;
    
    private void randomize() {
        walkerDestroyChance = (float)random.nextDouble() * 0.2f + 0.01f;
        walkerSpawnChance = (float)random.nextDouble() * 0.2f + 0.01f;
        walkerTurnChance = (float)random.nextDouble() * 0.5f + 0.01f;
    }
    
    public int[][] generate() {
        randomize();
        
        int[][] spawn = new int[spawnRoomSize][spawnRoomSize];
        
        for(int i = 0; i < spawnRoomSize; i++) {
            for(int j = 0; j < spawnRoomSize; j++) {
                if (i > 0 && j > 0 && i < spawnRoomSize-1 && j < spawnRoomSize-1) {
                    spawn[i][j] = objects[random.nextInt(objects.length - 1)] + 1;
                    while (spawn[i][j] == 25) {
                        spawn[i][j] = objects[random.nextInt(objects.length - 1)] + 1;
                    }
                } else {
                    spawn[i][j] = -1;
                }
            }
        }
    
        int[][] world = new int[worldSize][worldSize];
        ArrayList<Walker> walkers = new ArrayList<>();
        
        int midX = worldSize/2;
        int midY = worldSize/2;
    
        walkers.add(new Walker(worldSize/2, worldSize/2, random.nextInt(4)));
    
        int iterations = 0;
        while(iterations < 500) {
            iterations++;
        
            for(Walker walker : walkers) {
                walker.x = Math.max(Math.min(walker.x, worldSize-1), 0);
                walker.y = Math.max(Math.min(walker.y, worldSize-1), 0);
    
                int a = objects[random.nextInt(objects.length)]+1;
                world[walker.x][walker.y] = a;
                
                // If in protection area, don't spawn enemies
                if(Math.abs(midX - walker.x) < spawnProtectionRadius ||
                        Math.abs(midY - walker.y) < spawnProtectionRadius) {
                    
                    /*while (world[walker.x][walker.y] == 25) {
                        world[walker.x][walker.y] = objects[random.nextInt(objects.length-1)]+1;
                    }*/
                }
                
                if(iterations < spawnProtectionRadius) {
                    int x = walker.x - midX + spawnRoomSize/2;
                    int y = walker.y - midY + spawnRoomSize/2;
    
                    if(x >= 0 && y >= 0 && x < spawnRoomSize && y < spawnRoomSize) {
//                        spawn[x][y] = world[walker.x][walker.y];
                    }
                }
            }
            
            if(iterations > spawnProtectionRadius) {
                for(int i = 0; i < walkers.size(); i++) {
                    if(random.nextDouble() < walkerDestroyChance && walkers.size() > 1) {
                        walkers.remove(i);
                        break;
                    }
                }
            
                for(int i = 0; i < walkers.size(); i++) {
                    Walker walker = walkers.get(i);
                    
                    // -- Turn away if border gets closer --
                    
                    if(worldSize - walker.x < walkerLimitingDistance) {
                        if(random.nextDouble() < 0.25f) {
                            walker.direction = 2;
                        }
                    }
                    
                    if(worldSize - walker.y < walkerLimitingDistance) {
                        if(random.nextDouble() < 0.25f) {
                            walker.direction = 3;
                        }
                    }
                    
                    if(walker.x < walkerLimitingDistance) {
                        if(random.nextDouble() < 0.25f) {
                            walker.direction = 0;
                        }
                    }
                    
                    if(walker.y < walkerLimitingDistance) {
                        if(random.nextDouble() < 0.25f) {
                            walker.direction = 1;
                        }
                    }
                }
            
                for (Walker walker : walkers) {
                    if (random.nextDouble() < walkerTurnChance) {
                        walker.direction = random.nextInt(4);
                    }
                }
            
                for(int i = 0; i < walkers.size(); i++) {
                    Walker walker = walkers.get(i);
                
                    if(random.nextDouble() < walkerSpawnChance &&
                            walkers.size() < maxNumWalkers) {
                        walkers.add(new Walker(walker.x, walker.y, random.nextInt(4)));
                    }
                }
            }
        
            for (Walker thisWalker : walkers) {
                switch (thisWalker.direction) {
                    case 0:
                        thisWalker.x += 1;
                        break;
                    case 1:
                        thisWalker.y += 1;
                        break;
                    case 2:
                        thisWalker.x -= 1;
                        break;
                    default:
                        thisWalker.y -= 1;
                        break;
                }
            }
        }
        
        // ### PORTAL ###
        Walker walker = walkers.get(0);
        portal = new Portal(walker.x*16+8-12, walker.y*16+8-12);
        
        // ### CREATE SPAWN ###
        for(int i = 0; i < spawnRoomSize; i++) {
            for(int j = 0; j < spawnRoomSize; j++) {
                world[midX - i + spawnRoomSize/2][midY - j + spawnRoomSize/2] = spawn[i][j];
            }
        }
        
        return world;
    }
    
    public void generateTiles() {
        Color color = new Color(Color.HSBtoRGB(
                (float)random.nextDouble(),
                0.47f, 0.6f));
        
        // ### TILES ###
        
        coloredTiles = new PImage[Main.TILES.length];
    
        PGraphics temp = Main.MAIN.game.createGraphics(16, 16, PConstants.P2D);
        
        temp.beginDraw();
        temp.tint(color.getRed(), color.getGreen(), color.getBlue());
        
        for(int i = 0; i < coloredTiles.length; i++) {
            if(Main.TILES[i] == null) continue;
            
            temp.clear();
            
            temp.image(Main.TILES[i], 0, 0);
            
            coloredTiles[i] = temp.copy();
        }
        
        // ### TILE EDGE ###
        
        temp.clear();
        
        temp.image(Main.TILE_EDGE, 0, 0);
        
        tileEdge = temp.copy();
        
        // ### TILE EDGE REFLECTION ###
        /*tileEdgeReflection = new PImage[Main.TILE_EDGE_REFLECTION.length];
    
        temp.endDraw();
        
        temp = Main.MAIN.game.createGraphics(16, 5);
        
        temp.beginDraw();
    
        temp.tint(color.getRed(), color.getGreen(), color.getBlue());
    
        for(int i = 0; i < tileEdgeReflection.length; i++) {
            temp.clear();
        
            temp.image(Main.TILE_EDGE_REFLECTION[i], 0, 0);
    
            tileEdgeReflection[i] = temp.copy();
        }*/
    
        temp.endDraw();
    }
    
    private static class Walker {
        private int x, y;
        private int direction;
        
        private Walker(int x, int y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
    }
}
 