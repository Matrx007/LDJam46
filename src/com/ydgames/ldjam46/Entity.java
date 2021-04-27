package com.ydgames.ldjam46;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import java.util.Arrays;

import static com.ydgames.ldjam46.Main.MAIN;
import static com.ydgames.ldjam46.Main.soundManager;

public class Entity extends GameObject {
    
    // ### VALUES ###
    private boolean isProtected;
    
    // Health
    public int health;
    public int maxHealth;
    
    // Damage
    public int damage;
    
    // ### MOVEMENT ###
    public float speedX = 0, speedY = 0;
    public int facing = 1;
    
    // -- Knockback --
    public float knockbackX, knockbackY, knockbackInitialForce = 10;
    
    // ### ANIMATIONS ###
    public int frameTimer, frameCounter, frameSpeed = 1;
    public float hurtEffect;
    
    // ### GRAPHICS ###
    public PImage[] entityWalkFrames;
    public PImage[] entityStandFrames;
    public PImage entityImage, weaponImage;
    
    // ### WEAPON ###
    public float aimAngle, currentAngle;
    
    // Attacking
    public boolean hit;
    public int phase, phase1Budget = 64, phase2Budget = 18,
            phase1BudgetCurrent, phase2BudgetCurrent, attackCoolDown;
    private float hitRange;
    
    // ### DEATH ###
    private int deathAnimationFrame, deathAnimationTimer;
    
    public Entity(float x, float y, int width, int height, int health, int maxHealth, int damage, boolean isProtected, float hitRange) {
        super(x, y, width, height);
        this.health = health;
        this.maxHealth = maxHealth;
        this.damage = damage;
        this.isProtected = isProtected;
        this.hitRange = hitRange;
    }
    
    @Override
    public void update(Main main) {
        
        // ### DEATH ###
        if (health <= 0) {
            deathAnimationTimer++;
            if(deathAnimationTimer > 2) {
                deathAnimationFrame++;
                deathAnimationTimer = 0;
                
                if(deathAnimationFrame == 1) {
                    soundManager.playSound("death");
                }
                
                if(deathAnimationFrame == 4) {
                    drawBlood();
                }
                
                if(deathAnimationFrame == 8) {
                    dead = true;
                }
            }
            
            return;
        }
        
        
        if(hurtEffect >= 0.1f) {
            hurtEffect *= 0.9f;
        } else {
            hurtEffect = 0;
        }
        
        // ### VALUES ###
        
        // ### MOVEMENT ###
        
        // -- Collision --
        
        // Horizontal Collision
        int spdH = (int)Math.signum(speedX);
        int spdV = (int)Math.signum(speedY);
        
        if(spdH != 0) {
            if(!isProtected) {
                if (!main.checkForTile(x + width/2f*spdH + speedX, y + height/2f) ||
                        main.tileAt(x + width/2f*spdH + speedX, y + height/2f) == -1) {
                    while (main.checkForTile(x + width/2f*spdH + spdH, y + height/2f) &&
                            main.tileAt(x + width/2f*spdH + spdH, y + height/2f) != -1) {
                        x += spdH;
                    }
                } else {
                    x += speedX;
                }
            } else {
                if (!main.checkForTile(x + width / 2f * spdH + speedX, y + height / 2f)) {
                    while (main.checkForTile(x + width / 2f * spdH + spdH, y + height / 2f)) {
                        x += spdH;
                    }
                } else {
                    x += speedX;
                }
            }
        }
    
        // Vertical Collision
        if(spdV != 0) {
            if(!isProtected) {
                if (!main.checkForTile(x, y + height/2f + height/4f*spdV + speedY) ||
                        main.tileAt(x, y + height/2f + height/4f*spdV + speedY) == -1) {
                    while (main.checkForTile(x, y + height/2f + height/4f*spdV + spdV) &&
                            main.tileAt(x, y + height/2f + height/4f*spdV + spdV) != -1) {
                        y += spdV;
                    }
                } else {
                    y += speedY;
                }
            } else {
                if (!main.checkForTile(x, y + height/2f + height/4f*spdV + speedY)) {
                    while (main.checkForTile(x, y + height/2f + height/4f*spdV + spdV)) {
                        y += spdV;
                    }
                } else {
                    y += speedY;
                }
            }
        }
        
        // Knockback
        x += knockbackX;
        y += knockbackY;
        
        knockbackX *= 0.85f;
        knockbackY *= 0.85f;
        
        // Death
        if(!main.checkForTile(x, y+height/2f)) {
            health = 0;
        }
        
        // ### ANIMATION ###
    
        frameTimer++;
        while(frameTimer > frameSpeed) {
            frameTimer -= frameSpeed;
            frameCounter++;
        }
    
        if(speedX != 0 || speedY != 0) {
            int animationDirection = facing == 1 ? (speedX > 0 ? 1 : speedX < 0 ? -1: 1) :
                    (speedX > 0 ? -1 : speedX < 0 ? 1 : 1);
        
            frameCounter = frameCounter % entityWalkFrames.length;
            entityImage = entityWalkFrames[animationDirection == 1 ? frameCounter : entityWalkFrames.length-1-frameCounter];
        } else {
            frameCounter = frameCounter % entityStandFrames.length;
            entityImage = entityStandFrames[frameCounter];
        }
        
        // ### WEAPON ANIMATION ###
        if(attackCoolDown == 0) {
            if (hit && phase == 0) {
                // Phase 0: Initialize variables
                hit = false;
                currentAngle = aimAngle;
                phase1BudgetCurrent = phase1Budget;
                phase2BudgetCurrent = phase2Budget;
                attackCoolDown = 10;
                phase = 1;
    
                Main.soundManager.playSound("swing");
            }
        } else {
            attackCoolDown--;
        }
    
        if(phase != 0) {
            // Phase 1: Ease the angle to swingFrom
            if (phase == 1) {
                phase1BudgetCurrent /= 2;
                currentAngle += phase1BudgetCurrent;
                if(phase1BudgetCurrent == 1) phase++;
            }
    
            // Phase 2: Ease the angle to swingTo
            if (phase == 2) {
                phase2BudgetCurrent /= 2;
                currentAngle -= phase2BudgetCurrent * phase2BudgetCurrent;
                if(phase2BudgetCurrent == 1) phase = 0;
    
                // At the beginning of the swing, do damage
                // ### DOING DAMAGE ###
                if(phase2BudgetCurrent == phase2Budget/2) {
                    for (Entity entity : main.entities) {
                        if(entity == this) continue;
        
                        // Hit box location
                        float areaX = x + (float) Math.cos(Math.toRadians(aimAngle)) * hitRange;
                        float areaY = y + (float) Math.sin(Math.toRadians(aimAngle)) * hitRange;
        
                        // Look up all entities in the hit box
                        if (PVector.dist(new PVector(areaX, areaY),
                                new PVector(entity.x, entity.y)) < hitRange+4) {
                            entity.hit(this);
                        }
                    }
                }
            }
        } else {
            currentAngle = aimAngle;
        }
    }
    
    @Override
    public void render(Main main) {
        
        if(health <= 0) {
            // ### DEATH ###
            if(deathAnimationFrame <= 7) {
                main.screenBuffer.image(Main.DEATH_EXPLOSION[deathAnimationFrame],
                        x - 32, y - 32);
            }
        } else {
    
            // ### ENTITY ###
    
            if (entityImage != null) {
                main.screenBuffer.pushMatrix();
        
                main.screenBuffer.translate(x, y);
                main.screenBuffer.scale(facing, 1);
        
                main.screenBuffer.image(entityImage, -width / 2f, -height / 2f);
        
                main.screenBuffer.popMatrix();
            }
    
    
            // ### WEAPON ###
            if (weaponImage != null) {
                main.screenBuffer.pushMatrix();
        
                main.screenBuffer.translate(x, y);
                main.screenBuffer.rotate(PApplet.radians(currentAngle + 90));
                main.screenBuffer.scale(facing, 1);
        
                main.screenBuffer.image(weaponImage, -6, -11 - 26);
        
                // ### SWINGING ###
                if (phase != 0) {
                    main.screenBuffer.image(Main.SWING[1], -32, -32 - 8);
                }
        
                main.screenBuffer.popMatrix();
            }
    
    
            // ### HAND ###
            if (weaponImage != null) {
                main.screenBuffer.pushMatrix();
        
                main.screenBuffer.translate(x, y);
                main.screenBuffer.rotate(PApplet.radians(currentAngle + 90));
        
                main.screenBuffer.image(Main.HAND, -2.5f, -11 - 2);
        
                main.screenBuffer.popMatrix();
            }
    
            // ### HEALTH ###
            if(health != maxHealth) {
                main.screenBuffer.stroke(16, 16, 16);
                main.screenBuffer.fill(64, 64, 64);
                main.screenBuffer.rect(x - 12, y - height / 2f - 4, 24, 4);
                main.screenBuffer.fill(128, 64, 64);
                main.screenBuffer.rect(x - 12, y - height / 2f - 4,
                        Math.round(health / (float) maxHealth * 24f), 4);
            }
        }
    }
    
    private void drawBlood() {
        int numBloodStains = Main.random.nextInt(9)+9;
        
        int[][] bloodStains = new int[numBloodStains][3];
        int minX = Integer.MAX_VALUE,
                minY = Integer.MAX_VALUE,
                maxX = Integer.MIN_VALUE,
                maxY = Integer.MIN_VALUE,
                bsX, bsY;
        
        // -- Create blood stains --
        for(int i = 0; i < numBloodStains; i++) {
            
            bsX = (int)(x + Main.random.nextInt(48)-24 - 12);
            bsY = (int)(y + Main.random.nextInt(48)-24 - 12);
            
            minX = Math.min(minX, bsX);
            minY = Math.min(minY, bsY);
            maxX = Math.max(maxX, bsX+48);
            maxY = Math.max(maxY, bsY+48);
            
            bloodStains[i] = new int[]{
                    Main.random.nextInt(Main.BLOOD_STAINS.length),
                    bsX, bsY
            };
        }
    
        // -- Draw blood stains --
        for(int i = minX/16; i < Math.ceil(maxX/16f); i++) {
            for(int j = minY/16; j < Math.ceil(maxY/16f); j++) {
                if(i > 0 && j > 0 &&
                        i < MAIN.map.length &&
                        j < MAIN.map[0].length) {
                    if(MAIN.mapObjects[i][j] != null) {
                        MAIN.mapObjects[i][j].drawBlood(bloodStains);
                    }
                }
            }
        }
    }
    
    public void hit(int damage, float x, float y) {
        // subtract health
        health -= damage;
        
        // knockback
        double direction = Math.atan2(this.y-y, this.x-x);
        double dirX = Math.cos(direction);
        double dirY = Math.sin(direction);
        knockbackX = (float) dirX * knockbackInitialForce;
        knockbackY = (float) dirY * knockbackInitialForce;
        hurtEffect = 255f;
        
        // sound
        Main.soundManager.playSound("hit");
    }
    
    public void hit(Entity other) {
        this.hit(other.damage, other.x, other.y);
    }
}
