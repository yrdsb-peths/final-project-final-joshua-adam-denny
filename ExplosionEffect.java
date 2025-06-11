import greenfoot.*;
import java.util.*;
/**
 * ExplosionEffect Class is an effect that displays an explosion animation.
 * 
 * @author Joshua Stevens
 * @version Version 1.0
 */
public class ExplosionEffect extends Actor {
    private static final int FRAME_COUNT = 8;
    private static final int FRAME_DURATION = 4; // Each frame shown for 4 acts
    private int timer;
    private int radius;

    private List<GreenfootImage> originalFrames = new ArrayList<>();

    // These are size multipliers for each frame (relative to radius * 2)
    private final double[] SCALE_FACTORS = {
        0.3, // frame 0
        0.6, // frame 1
        0.85, // frame 2
        1.0,  // frame 3 (peak)
        1.0,  // frame 4 (peak)
        0.8,  // frame 5
        0.5,  // frame 6
        0.3   // frame 7
    };

    public ExplosionEffect(int radius) {
        this.radius = radius;
        this.timer = FRAME_COUNT * FRAME_DURATION;

        // Load base images once
        for (int i = 0; i < FRAME_COUNT; i++) {
            originalFrames.add(new GreenfootImage("explosion/explosion" + i + ".png"));
        }

        updateImage();
    }

    public void act() {
        timer--;
        if (timer < 0) {
            getWorld().removeObject(this);
        } else {
            updateImage();
        }
    }

    private void updateImage() {
        int currentFrame = (FRAME_COUNT * FRAME_DURATION - timer - 1) / FRAME_DURATION;
        currentFrame = Math.min(currentFrame, FRAME_COUNT - 1);

        GreenfootImage base = new GreenfootImage(originalFrames.get(currentFrame));
        double scaleFactor = SCALE_FACTORS[currentFrame];
        int targetSize = (int) (radius * 2 * scaleFactor);

        base.scale(targetSize, targetSize);
        setImage(base);
    }
}
