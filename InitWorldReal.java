import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Init world reakl
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 4, 2025)
 */
public class InitWorldReal extends World {
    private static final int WORLD_WIDTH = 1160;
    private static final int WORLD_HEIGHT = 600;
    private static final int FADE_DURATION_MS = 2000;
    private static final int PHASE1_DURATION_MS = 1500;
    private static final int PHASE2_DURATION_MS = 1500;
    private static final int PHASE2_DELAY_MS = 2000;
    private static final int PHASE3_DURATION_MS = 2000;
    private static final int PADDING = 10;

    private ImageActor bg1Scuffed;
    private ImageActor bg1Engine;
    private ImageActor bg1PoweredByGreenfoot;
    private ImageActor blackOverlay;

    private int centerX, centerY;
    private int halfMoveScuffed, halfMoveEngine;

    private int phase = 0;
    private long phaseStartTime;

    private int newImageStartX, newImageStartY;
    private int newImageTargetY;

    public InitWorldReal() {
        super(WORLD_WIDTH, WORLD_HEIGHT, 1);

        GreenfootImage bg = new GreenfootImage(getWidth(), getHeight());
        bg.setColor(new Color(66, 66, 66));
        bg.fill();
        setBackground(bg);

        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        bg1Scuffed = new ImageActor("ui/scuffedBG.png");
        bg1Engine = new ImageActor("ui/engineBG.png");
        bg1PoweredByGreenfoot = new ImageActor("ui/poweredbygreenfoot.png");

        bg1Scuffed.setTransparency(0);
        bg1Engine.setTransparency(0);
        bg1PoweredByGreenfoot.setTransparency(0);

        addObject(bg1PoweredByGreenfoot, centerX, centerY);
        addObject(bg1Engine, centerX, centerY);
        addObject(bg1Scuffed, centerX, centerY);

        halfMoveScuffed = bg1Scuffed.getImage().getWidth() / 2;
        halfMoveEngine = bg1Engine.getImage().getWidth() / 2;

        blackOverlay = new ImageActor(WORLD_WIDTH, WORLD_HEIGHT);
        blackOverlay.setColor(new Color(0, 0, 0));
        blackOverlay.fill();
        blackOverlay.setTransparency(0);
        addObject(blackOverlay, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);

        phase = 0;
        phaseStartTime = System.currentTimeMillis();
    }

    @Override
    public void act() {
        long now = System.currentTimeMillis();
        long elapsed0 = now - phaseStartTime;

        switch (phase) {
            case 0:
                if (elapsed0 < FADE_DURATION_MS) {
                    int alpha = (int) Math.round(
                        Utils.map(elapsed0, 0, FADE_DURATION_MS, 0, 255)
                    );
                    bg1Scuffed.setTransparency(alpha);
                } else {
                    bg1Scuffed.setTransparency(255);
                    enterPhase1();
                }
                break;

            case 1:
                if (elapsed0 < PHASE1_DURATION_MS) {
                    double t = (double) elapsed0 / PHASE1_DURATION_MS;
                    double ease = (1 - Math.cos(Math.PI * t)) * 0.5;

                    double newXScuffed = centerX - halfMoveScuffed * ease;
                    bg1Scuffed.setLocation((int) Math.round(newXScuffed), centerY);

                    double newXEngine = centerX + halfMoveEngine * ease;
                    bg1Engine.setLocation((int) Math.round(newXEngine), centerY);
                } else {
                    bg1Scuffed.setLocation(centerX - halfMoveScuffed, centerY);
                    bg1Engine.setLocation(centerX + halfMoveEngine, centerY);
                    enterPhase2();
                }
                break;

            case 2:
                long elapsed2 = System.currentTimeMillis() - phaseStartTime;
                if (elapsed2 < PHASE2_DURATION_MS) {
                    double t = (double) elapsed2 / PHASE2_DURATION_MS;
                    double ease = (1 - Math.cos(Math.PI * t)) * 0.5;

                    double newY = newImageStartY + (newImageTargetY - newImageStartY) * ease;
                    bg1PoweredByGreenfoot.setLocation(newImageStartX, (int) Math.round(newY));
                } else {
                    bg1PoweredByGreenfoot.setLocation(newImageStartX, newImageTargetY);
                    enterPhase2Delay();
                }
                break;
                
            case 3:
                long delayElapsed = System.currentTimeMillis() - phaseStartTime;
                if (delayElapsed >= PHASE2_DELAY_MS) {
                    enterPhase3();
                }
                break;

            case 4:
                long elapsed3 = System.currentTimeMillis() - phaseStartTime;
                if (elapsed3 < PHASE3_DURATION_MS) {
                    int alpha = (int) Math.round(
                        Utils.map(elapsed3, 0, PHASE3_DURATION_MS, 0, 255)
                    );
                    alpha = (int) Utils.clamp(alpha, 0, 255);
                    blackOverlay.setTransparency(alpha);
                } else {
                    blackOverlay.setTransparency(255);
                    phase = 4;
                }
                break;

            default:
                break;
        }
    }

    private void enterPhase1() {
        phase = 1;
        phaseStartTime = System.currentTimeMillis();
        bg1Engine.setTransparency(255);
    }

    private void enterPhase2() {
        phase = 2;
        phaseStartTime = System.currentTimeMillis();

        int engineX = bg1Engine.getX();
        int engineY = bg1Engine.getY();
        newImageStartX = engineX;
        newImageStartY = engineY;
        bg1PoweredByGreenfoot.setLocation(engineX, engineY);
        bg1PoweredByGreenfoot.setTransparency(255);

        int engineHalfH = bg1Engine.getImage().getHeight() / 2;
        int newHalfH = bg1PoweredByGreenfoot.getImage().getHeight() / 2;
        newImageTargetY = (engineY + engineHalfH + PADDING) + newHalfH;
    }
    
    private void enterPhase2Delay() {
        phase = 3;
        phaseStartTime = System.currentTimeMillis();
    }

    private void enterPhase3() {
        phase = 4;
        phaseStartTime = System.currentTimeMillis();
        blackOverlay.setTransparency(0);
    }
}
