package com.ydgames.ldjam46;

public abstract class GameObject {

    public int width, height;
    public float x, y;
    
    public boolean dead = false;
    
    public GameObject(float x, float y, int width, int height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
    
    public abstract void update(Main main);
    public abstract void render(Main main);

}
