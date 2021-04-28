# LDJam46
Game made for LD Jam 46 in 48 hours. Game is written in Java using my custom library built around Processing 3 to provide abstractions and a game loop.

# Compilation

## Manual

Requried for compiling:
  - unzip
  - make
  - javac, jar (Open or Oracle JDK 8)

Compile using:
```bash
make
```

## Using IntelliJ IDEA

Importing libraries:
1. Project Settings > Libraries > Add > Choose all libraries in 'lib/' > "Library will be added onto selected modules" > OK

Adding run configuration:

2. Run (Toolbar) > Edit configurations > Add > Application
   * Main Class: com.ydgames.ldjam46.Main
   * Working directory: project root folder (where 'assets/' is located)

3. Profit!

# Running

The executable .jar should be in a folder where 'assets/' is also located, containing all of the required assets for the game.

Game can be run using Oracle or Open JRE 8
