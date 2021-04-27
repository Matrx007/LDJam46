package com.ydgames.ldjam46;

import processing.core.PGraphics;

public class Tile extends GameObject {
    private int tile;
    
    private int frame, animationSpeed, animationTimer;
    private int reflectionFrameTimer, reflectionFrame;
    private boolean hasEdge, playerOnTop;
    
    private PGraphics image;
    private boolean hasBlood;
    
    public Tile(float x, float y, int tile, boolean hasEdge) {
        super(x, y, 16, hasEdge ? 26 : 16);
        this.tile = tile;
        
        if(tile == -2) {
            this.tile = 25;
        }
        
        this.hasEdge = hasEdge;
    }
    
    @Override
    public void update(Main main) {
        
        // Is player on top
        playerOnTop = Math.abs(main.player.x - x - 8) < 8 &&
                Math.abs(main.player.y  + main.player.height/2f - y - 8) < 8;
    
        // If player is on top
        if(playerOnTop) {
            switch (tile) {
                case 18:
                    if(frame == 0 && animationSpeed == 0) {
                        // Take damage and play animation
                        main.player.health -= 1;
                        animationSpeed = 1;
                    }
            }
        }
        
        if(tile == 18) {
            if(animationSpeed != 0) {
                animationTimer++;
                if(animationTimer > 5) {
                    animationTimer = 0;
                    
                    if (!playerOnTop || frame != 3) {
                        frame += animationSpeed;
                        
                        if(frame >= 3) {
                            animationSpeed *= -1;
                            frame = 3;
                        } else if(frame <= 0) {
                            animationSpeed = 0;
                            frame = 0;
                        }
                    }
                }
            }
        }
        
        if(hasEdge) {
            reflectionFrameTimer++;
            
            if(reflectionFrameTimer >= 10) {
                reflectionFrameTimer = 0;
    
                reflectionFrame = (reflectionFrame+1) % Main.TILE_EDGE_REFLECTION.length;
            }
        }
    }
    
    @Override
    public void render(Main main) {
        if(tile < Main.MAIN.mapGenerator.coloredTiles.length) {
            if (Main.MAIN.mapGenerator.coloredTiles[tile] == null) {
                System.out.println(tile + " is null");
                return;
            }
        } else {
            System.out.println(tile+" is out of bounds");
            return;
        }
    
        if(hasBlood)
            main.screenBuffer.image(image, x, y);
        else
            main.screenBuffer.image(Main.MAIN.mapGenerator.coloredTiles[tile], x, y);
        
        if(tile == 18 && frame != 0)
            main.screenBuffer.image(Main.SPIKES[frame], x, y);
        
        if(hasEdge) {
            main.screenBuffer.image(Main.MAIN.mapGenerator.tileEdge, x, y+16);
            main.screenBuffer.image(Main.TILE_EDGE_REFLECTION[reflectionFrame], x, y+16+5);
        }
    }
    
    public void drawBlood(int[][] bloodStains) {
        if(hasBlood) {
            image.beginDraw();
            
            // Blood
            for(int[] bloodStain : bloodStains) {
                image.image(Main.BLOOD_STAINS[bloodStain[0]], bloodStain[1]-x, bloodStain[2]-y);
            }
            image.endDraw();
        } else {
            hasBlood = true;
    
            image = Main.MAIN.game.createGraphics(16, 16);
            image.beginDraw();
            
            // Tile
            image.image(Main.MAIN.mapGenerator.coloredTiles[tile], 0, 0);
    
            // Blood
            for (int[] bloodStain : bloodStains) {
                image.image(Main.BLOOD_STAINS[bloodStain[0]], bloodStain[1]-x, bloodStain[2]-y);
            }
    
            image.endDraw();
        }
    }
}
