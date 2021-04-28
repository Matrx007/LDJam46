PWD = $(shell pwd)

JC = javac

LIBRARIES = $(PWD)/lib/MXEngine.jar

CP = $(LIBRARIES)
SP = "$(PWD)/src/"
OP = "$(PWD)/build"

JFLAGS = -g -verbose

all: build/com/ydgames/ldjam46/Enemy.class build/com/ydgames/ldjam46/Entity.class build/com/ydgames/ldjam46/GameObject.class build/com/ydgames/ldjam46/Main.class build/com/ydgames/ldjam46/MapGenerator.class build/com/ydgames/ldjam46/Player.class build/com/ydgames/ldjam46/Portal.class build/com/ydgames/ldjam46/Protect.class build/com/ydgames/ldjam46/SoundManager.class build/com/ydgames/ldjam46/Tile.class
	unzip -d build/ $(shell printf "$(LIBRARIES)" | tr ':' ' ')
	cp -r src/META-INF build/
	jar cvmf build/META-INF/MANIFEST.MF LDJam46.jar -C build/ .

build/com/ydgames/ldjam46/Enemy.class: src/com/ydgames/ldjam46/Enemy.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/ydgames/ldjam46/Enemy.java

build/com/ydgames/ldjam46/Entity.class: src/com/ydgames/ldjam46/Entity.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/ydgames/ldjam46/Entity.java

build/com/ydgames/ldjam46/GameObject.class: src/com/ydgames/ldjam46/GameObject.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/ydgames/ldjam46/GameObject.java

build/com/ydgames/ldjam46/Main.class: src/com/ydgames/ldjam46/Main.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/ydgames/ldjam46/Main.java

build/com/ydgames/ldjam46/MapGenerator.class: src/com/ydgames/ldjam46/MapGenerator.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/ydgames/ldjam46/MapGenerator.java

build/com/ydgames/ldjam46/Player.class: src/com/ydgames/ldjam46/Player.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/ydgames/ldjam46/Player.java

build/com/ydgames/ldjam46/Portal.class: src/com/ydgames/ldjam46/Portal.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/ydgames/ldjam46/Portal.java

build/com/ydgames/ldjam46/Protect.class: src/com/ydgames/ldjam46/Protect.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/ydgames/ldjam46/Protect.java

build/com/ydgames/ldjam46/SoundManager.class: src/com/ydgames/ldjam46/SoundManager.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/ydgames/ldjam46/SoundManager.java

build/com/ydgames/ldjam46/Tile.class: src/com/ydgames/ldjam46/Tile.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/ydgames/ldjam46/Tile.java

clean:
	rm -rf build/
	rm LDJam46.jar