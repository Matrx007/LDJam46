package com.ydgames.ldjam46;

import java.io.*;
import java.util.*;
import javax.sound.sampled.*;

public class SoundManager {
    private javax.sound.sampled.Line.Info lineInfo;
    private HashMap<String, AudioFormat> audioFormats;
    private HashMap<String, Integer> sizes;
    private HashMap<String, Float> volumes;
    private HashMap<String, DataLine.Info> info;
    private HashMap<String, byte[]> audioData;

    public SoundManager() {
        audioFormats = new HashMap<>();
        sizes = new HashMap<>();
        info = new HashMap<>();
        audioData = new HashMap<>();
        volumes = new HashMap<>();
    }

    public void addClip(String file, String name, float defaultVolume) {
        try {
            InputStream stream;
            
            stream = new FileInputStream(new File(file));
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    loadStream(stream));
            AudioFormat af = audioInputStream.getFormat();
            int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
            byte[] audio = new byte[size];
            DataLine.Info info = new DataLine.Info(Clip.class, af, size);
            audioInputStream.read(audio, 0, size);

            audioFormats.put(name, af);
            sizes.put(name, size);
            this.info.put(name, info);
            audioData.put(name, audio);
            volumes.put(name, defaultVolume);
        } catch (Exception e) {
            System.err.println("Failed to load file \""+name+"\"");
            e.printStackTrace();
        }
    }

    private ByteArrayInputStream loadStream(InputStream inputstream) throws
            IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        byte data[] = new byte[1024];
        for(int i = inputstream.read(data); i != -1; i = inputstream.read(data))
            bytearrayoutputstream.write(data, 0, i);

        inputstream.close();
        bytearrayoutputstream.close();
        data = bytearrayoutputstream.toByteArray();
        return new ByteArrayInputStream(data);
    }

    public Clip playSound(String name) {
        if(!name.contains(name)) {
            System.out.println("SoundManager: unknown sample \""+name+"\"");
        } else {
            try {
                Clip clip = (Clip) AudioSystem.getLine(info.get(name));
                clip.open(audioFormats.get(name), audioData.get(name), 0, sizes.get(name));
                FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                floatControl.setValue(volumes.get(name));
                clip.start();
                return clip;
            } catch (Exception e) {
                System.err.println("Failed to play audio \""+name+"\"");
                e.printStackTrace();
            }
        }
        return null;
    }

    public Clip playSound(String name, int loop) {
        if(!name.contains(name)) {
            System.out.println("SoundManager: unknown sample \""+name+"\"");
        } else {
            try {
                Clip clip = (Clip) AudioSystem.getLine(info.get(name));
                clip.loop(loop);
                clip.open(audioFormats.get(name), audioData.get(name), 0, sizes.get(name));
                FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                floatControl.setValue(volumes.get(name));
                clip.start();
                return clip;
            } catch (Exception e) {
                System.err.println("Failed to play audio \""+name+"\"");
                e.printStackTrace();
            }
        }
        return null;
    }

    public Clip playSound(String name, float volume) {
        if(!name.contains(name)) {
            System.out.println("SoundManager: unknown sample \""+name+"\"");
        } else {
            try {

                Clip clip = (Clip) AudioSystem.getLine(info.get(name));
                clip.open(audioFormats.get(name), audioData.get(name), 0, sizes.get(name));
                FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                floatControl.setValue(volumes.get(name)+volume);
                clip.start();
                return clip;
            } catch (Exception e) {
                System.err.println("Failed to play audio \""+name+"\"");
                e.printStackTrace();
            }
        }
        return null;
    }

    public Clip playSound(String name, int loop, float volume) {
        if(!name.contains(name)) {
            System.out.println("SoundManager: unknown sample \""+name+"\"");
        } else {
            try {
                Clip clip = (Clip) AudioSystem.getLine(info.get(name));
                clip.loop(loop);
                clip.open(audioFormats.get(name), audioData.get(name), 0, sizes.get(name));
                FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                floatControl.setValue(volumes.get(name)+volume);
                clip.start();
                return clip;
            } catch (Exception e) {
                System.err.println("Failed to play audio \""+name+"\"");
                e.printStackTrace();
            }
        }
        return null;
    }
}
