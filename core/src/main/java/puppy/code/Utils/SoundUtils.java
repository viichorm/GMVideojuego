package puppy.code.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundUtils {

    public static Sound loadSound(String path) {
        return Gdx.audio.newSound(Gdx.files.internal(path));
    }

    public static Music loadMusic(String path, boolean loop, float volume) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
        music.setLooping(loop);
        music.setVolume(volume);
        return music;
    }

    public static void playSound(Sound sound) {
        if (sound != null) {
            sound.play();
        }
    }

    public static void dispose(Sound sound) {
        if (sound != null) {
            sound.dispose();
        }
    }

    public static void dispose(Music music) {
        if (music != null) {
            music.dispose();
        }
    }
}
