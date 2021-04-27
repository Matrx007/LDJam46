package com.ydgames.ldjam46;

public class Portal extends GameObject {
    
    // ### ANIMATION ###
    private int frameTimer, frame;
    
    public Portal(float x, float y) {
        super(x, y, 24, 24);
    }
    
    @Override
    public void update(Main main) {
    
        boolean playerHere = main.player.x > x - 12 && main.player.y > y - 12 &&
                main.player.x < x + 24 + 12 && main.player.y < y + 24 + 12;
        boolean protectHere = main.protect.x > x - 12 && main.protect.y > y - 12 &&
                main.protect.x < x + 24 + 12 && main.protect.y < y + 24 + 12;
        
        int blinkSpeed = (playerHere || protectHere) ? 4 : 10;
        
        if(playerHere && protectHere && !main.levelComplete) {
            // Level complete
            main.levelComplete = true;
            main.levelCounter++;
            main.player.weaponImage = Main.WEAPONS[Math.min(11, main.levelCounter)];
            main.player.damage++;
            main.nextLevelCountdown = 60;
        }
        
        frameTimer++;
        if (frameTimer > blinkSpeed) {
            frameTimer = 0;
            frame = (frame + 1) % 2;
        }
    }
    
    @Override
    public void render(Main main) {
        main.screenBuffer.image(Main.PORTAL[frame], x, y);
    }
}
