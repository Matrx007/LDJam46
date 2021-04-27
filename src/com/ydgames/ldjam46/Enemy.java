package com.ydgames.ldjam46;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Enemy extends Entity {
    
    // ### MOVING ###
    private float movingDirection;
    private float movingSpeed = 1.5f;
    
    // ### AI ###
    private boolean sleeping = true;
    
    private int wanderingTurnTimer;
    private float wanderingDirection;
    
    // ### SOUNDS ###
    private int timeFromLastSound;
    
    public Enemy(float x, float y, int id) {
        super(x, y, Main.ENEMY_DATA[id][0], Main.ENEMY_DATA[id][0],
                10, 10, 40, false, 18);
        
        maxHealth = Main.ENEMY_DATA[id][5];
        health = maxHealth;
        
        movingSpeed = Main.ENEMY_DATA[id][1]/10f;
        damage = Main.ENEMY_DATA[id][2];
        weaponImage = Main.WEAPONS[Main.ENEMY_DATA[id][3]];
        
        entityWalkFrames = Main.ENEMIES[id];
        entityStandFrames = new PImage[]{Main.ENEMIES[id][0]};
        
        frameSpeed = 6;
    }
    
    @Override
    public void update(Main main) {
        timeFromLastSound++;
        
        float disPlayer = PVector.dist(new PVector(x, y),
                new PVector(main.player.x, main.player.y));
        float disProtect = PVector.dist(new PVector(x, y),
                new PVector(main.protect.x, main.protect.y));
        
        // ### MOVEMENT ###
        if(!sleeping) {
            float direction;
            float dis;
            if(disPlayer < disProtect) {
                direction = (float) Math.atan2(
                        main.player.y - y,
                        main.player.x - x
                );
                dis = PVector.dist(
                        new PVector(main.player.x, main.player.y),
                        new PVector(x, y)
                );
            } else {
                direction = (float) Math.atan2(
                        main.protect.y - y,
                        main.protect.x - x
                );
                dis = PVector.dist(
                        new PVector(main.protect.x, main.protect.y),
                        new PVector(x, y)
                );
            }
            
            aimAngle = (float) Math.toDegrees(direction);
            
            if(dis > 16) {
                movingDirection += Math.sin(direction - movingDirection) * 5;
        
                speedX = (float) Math.cos(direction) * movingSpeed;
                speedY = (float) Math.sin(direction) * movingSpeed;
            }
        } else {
    
            movingDirection += Math.sin(wanderingDirection - movingDirection) * 5;
    
            speedX = (float) Math.cos(wanderingDirection) * movingSpeed * 0.5f;
            speedY = (float) Math.sin(wanderingDirection) * movingSpeed * 0.5f;
            
            wanderingTurnTimer--;
            if(wanderingTurnTimer <= 0) {
                wanderingTurnTimer = 120 + Main.random.nextInt(500);
                
                wanderingDirection = Main.random.nextInt(360);
            }
            
            if(disPlayer < 160 || disProtect < 160) {
                sleeping = false;
                
                if(timeFromLastSound > 480) {
                    timeFromLastSound = 0;
                    Main.soundManager.playSound("enemy"+Main.random.nextInt(4));
                }
            }
        }
    
    
        // ### ATTACKING ###
        
        if ((Main.MAIN.player.x > x - 32 &&
                Main.MAIN.player.x < x + 32 &&
                Main.MAIN.player.y > y - 32 &&
                Main.MAIN.player.y < y + 32) ||
                        Main.MAIN.protect.x > x - 32 &&
                        Main.MAIN.protect.x < x + 32 &&
                        Main.MAIN.protect.y > y - 32 &&
                        Main.MAIN.protect.y < y + 32) {
            hit = true;
        }
        
        super.update(main);
    }
    
    @Override
    public void render(Main main) {
        super.render(main);
    }
}
