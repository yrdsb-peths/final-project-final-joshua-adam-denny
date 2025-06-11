import greenfoot.*;
import java.util.*;
/**
 * A AudioManager that handles all audio playback in the game. 
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 7, 2025)
 */
public class AudioManager 
{
    private static GreenfootSound currentMusic = null;
    private static Thread introWatcher = null;
    private static final Set<GreenfootSound> activeSFX = new HashSet<>();
    private static final Set<GreenfootSound> loopingSFX = new HashSet<>();
    
    /*
     * MUSIC PLAYER FUNCTIONS
     */
    
    
    /*
     * Play a looping music track.
     * @param music The GreenfootSound object to play in a loop.
     */
    public static void playMusic(GreenfootSound music) {
        stopMusic();
        currentMusic = music;
        int vol = PlayerPrefs.getData("VolumeMusic", 30);
        currentMusic.setVolume(vol);
        currentMusic.playLoop();
    }
    
    /**
     * Play an intro track once, then switch to the looping track.
     * @param intro the one time opening music
     * @param loopTrack Looping track to play after the intro finishes
     */
    public static void playMusic(GreenfootSound intro, GreenfootSound loopTrack) {
        stopMusic();  // kills any existing music & watcher
        currentMusic = intro;

        intro.setVolume(PlayerPrefs.getData("VolumeMusic", 30));
        intro.play();
        // spawn a watcher thread to wait for the intro to finish
        introWatcher = new Thread(() -> {
            try {
                // poll every 100 ms until the intro stops or gets interrupted
                while (intro.isPlaying()) {
                    Thread.sleep(10);
                }
                // if we weren’t stopped mid-intro, start the loop
                if (!Thread.currentThread().isInterrupted()) {
                    currentMusic = loopTrack;
                    loopTrack.setVolume(PlayerPrefs.getData("VolumeMusic", 30));
                    loopTrack.playLoop();
                }
            } catch (InterruptedException e) {
                // if someone called stopMusic(), we’ll get interrupted here—just exit
            }
        });
        introWatcher.setDaemon(true);
        introWatcher.start();
    }
    

    /**
     * Stop all Music Players. and kills the watcher thread if it exists.
        * This will stop the currently playing music and any intro watcher thread. 
     */
    public static void stopMusic() {
        // kill the watcher thread, if any
        if (introWatcher != null) {
            introWatcher.interrupt();
            introWatcher = null;
        }
        // stop the actual sound
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }
    }

    /**
     * Set the volume of the currently playing music.
     * @param volume The volume level to set (0-100).
     */
    public static void setMusicVolume(int volume) {
        int vol = (int)Utils.clamp(volume, 0, 100);
        PlayerPrefs.setData("VolumeMusic", vol);
        if (currentMusic != null) {
            currentMusic.setVolume(vol);
        }
    }
    
    
    /*
     * *SFX FUNCTIONS*
     */
    
    public static void playSFX(GreenfootSound audioPlayer)
    {
        if (PlayerPrefs.getData("PreformanceMode",false)) return;
        int vol = (int) PlayerPrefs.getData("VolumeSFX", 30);
        audioPlayer.setVolume(vol);
        audioPlayer.play();
        activeSFX.add(audioPlayer);
    }
    
    public static void playSpecialSFX(GreenfootSound audioPlayer)
    {
        if (PlayerPrefs.getData("PreformanceMode",false)) return;
        int vol = (int) PlayerPrefs.getData("VolumeSpecialSFX", 45);
        audioPlayer.setVolume(vol);
        audioPlayer.play();
        activeSFX.add(audioPlayer);
    }
    
    public static void playLoopingSFX(GreenfootSound sfx) {
        if (PlayerPrefs.getData("PreformanceMode",false)) return;
        int vol = PlayerPrefs.getData("VolumeSFX", 30);
        sfx.setVolume(vol);
        sfx.playLoop();
        loopingSFX.add(sfx);

    }
    
    public static void stopLoopingSFX(GreenfootSound sfx) {
        if (loopingSFX.remove(sfx)) {
            sfx.stop();
        }
    }
    
    
    public static void stopAllLoopingSFX() {
        for (GreenfootSound sfx : loopingSFX) {
            sfx.stop();
        }
        loopingSFX.clear();
    }
    
    public static void stopAllSFX() {
        for (GreenfootSound sfx : activeSFX) {
            sfx.stop();
        }
        activeSFX.clear();
    }
    
    
    
    
    /*
     * GENERAL AUDIO PLAYER
     */
    public static void playAudio(GreenfootSound audioPlayer, int volume)
    {
        audioPlayer.setVolume(volume);
        audioPlayer.play();
    }
    public static void playAudioLoop(GreenfootSound audioPlayer, int volume)
    {
        audioPlayer.setVolume(volume);
        audioPlayer.playLoop();
    }

}
