package com.ydgames.ldjam46;

import com.ydgames.mxe.Game;
import com.ydgames.mxe.GameContainer;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PShader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.SplittableRandom;

import static processing.core.PConstants.DISABLE_TEXTURE_MIPMAPS;

public class Main extends GameContainer {
    
    // ### CONSTANTS ###
    public static final Main MAIN = new Main();
    public static final String RENDERER = PConstants.P2D;

    // ### ENGINE ###
    public Game game;
    public static final SplittableRandom random = new SplittableRandom();
    public float mouseX, mouseY, absMouseX, absMouseY;
    
    public PGraphics screenBuffer;
    public PGraphics shaderBuffer;
    
    // ### WORLD ###
    public int[][] map;
    public Tile[][] mapObjects;
    public MapGenerator mapGenerator;
    public boolean newWorldPending;
    public ArrayList<GameObject> world, addWorld, removeWorld;
    public ArrayList<Entity> entities, addEntities, removeEntities;
    
    public Entity controlling;
    public Player player;
    public Protect protect;
    
    // ### ENEMY DATA ###
    
    // Frame width, speed, damage, weapon id, height, health
    public static final int[][] ENEMY_DATA = new int[][]{
        new int[]{16, 10, 5,  1, 16, 10},
        new int[]{16, 10, 10, 2, 16, 10},
        new int[]{16, 14, 5,  3, 17, 20},
        new int[]{16, 18, 3,  5, 16, 15},
        new int[]{16, 24, 15, 6, 21, 20},
        new int[]{32, 20, 30, 8, 34, 15},
        new int[]{32, 12, 50, 9, 34, 30}
    };
    
    // ### GRAPHICS ###
    public static final PImage[][] ENEMIES;
    public static final PImage[] PROTECT;
    public static final PImage[] WEAPONS;
    public static final PImage[] SWING;
    public static final PImage HAND;
    
    public static final PImage[] TILES;
    public static final PImage[] TILE_EDGE_REFLECTION;
    public static final PImage TILE_EDGE;
    public static final PImage[] SPIKES;
    public static final PImage[] PORTAL;
    
    public static final PImage[] DEATH_EXPLOSION;
    public static final PImage[] BLOOD_STAINS;
    
    public static final PImage LEVEL_CLEARED;
    public static final PImage LEVEL_FAILED;
    public static final PImage PLAY_BUTTON_IDLE;
    public static final PImage PLAY_BUTTON_HOVER;
    public static final PImage START;
    
    // ### SHADERS ###
    public PShader WHITEN;
    public PShader WATER_SHADER;
    
    // ### GUI ###
    public int levelCounter = 1;
    private boolean gameStarted = false;
    
    // -- Level End --
    public boolean levelComplete;
    private boolean levelFailed;
    public int nextLevelCountdown;
    
    // -- Fonts --
    public PFont retroFont;
    
    // ### SOUND ###
    
    public static final SoundManager soundManager = new SoundManager();
    
    static {
        // ### WEAPONS ###
        WEAPONS = new PImage[10];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/weapons.png"));
    
            if(sheet != null) {
                for(int i = 0; i < 10; i++) {
                    WEAPONS[i] = new PImage(sheet.getSubimage(i*12, 0, 12, 26));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // ### HAND ###
        PImage hand;
        try {
            hand = new PImage(ImageIO.read(new File("assets/chosen/hand.png")));
        } catch (IOException e) {
            e.printStackTrace();
            hand = null;
        }
        HAND = hand;
        
        // ### SWING ###
        SWING = new PImage[4];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/swing.png"));
    
            if(sheet != null) {
                for(int i = 0; i < 4; i++) {
                    SWING[i] = new PImage(sheet.getSubimage(i*32, 0, 32, 32));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // ### ENEMIES ###
        ENEMIES = new PImage[7][4];
        
        // -- ENEMY 0 --
        ENEMIES[0] = new PImage[4];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/enemy0.png"));
    
            if(sheet != null) {
                for(int i = 0; i < 4; i++) {
                    ENEMIES[0][i] = new PImage(sheet.getSubimage(
                            i*ENEMY_DATA[0][0], 0, ENEMY_DATA[0][0], sheet.getHeight()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // -- ENEMY 1 --
        ENEMIES[1] = new PImage[4];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/enemy1.png"));
    
            if(sheet != null) {
                for(int i = 0; i < 4; i++) {
                    ENEMIES[1][i] = new PImage(sheet.getSubimage(
                            i*ENEMY_DATA[1][0], 0, ENEMY_DATA[1][0], sheet.getHeight()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // -- ENEMY 2 --
        ENEMIES[2] = new PImage[4];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/enemy2.png"));
    
            if(sheet != null) {
                for(int i = 0; i < 4; i++) {
                    ENEMIES[2][i] = new PImage(sheet.getSubimage(
                            i*ENEMY_DATA[2][0], 0, ENEMY_DATA[2][0], sheet.getHeight()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // -- ENEMY 3 --
        ENEMIES[3] = new PImage[4];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/enemy3.png"));
    
            if(sheet != null) {
                for(int i = 0; i < 4; i++) {
                    ENEMIES[3][i] = new PImage(sheet.getSubimage(
                            i*ENEMY_DATA[3][0], 0, ENEMY_DATA[3][0], sheet.getHeight()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // -- ENEMY 4 --
        ENEMIES[4] = new PImage[4];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/enemy4.png"));
    
            if(sheet != null) {
                for(int i = 0; i < 4; i++) {
                    ENEMIES[4][i] = new PImage(sheet.getSubimage(
                            i*ENEMY_DATA[4][0], 0, ENEMY_DATA[4][0], sheet.getHeight()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // -- ENEMY 5 --
        ENEMIES[5] = new PImage[4];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/enemy5.png"));
    
            if(sheet != null) {
                for(int i = 0; i < 4; i++) {
                    ENEMIES[5][i] = new PImage(sheet.getSubimage(
                            i*ENEMY_DATA[5][0], 0, ENEMY_DATA[5][0], sheet.getHeight()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // -- ENEMY 6 --
        ENEMIES[6] = new PImage[4];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/enemy6.png"));
    
            if(sheet != null) {
                for(int i = 0; i < 4; i++) {
                    ENEMIES[6][i] = new PImage(sheet.getSubimage(
                            i*ENEMY_DATA[6][0], 0, ENEMY_DATA[6][0], sheet.getHeight()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // #### TILES ###
        TILES = new PImage[6*4+2];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/tiles.png"));
        
            if(sheet != null) {
                for(int i = 0; i < 6; i++) {
                    for(int j = 0; j < 4; j++) {
                        TILES[j*6+i] = new PImage(sheet.getSubimage(
                                i*16, j*16, 16, 16));
                    }
                }
            }
            
            TILES[25] = new PImage(ImageIO.read(new File("assets/chosen/spawnTile.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // #### TILE EDGE REFLECTION ###
        TILE_EDGE_REFLECTION = new PImage[4];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/tileEdgeReflection.png"));
        
            if(sheet != null) {
                for(int i = 0; i < 4; i++) {
                    TILE_EDGE_REFLECTION[i] = new PImage(sheet.getSubimage(
                            i*16, 0, 16, 5));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // ### SPIKES ###
        SPIKES = new PImage[4];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/spikes.png"));
        
            if(sheet != null) {
                for(int i = 0; i < 4; i++) {
                    SPIKES[i] = new PImage(sheet.getSubimage(
                            i*16, 0, 16, 16));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // ### TILE EDGE ###
        PImage tileEdge;
        try {
            tileEdge = new PImage(ImageIO.read(new File("assets/chosen/tileEdge.png")));
        } catch (IOException e) {
            e.printStackTrace();
            tileEdge = null;
        }
        TILE_EDGE = tileEdge;
    
        // ### PROTECT ###
        PROTECT = new PImage[4];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/protect.png"));
        
            if(sheet != null) {
                for(int i = 0; i < 4; i++) {
                    PROTECT[i] = new PImage(sheet.getSubimage(
                            i*16, 0, 16, 19));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // ### DEATH_EXPLOSION ###
        DEATH_EXPLOSION = new PImage[8];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/deathExplosion.png"));
        
            if(sheet != null) {
                for(int i = 0; i < 8; i++) {
                    DEATH_EXPLOSION[i] = new PImage(sheet.getSubimage(
                            i*64, 0, 64, 64));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // ### LEVEL CLEARED ###
        PImage levelCleared;
        try {
            levelCleared = new PImage(ImageIO.read(new File("assets/chosen/levelCleared.png")));
        } catch (IOException e) {
            e.printStackTrace();
            levelCleared = null;
        }
        LEVEL_CLEARED = levelCleared;
    
        // ### LEVEL COMPLETED ###
        PImage levelFailed;
        try {
            levelFailed = new PImage(ImageIO.read(new File("assets/chosen/levelFailed.png")));
        } catch (IOException e) {
            e.printStackTrace();
            levelFailed = null;
        }
        LEVEL_FAILED = levelFailed;
    
        // ### BLOOD STAINS ###
        BLOOD_STAINS = new PImage[10];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/bloodStains.png"));
        
            if(sheet != null) {
                for(int i = 0; i < 10; i++) {
                    BLOOD_STAINS[i] = new PImage(sheet.getSubimage(
                            i*24, 0, 24, 16));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // ### PORTAL ###
        PORTAL = new PImage[2];
    
        try {
            BufferedImage sheet = ImageIO.read(new File("assets/chosen/portal.png"));
        
            if(sheet != null) {
                for(int i = 0; i < 2; i++) {
                    PORTAL[i] = new PImage(sheet.getSubimage(
                            i*24, 0, 24, 24));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // ### PLAY BUTTON ###
        PImage playButtonIdle = null, playButtonHover = null;
        try {
            playButtonIdle  = new PImage(ImageIO.read(new File("assets/chosen/playIdle.png")));
            playButtonHover = new PImage(ImageIO.read(new File("assets/chosen/playHover.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // ### START ###
        PImage start = null;
        try {
            start  = new PImage(ImageIO.read(new File("assets/chosen/start.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        START = start;
        
        
        PLAY_BUTTON_IDLE = playButtonIdle;
        PLAY_BUTTON_HOVER = playButtonHover;
    }
    
    public static void main(String[] args) {
        Game.createGame(1080, 720, MAIN, 60f, RENDERER);
    }
    
    private Main() {}
    
    @Override
    public void setup() {
    
        // ### ENGINE ###
        game = getGame();
        screenBuffer = game.createGraphics(360, 240, RENDERER);
        shaderBuffer = game.createGraphics(360, 240, RENDERER);
    
        game.hint(DISABLE_TEXTURE_MIPMAPS);
        ((PGraphicsOpenGL)game.g).textureSampling(3);
        ((PGraphicsOpenGL)screenBuffer).textureSampling(3);
        ((PGraphicsOpenGL)shaderBuffer).textureSampling(3);
        
        // ### WORLD ###
        world = new ArrayList<>();
        addWorld = new ArrayList<>();
        removeWorld = new ArrayList<>();
        
        
        // ### ENTITIES ###
        entities = new ArrayList<>();
        addEntities = new ArrayList<>();
        removeEntities = new ArrayList<>();
    
        // ### MAP ###
        mapGenerator = new MapGenerator();
    
        player = new Player(mapGenerator.worldSize/2f*16, mapGenerator.worldSize/2f*16);
        protect = new Protect(mapGenerator.worldSize/2f*16, mapGenerator.worldSize/2f*16);
        controlling = player;
        
        newWorld();
    
        // ### SHADERS ###
        WHITEN = game.loadShader("assets/shaders/whiten.glsl",
                "assets/shaders/passThrough.glsl");
        WATER_SHADER = game.loadShader("assets/shaders/water.glsl");
        
        // ### FONTS ###
        retroFont = game.createFont("assets/fonts/PressStart2P.ttf", 8, false);
        
        // ### SOUND ###
        soundManager.addClip("assets/sounds/hit.wav", "hit", -3f);
        soundManager.addClip("assets/sounds/stepping.wav", "stepping", 0f);
        soundManager.addClip("assets/sounds/stepLeft.wav", "stepLeft", -5f);
        soundManager.addClip("assets/sounds/stepRight.wav", "stepRight", -5f);
        soundManager.addClip("assets/sounds/swing.wav", "swing", -11f);
        soundManager.addClip("assets/sounds/death.wav", "death", -5f);
        
        soundManager.addClip("assets/sounds/enemy0.wav", "enemy0", 0f);
        soundManager.addClip("assets/sounds/enemy1.wav", "enemy1", 0f);
        soundManager.addClip("assets/sounds/enemy2.wav", "enemy2", 0f);
        soundManager.addClip("assets/sounds/enemy3.wav", "enemy3", 0f);
        
        soundManager.addClip("assets/sounds/backgroundNoise.wav", "backgroundNoise", -2f);
        soundManager.playSound("backgroundNoise", Integer.MAX_VALUE);
    }
    
    @Override
    public void updateTick() {
        if(!gameStarted) {
            if(game.input.isKeyDown(PConstants.ENTER)) {
                gameStarted = true;
            }
            return;
        }
        
        mouseX = game.mouseX / 3f + player.x - 180;
        mouseY = game.mouseY / 3f + player.y - 120;
        absMouseX = game.mouseX / 3f;
        absMouseY = game.mouseY / 3f;
        
        // ### WORLD ###
        for(GameObject object : world) {
            object.update(this);
            
            if(object.dead) {
                removeWorld.add(object);
            }
        }
        
        world.addAll(addWorld);
        addWorld.clear();
        world.removeAll(removeWorld);
        removeWorld.clear();
        
        // ### SWITCH CHARACTER ###
        if(game.input.isButtonDown(PConstants.RIGHT)) {
            if(controlling == player) {
                controlling = protect;
            } else {
                controlling = player;
            }
        }
        
        // ### ENTITIES ###
        for(Entity entity: entities) {
            entity.update(this);
            
            if(entity.dead) {
                removeEntities.add(entity);
            }
        }
        
        entities.addAll(addEntities);
        addEntities.clear();
        entities.removeAll(removeEntities);
        removeEntities.clear();
        
        if(newWorldPending) {
            newWorld();
        }
    
        // ### GAME STATE ###
        
        if(!entities.contains(player) || !entities.contains(protect)) {
            if(!levelFailed && !levelComplete) {
                levelFailed = true;
            }
        }
    
        // ### LEVEL COMPLETED ###
        if(levelComplete) {
            entities.remove(player);
            entities.remove(protect);
        
            nextLevelCountdown--;
        
            if(nextLevelCountdown <= 0) {
                newWorld();
            
                levelComplete = false;
            }
        }
        
        // ### LEVEL FAILED ###
        if(levelFailed) {
            if(absMouseX > 120 && absMouseY > 168 &&
                    absMouseX < 240 && absMouseY < 216 &&
                    game.input.isButtonDown(PConstants.LEFT)) {
    
                // -- New game --
                
                if(levelFailed) {
                    player = new Player(0, 0);
                    protect = new Protect(0, 0);
                }
                
                levelCounter = 1;
    
                newWorld();
    
                levelFailed = false;
            }
        }
        
        // ### PORTAL ###
        mapGenerator.portal.update(this);
    }
    
    @Override
    public void render() {
        screenBuffer.beginDraw();
        
        if(!gameStarted) {
            screenBuffer.image(START, 0 ,0);
            
            screenBuffer.endDraw();
    
            game.image(screenBuffer, 0, 0, game.pixelWidth, game.pixelHeight);
            
            return;
        }
        
        // Clear the buffer
        screenBuffer.fill(25, 25, 25, 255);
        screenBuffer.rect(0, 0, screenBuffer.width, screenBuffer.height);
        
        // Translate to player
        screenBuffer.translate(-controlling.x+180, -controlling.y+120);
        
        // Render world
        for(GameObject object : world) {
            // Render only if inside the view
            if(object.x+object.width > controlling.x-180 && object.y+object.height > controlling.y-120 &&
                    object.x < controlling.x+180 && object.y < controlling.y+120) {
                object.render(this);
            }
        }
        
        // Render entities
        for(GameObject entity : entities) {
            entity.render(this);
        }
    
        // Portal
        mapGenerator.portal.render(this);
    
        // Translate to player
        screenBuffer.translate(controlling.x-180, controlling.y-120);
        
        // ### STATS ###
        screenBuffer.textFont(retroFont, 8);
        
        screenBuffer.textAlign(PConstants.LEFT);
        
        // -- Level --
        screenBuffer.fill(64, 64, 64);
        screenBuffer.text("LEVEL: "+levelCounter, 8+1, 240-8-8+1);
        screenBuffer.fill(128, 128, 128);
        screenBuffer.text("LEVEL: "+levelCounter, 8, 240-8-8);
        
        // -- Enemies --
        screenBuffer.fill(64, 64, 64);
        screenBuffer.text("ENEMIES: "+(entities.size()-2), 8+1 + 80, 240-8-8+1);
        screenBuffer.fill(128, 128, 128);
        screenBuffer.text("ENEMIES: "+(entities.size()-2), 8 + 80, 240-8-8);
        
        // ### LEVEL FAILEd/COMPLETE ###
        if(levelComplete) screenBuffer.image(LEVEL_CLEARED, 0, 0);
        if(levelFailed) {
            screenBuffer.image(LEVEL_FAILED, 0, 0);
            
            if(absMouseX > 120 && absMouseY > 168 &&
                    absMouseX < 240 && absMouseY < 216) {
                screenBuffer.image(PLAY_BUTTON_HOVER, 120, 168);
            } else {
                screenBuffer.image(PLAY_BUTTON_IDLE, 120, 168);
            }
            
            screenBuffer.textFont(retroFont, 12);
            screenBuffer.noStroke();
            screenBuffer.textAlign(PConstants.CENTER);
            screenBuffer.fill(80, 80, 80);
            screenBuffer.text("HIGHEST LEVEL: "+levelCounter, 180+1, 60+1);
            screenBuffer.fill(160, 160, 160);
            screenBuffer.text("HIGHEST LEVEL: "+levelCounter, 180, 60);
        }
        
        screenBuffer.endDraw();
    
        /*// ### WATER ###
        
        shaderBuffer.beginDraw();
        
        shaderBuffer.fill(0, 0, 0, 255);
        shaderBuffer.rect(0, 0, shaderBuffer.width, shaderBuffer.height);
    
        shaderBuffer.image(screenBuffer, 0, 0);
        
        shaderBuffer.endDraw();*/
        
        // Draw buffer onto screen
        game.image(screenBuffer, 0, 0, game.pixelWidth, game.pixelHeight);
    }
    
    /**
     * Generates a new world but keeps the player and Protect.
     * Used in holes, stairs and ladders.
     * */
    private void newWorld() {
        
        // Clear map
        world.clear();
        entities.clear();
        
        // Generate map & tiles
        mapGenerator.generateTiles();
        map = mapGenerator.generate();
        mapObjects = new Tile[map.length][map[0].length];
        
        // Construct the new map
        Tile tile;
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                if(map[i][j] != 0) {
                    if(map[i][j] == 25) {
                        addEntities.add(new Enemy(i * 16, j * 16, random.nextInt(ENEMY_DATA.length-1)));
    
                        addWorld.add(tile = new Tile(i * 16, j * 16,
                                mapGenerator.objects[Math.min(24, random.nextInt(
                                        mapGenerator.objects.length-1))],
                                j < map[i].length - 1 ? (map[i][j + 1] == 0) : true));
                    } else {
                        addWorld.add(tile = new Tile(i * 16, j * 16, map[i][j] - 1,
                                j < map[i].length - 1 ? (map[i][j + 1] == 0) : true));
                    }
    
                    mapObjects[i][j] = tile;
                } else {
                    mapObjects[i][j] = null;
                }
            }
        }
    
    
        player.x = mapGenerator.worldSize/2f*16;
        player.y = mapGenerator.worldSize/2f*16;
        protect.x = mapGenerator.worldSize/2f*16;
        protect.y = mapGenerator.worldSize/2f*16;
        
        // Insert player, Protect and portal
        entities.add(player);
        entities.add(protect);
        
        controlling = player;
    }
    
    public boolean checkForTile(float x, float y) {
        int tileX = (int)Math.floor(x / 16);
        int tileY = (int)Math.floor(y / 16);
        if(tileX >= 0 &&
                tileY >= 0 &&
                tileX < map.length &&
                tileY < map[0].length) return map[tileX][tileY] != 0;
        return false;
    }
    
    public int tileAt(float x, float y) {
        int tileX = (int)Math.floor(x / 16);
        int tileY = (int)Math.floor(y / 16);
        if(tileX >= 0 &&
                tileY >= 0 &&
                tileX < map.length &&
                tileY < map[0].length) return map[tileX][tileY];
        return -1;
    }
    
    
    
    @Override
    public void settings() {
        getGame().noSmooth();
    }
    
    @Override
    public void init() {
    
    }
    
    double deltaTime = 0;
    @Override
    public void update(double v) {
        deltaTime += v;
        while(deltaTime > 1/60f) {
            deltaTime -= 1/60f;
            updateTick();
        }
    }
}
