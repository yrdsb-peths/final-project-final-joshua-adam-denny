import greenfoot.*;
/**
 * Write a description of class AudioManagert here.
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 7, 2025)
 */
public class AudioManager 
{
    private static GreenfootSound currentMusic = null;
    private static Thread introWatcher = null;
    
    public static void playMusic(GreenfootSound music) {
        stopMusic();
        currentMusic = music;
        int vol = PlayerPrefs.getData("VolumeMusic", 30);
        currentMusic.setVolume(vol);
        currentMusic.playLoop();
    }
    
    /**
     * Play an intro track once, then switch to the looping track.
     * @param intro the one-time opening music
     * @param loop  the music to playLoop() after the intro ends
     * @param world the World to which we’ll add a small helper Actor
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
    
    public static void setMusicVolume(int volume) {
        int vol = (int)Utils.clamp(volume, 0, 100);
        PlayerPrefs.setData("VolumeMusic", vol);
        if (currentMusic != null) {
            currentMusic.setVolume(vol);
        }
    }
    
    
    public static void playSFX(GreenfootSound audioPlayer)
    {
        int vol = (int) PlayerPrefs.getData("VolumeSFX", 30);
        audioPlayer.setVolume(vol);
        audioPlayer.play();
    }
    public static void playSFX(GreenfootSound audioPlayer, int volume)
    {
        audioPlayer.setVolume(volume);
        audioPlayer.play();
    }
    
    public static void playSpecialSFX(GreenfootSound audioPlayer)
    {
        int vol = (int) PlayerPrefs.getData("VolumeSpecialSFX", 75);
        audioPlayer.setVolume(vol);
        audioPlayer.play();
    }
    public static void playSpecialSFX(GreenfootSound audioPlayer, int volume)
    {
        audioPlayer.setVolume(volume);
        audioPlayer.play();
    }

    public static void playAudio(GreenfootSound audioPlayer, int volume)
    {
        audioPlayer.setVolume(volume);
        audioPlayer.playLoop();
    }

}
