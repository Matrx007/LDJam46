package com.ydgames.ldjam46;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class Player extends Entity {
    
    // ### CONSTANTS ###
    private static final int SPEED = 2;
    private static final int HAND_DAMAGE = 4;
    private static final PImage[] IMAGES = new PImage[]{
            Main.MAIN.game.loadImage("assets/chosen/player/walk0.png"),
            Main.MAIN.game.loadImage("assets/chosen/player/walk1.png"),
            Main.MAIN.game.loadImage("assets/chosen/player/walk2.png"),
            Main.MAIN.game.loadImage("assets/chosen/player/walk3.png"),
            Main.MAIN.game.loadImage("assets/chosen/player/standing.png")
    };
    
    // ### SOUNDS ###
    private int steppingSoundTimer = 0;
    private boolean steppingSoundLeft = true;
    
    public Player(float x, float y) {
        super(x, y, 16, 20, 100, 100, HAND_DAMAGE, true, 20);
        
        entityWalkFrames = new PImage[]{
                Main.MAIN.game.loadImage("assets/chosen/player/walk0.png"),
                Main.MAIN.game.loadImage("assets/chosen/player/walk1.png"),
                Main.MAIN.game.loadImage("assets/chosen/player/walk2.png"),
                Main.MAIN.game.loadImage("assets/chosen/player/walk3.png")
        };
        
        entityStandFrames = new PImage[]{
                Main.MAIN.game.loadImage("assets/chosen/player/standing.png")
        };
        
        // ### MOVEMENT ###
        weaponImage = Main.WEAPONS[0];
    
        // ### ANIMATIONS ###
    
        frameSpeed = 6;
    }
    
    @Override
    public void update(Main main) {
        
        // ### MOVEMENT ###
        if(main.controlling == this) {
            int spdH = (main.game.input.isKey(PConstants.RIGHT) || main.game.input.isKey('D') ? 1 : 0) -
                    (main.game.input.isKey(PConstants.LEFT) || main.game.input.isKey('A')? 1 : 0);
            int spdV = (main.game.input.isKey(PConstants.DOWN) || main.game.input.isKey('S') ? 1 : 0) -
                    (main.game.input.isKey(PConstants.UP) || main.game.input.isKey('W') ? 1 : 0);
    
            speedX = spdH * SPEED;
            speedY = spdV * SPEED;
        } else {
            speedX = 0;
            speedY = 0;
        }
        
        if(speedX != 0 || speedY != 0) {
            steppingSoundTimer++;
            
            if(steppingSoundTimer >= 15) {
                steppingSoundTimer = 0;
    
                Main.soundManager.playSound(steppingSoundLeft ? "stepLeft" : "stepRight");
            }
        }
    
        // ### WEAPON ###
        
        if(main.controlling == this) {
    
            aimAngle = PApplet.degrees((float)Math.atan2(
                    main.mouseY-y, main.mouseX-x
            ));
            
            if (main.game.input.isButtonDown(PConstants.LEFT)) {
                hit = true;
            }
    
            facing = main.mouseX > x ? 1 : -1;
        }
    
        super.update(main);
    }
    
    @Override
    public void render(Main main) {
        super.render(main);
    }
}
