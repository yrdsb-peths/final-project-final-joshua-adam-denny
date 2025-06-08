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
    public static void playMusic(GreenfootSound music) {
        
        if (currentMusic != null && currentMusic != music) {
            currentMusic.stop();
        }
        currentMusic = music;
        int vol = PlayerPrefs.getData("VolumeMusic", 30);
        currentMusic.setVolume(vol);
        if (!currentMusic.isPlaying()) {
            currentMusic.playLoop();
        }
    }
    

    public static void stopMusic() {
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
    
    public static void playMusic(GreenfootSound audioPlayer, int volume)
    {
        audioPlayer.setVolume(volume);
        audioPlayer.play();
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
