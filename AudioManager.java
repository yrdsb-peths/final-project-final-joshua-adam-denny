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
    

    /**
     * Plays a sound effect (SFX) with the current SFX volume setting.
     * This method is used for general sound effects in the game.
     * 
     * @param audioPlayer The GreenfootSound object to play as SFX.
     */
    public static void playSFX(GreenfootSound audioPlayer)
    {
        if (PlayerPrefs.getData("PreformanceMode",false)) return;
        int vol = (int) PlayerPrefs.getData("VolumeSFX", 30);
        audioPlayer.setVolume(vol);
        audioPlayer.play();
        activeSFX.add(audioPlayer);
    }
    

    /**
     * Plays a special sound effect that is not affected by the main SFX volume.
     * This is used for important game events like achievements or critical actions.
     * 
     * @param audioPlayer The GreenfootSound object to play as a special SFX.
     */
    public static void playSpecialSFX(GreenfootSound audioPlayer)
    {
        if (PlayerPrefs.getData("PreformanceMode",false)) return;
        int vol = (int) PlayerPrefs.getData("VolumeSpecialSFX", 45);
        audioPlayer.setVolume(vol);
        audioPlayer.play();
        activeSFX.add(audioPlayer);
    }
    

    /**
     * Plays a sound effect in a loop, such as background ambient sounds or UI effects.
     * This method is used for sounds that should continue playing until explicitly stopped.
     * 
     * @param sfx The GreenfootSound object to play in a loop.
     */
    public static void playLoopingSFX(GreenfootSound sfx) {
        if (PlayerPrefs.getData("PreformanceMode",false)) return;
        int vol = PlayerPrefs.getData("VolumeSFX", 30);
        sfx.setVolume(vol);
        sfx.playLoop();
        loopingSFX.add(sfx);

    }
   
    
    /**
     * Stops a specific looping sound effect.
     * This method is used to stop a sound effect that was previously set to loop.
     * 
     * @param sfx The GreenfootSound object to stop looping.
     */
    public static void stopLoopingSFX(GreenfootSound sfx) {
        if (loopingSFX.remove(sfx)) {
            sfx.stop();
        }
    }
    
    
    /**
     * Stops all currently playing looping sound effects.
     * This method is used to clear all looping sound effects, such as when changing scenes or states.
     */
    public static void stopAllLoopingSFX() {
        for (GreenfootSound sfx : loopingSFX) {
            sfx.stop();
        }
        loopingSFX.clear();
    }
    

    /**
     * Stops all currently playing sound effects (SFX).
     * This method is used to clear all sound effects, such as when changing scenes or states.
     */
    public static void stopAllSFX() {
        for (GreenfootSound sfx : activeSFX) {
            sfx.stop();
        }
        activeSFX.clear();
    }
    
    
    
    
    /*
     * GENERAL AUDIO PLAYER
     */

    /**
     * Plays a GreenfootSound audio player with the specified volume.
     * This method is used for one-time audio playback, such as sound effects or notifications.
     * 
     * @param audioPlayer The GreenfootSound object to play.
     * @param volume The volume level to set (0-100).
     */
    public static void playAudio(GreenfootSound audioPlayer, int volume)
    {
        audioPlayer.setVolume(volume);
        audioPlayer.play();
    }

    /**
     * Plays a GreenfootSound audio player in a loop with the specified volume.
     * This method is used for continuous audio playback, such as background music or ambient sounds.
     * 
     * @param audioPlayer The GreenfootSound object to play in a loop.
     * @param volume The volume level to set (0-100).
     */
    public static void playAudioLoop(GreenfootSound audioPlayer, int volume)
    {
        audioPlayer.setVolume(volume);
        audioPlayer.playLoop();
    }

}
