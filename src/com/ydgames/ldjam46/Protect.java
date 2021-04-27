package com.ydgames.ldjam46;

import processing.core.PConstants;
import processing.core.PImage;

public class Protect extends Entity {
    
    // ### CONSTANTS ###
    private static final float SPEED = 2.5f;
    
    public Protect(float x, float y) {
        super(x, y, 16, 20, 100, 100, 0, true, 0);
        
        entityWalkFrames = new PImage[]{
                Main.PROTECT[0],
                Main.PROTECT[1],
                Main.PROTECT[2]
        };
        
        entityStandFrames = new PImage[]{
                Main.PROTECT[3]
        };
        
        // ### MOVEMENT ###
        weaponImage = null;
    
        // ### ANIMATIONS ###
        frameSpeed = 6;
    }
    
    @Override
    public void update(Main main) {
        
        // ### MOVEMENT ###
        if (main.controlling == this) {
            int spdH = (main.game.input.isKey(PConstants.RIGHT) || main.game.input.isKey('D') ? 1 : 0) -
                    (main.game.input.isKey(PConstants.LEFT) || main.game.input.isKey('A')? 1 : 0);
            int spdV = (main.game.input.isKey(PConstants.DOWN) || main.game.input.isKey('S') ? 1 : 0) -
                    (main.game.input.isKey(PConstants.UP) || main.game.input.isKey('W') ? 1 : 0);
            
            speedX = spdH * SPEED;
            speedY = spdV * SPEED;
            
            if(speedX > 0) facing = 1;
            if(speedX < 0) facing = -1;
        } else {
            speedX = 0;
            speedY = 0;
        }
        
        
        super.update(main);
    }
    
    @Override
    public void render(Main main) {
        super.render(main);
    }
    
    @Override
    public void hit(Entity other) {
        if(other instanceof Player) return;
        super.hit(other);
    }
}
