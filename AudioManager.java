import greenfoot.*;
/**
 * Write a description of class AudioManagert here.
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 7, 2025)
 */
public class AudioManager 
{
    public static void playMusic(GreenfootSound audioPlayer)
    {
        int vol = (int) PlayerPrefs.getData("VolumeMusic", 30);
        audioPlayer.setVolume(vol);
        audioPlayer.play();
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
