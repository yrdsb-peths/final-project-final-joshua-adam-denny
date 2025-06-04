import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.IOException;

/**
 * Init world real
 * 
 * @author Denny Ung
 * @version Version 1.4.4 (June 4, 2025)
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

    private GreenfootImage bg = new GreenfootImage(getWidth(), getHeight());
    
    private ImageActor bg1Scuffed;
    private ImageActor bg1Engine;
    private ImageActor bg1PoweredByGreenfoot;
    private ImageActor blackOverlay;
    
    private PolyRender bg2PolyRenderModel;
    private ImageActor bg2PolyRender;
    private PolyRender bg2PRCubeModel;
    private ImageActor bg2PRCube;
    
    private GreenfootImage bg3Title;
    private GreenfootImage bg3TitleBlur;
    
    private ImageActor bg3fade;
    
    private int centerX, centerY;
    private int halfMoveScuffed, halfMoveEngine;

    private int phase = 0;
    private long phaseStartTime;

    private int newImageStartX, newImageStartY;
    private int newImageTargetY;

    private Button startButton;
    private Label startLabel;

    
    private Label statusLabel;
    private Boolean connectionResult = null; 
    private int scuffedAPIAttempts = 0;
    private boolean scuffedAPIConnectioninProgress = false;
    
    public InitWorldReal() {
        super(WORLD_WIDTH, WORLD_HEIGHT, 1);

        bg.setColor(new Color(66, 66, 66));
        bg.fill();
        setBackground(bg);

        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        bg1Scuffed = new ImageActor("ui/scuffedBG.png");
        bg1Engine = new ImageActor("ui/engineBG.png");
        bg1PoweredByGreenfoot = new ImageActor("ui/poweredbygreenfoot.png");

        double[][][] polyRenderText = new double[0][][];
        double[][][] polyRenderCube = new double[0][][];
        try {
            polyRenderCube = ObjParser.parseObj("3dModels/cube.obj", 100);
            polyRenderText = ObjParser.parseObj("3dModels/polyrender.obj", 100);
        } catch(IOException e) {
            // handle exception or log
        }
        
        bg2PolyRenderModel = new PolyRender(polyRenderText);
        bg2PRCubeModel = new PolyRender(polyRenderCube);
        
        bg2PolyRenderModel.position(0,0,250);
        bg2PolyRenderModel.rotate(Math.toRadians(75.0),Math.toRadians(0.0),Math.toRadians(0.0));
        bg2PolyRenderModel.setScale(-1);
        bg2PolyRenderModel.setRenderVersion(1);
        bg2PolyRenderModel.setVersionOneRender_MinMaxLighting(100.0,255.0);
        
        bg2PRCubeModel.position(0,0,500);
        bg2PRCubeModel.rotate(Math.toRadians(35.0),Math.toRadians(45.0),Math.toRadians(0.0));
        bg2PRCubeModel.setRenderVersion(1);
        bg2PRCubeModel.setVersionOneRender_MinMaxLighting(50.0,255.0);
        
        bg2PolyRenderModel.act();
        bg2PRCubeModel.act();
        
        bg2PolyRender = new ImageActor(bg2PolyRenderModel.getGreenfootImage());
        bg2PRCube = new ImageActor(bg2PRCubeModel.getGreenfootImage());
        
        bg3Title = new GreenfootImage("ui/titlescreen.png");
        bg3TitleBlur = BlurHelper.fastBlur(bg3Title, 0.001);
        bg3fade = new ImageActor("ui/fade.png");
        
        bg1Scuffed.setTransparency(0);
        bg1Engine.setTransparency(0);
        bg1PoweredByGreenfoot.setTransparency(0);
        bg2PolyRender.setTransparency(0);
        bg2PRCube.setTransparency(0);
        bg3fade.setTransparency(0);

        addObject(bg1PoweredByGreenfoot, centerX, centerY);
        addObject(bg1Engine, centerX, centerY);
        addObject(bg1Scuffed, centerX, centerY);
        addObject(bg2PolyRender, centerX+100, centerY);
        addObject(bg2PRCube, centerX-200, centerY);
        addObject(bg3fade, centerX, WORLD_HEIGHT);

        halfMoveScuffed = bg1Scuffed.getImage().getWidth() / 2;
        halfMoveEngine = bg1Engine.getImage().getWidth() / 2;

        blackOverlay = new ImageActor(WORLD_WIDTH, WORLD_HEIGHT);
        blackOverlay.setColor(new Color(0, 0, 0));
        blackOverlay.fill();
        blackOverlay.setTransparency(0);
        addObject(blackOverlay, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);

        // Prepare start button and label but do NOT add to world yet
        GreenfootImage[] buttonImages = new GreenfootImage[2];
        buttonImages[0] = new GreenfootImage("ui/button-sidebar.png");
        buttonImages[1] = new GreenfootImage("ui/button-sidebar-pressed.png");
        startButton = new Button(false, buttonImages, 200, 50); // example size

        startLabel = new Label("Start", 30);  // text "Start", font size 30
        startLabel.setFillColor(Color.WHITE);
        startLabel.setLineColor(Color.BLACK);

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
                    enterPhase3();
                }
                break;
                
            case 3:
                long delayElapsed = System.currentTimeMillis() - phaseStartTime;
                if (delayElapsed >= PHASE2_DELAY_MS) {
                    enterPhase4();
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
                    bg.setColor(new Color(0,0,0));
                    bg.fill();
                    setBackground(bg);
                    
                    bg1Scuffed.setTransparency(0);
                    bg1Engine.setTransparency(0);
                    bg1PoweredByGreenfoot.setTransparency(0);
                    blackOverlay.setTransparency(0);
                    enterPhase5();
                }
                break;
            case 5:
                long elapsed4 = System.currentTimeMillis() - phaseStartTime; 
                if (elapsed4 < 750) {
                    int alpha = (int) Math.round(
                        Utils.map(elapsed4, 0, 750, 0, 255)
                    );
                    
                    alpha = (int) Utils.clamp(alpha, 0, 255);
                    
                    bg2PolyRender.setTransparency(alpha);
                    bg2PRCube.setTransparency(alpha);
                } else {
                    bg2PolyRender.setTransparency(255);
                    bg2PRCube.setTransparency(255);
                    enterPhase6();
                }
                break;
                
            case 6:
                long delayElapsed2 = System.currentTimeMillis() - phaseStartTime;
                if (delayElapsed2 >= 1500) {
                    enterPhase7();
                }
                
                break;
            case 7:
                long elapsed5 = System.currentTimeMillis() - phaseStartTime; 
                if (elapsed5 < 600) {
                    int actorPosition = (int) Math.round(
                        Utils.map(elapsed5, 0, 600, bg2PRCube.getX(), centerX)
                    );
                    
                    int cubeRotation1 = (int) Math.round(
                        Utils.map(elapsed5, 0, 600, 0, 90)
                    );
                    int cubeRotation2 = (int) Math.round(
                        Utils.map(elapsed5, 0, 600, 0, 180)
                    );
                    int cubeRotation3 = (int) Math.round(
                        Utils.map(elapsed5, 0, 600, 0, 360)
                    );
                    
                    int cubePosition = (int) Math.round(
                        Utils.map(elapsed5, 0, 600, 500, 0)
                    );

                    bg2PRCubeModel.position(0,0,cubePosition);
                    bg2PRCubeModel.rotate(Math.toRadians(cubeRotation1),Math.toRadians(cubeRotation2),Math.toRadians(cubeRotation3));
                    bg2PRCubeModel.act();
                    bg2PRCube.setImage(bg2PRCubeModel.getGreenfootImage());
                    
                    bg2PRCube.setLocation(actorPosition,bg2PRCube.getY());
                } else {
                    enterPhase8();
                }
                
                if (elapsed5 < 650 && elapsed5 > 150) {
                    int overlayAlpha = (int) Math.round(
                        Utils.map(elapsed5, 0, 500, 0, 255)
                    );
                    
                    overlayAlpha = (int)Utils.clamp(overlayAlpha,0,255);
                    
                    blackOverlay.setTransparency(overlayAlpha);
                }
                break;
                
            case 8:
                phase = 9;
                break;
            case 9:
                phase = 10;
                break;
            case 10:
                phase = 11;
                break;
            case 11:
                long elapsed11 = System.currentTimeMillis() - phaseStartTime; 
                if (elapsed11 < 3000) {
                    blackOverlay.setTransparency(0);
                    double expo = Utils.map(elapsed11, 0, 3000, 0, 100);
                    double raw = Math.pow(1.05, expo);
                    double blurPower = Utils.map(raw, 0, 100, 0, 1);
                    blurPower = Utils.clamp(blurPower, 0.0, 1.0);
                    bg3TitleBlur = BlurHelper.fastBlur(bg3Title, blurPower);
                    setBackground(bg3TitleBlur);
                } else {
                    enterPhase12(); // transition to show button and wait for click
                }
                break;
            case 12:
                long delayElapsed3 = System.currentTimeMillis() - phaseStartTime;
                if (delayElapsed3 >= 1500) {
                    enterPhase13();
                }
                break;
            case 13:
                long elapsed13 = System.currentTimeMillis() - phaseStartTime; 
                if (elapsed13 < 500) {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed13, 0, 500, 0, 255)
                    );
                    
                    int progressPos = (int) Math.round(
                        Utils.map(elapsed13, 0, 500, WORLD_HEIGHT, WORLD_HEIGHT - bg3fade.getHeight()/2)
                    );
                    
                    progressAlpha = (int)Utils.clamp(progressAlpha,0,255);
                    progressPos = (int)Utils.clamp(progressPos,WORLD_HEIGHT - bg3fade.getHeight()/2,WORLD_HEIGHT);
                    
                    
                    bg3fade.setTransparency(progressAlpha);
                    bg3fade.setLocation(bg3fade.getX(),progressPos);
                    
                    
                } else {
                    bg3fade.setTransparency(255);
                    bg3fade.setLocation(bg3fade.getX(),WORLD_HEIGHT - bg3fade.getHeight()/2);
                    enterPhase14();
                }
                break;
            case 14:
                
                
                
                if (statusLabel == null) {
                    statusLabel = new Label("Connecting...", 24);
                    addObject(statusLabel, WORLD_WIDTH / 2, WORLD_HEIGHT - statusLabel.getImage().getHeight()/2);
                }
                
                if (!scuffedAPIConnectioninProgress)
                {
                    new Thread(() -> {
                        connectionResult = ScuffedAPI.getInstance().connect();
                    }).start();
                    scuffedAPIConnectioninProgress = true;
                    phaseStartTime = System.currentTimeMillis();
                }
                
                if (scuffedAPIConnectioninProgress)
                {
                    if (connectionResult != null)
                    {
                        long delayElapsed14 = System.currentTimeMillis() - phaseStartTime;
                        if (delayElapsed14 >= 500) {
                            statusLabel.setValue("ScuffedAPI: Connecting...");
                        }
                        else
                        {
                            if (connectionResult == true)
                            {
                                if (delayElapsed14 >= 1500) {
                                    statusLabel.setValue("ScuffedAPI: Leaderboard Connected!");
                                     if (delayElapsed14 >= 2500) {
                                        enterPhase15();
                                    }
                                }
                                
                            }
                            else
                            {
                                if (delayElapsed14 >= 1500) {
                                    statusLabel.setValue("ScuffedAPI: Failed to connect!");
                                    scuffedAPIConnectioninProgress = false;
                                }

                            }
                        }
                        
                        
                        
                    }
                }
    
                
                break;
            case 15:
                break;
            case 16:
                break;
            case 17:
                break;

            case 18:
                // Add start button and label only once
                if (startButton.getWorld() == null) {
                    addObject(startButton, centerX, centerY + 150);
                    addObject(startLabel, centerX, centerY + 150);
                    startButton.setActive(true);
                }

                startButton.act(); // update button visuals and input

                // Check if button was pressed
                if (startButton.isPressed()) {
                    removeObject(startButton);
                    removeObject(startLabel);
                    Greenfoot.setWorld(new GameWorld());
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
    
    private void enterPhase3() {
        phase = 3;
        phaseStartTime = System.currentTimeMillis();
    }

    private void enterPhase4() {
        phase = 4;
        phaseStartTime = System.currentTimeMillis();
        blackOverlay.setTransparency(0);
    }
    
    private void enterPhase5() {
        phase = 5;
        phaseStartTime = System.currentTimeMillis();
    }
    
    private void enterPhase6() {
        phase = 6;
        phaseStartTime = System.currentTimeMillis();
    }
    private void enterPhase7() {
        phase = 7;
        phaseStartTime = System.currentTimeMillis();
        blackOverlay.setColor(new Color(179,179,179));
        blackOverlay.fill();
    }
    
    private void enterPhase8() {
        phase = 8;
        phaseStartTime = System.currentTimeMillis();
        bg2PolyRenderModel.setScale(0);
        bg2PRCubeModel.setScale(0);
        bg2PolyRenderModel.act();
        bg2PRCubeModel.act();
        bg2PolyRender.setImage(bg2PolyRenderModel.getGreenfootImage());
        bg2PRCube.setImage(bg2PRCubeModel.getGreenfootImage());
    }
    
    private void enterPhase12() {
        phase = 12;
        phaseStartTime = System.currentTimeMillis();
    }
        
    private void enterPhase13() {
        phase = 13;
        phaseStartTime = System.currentTimeMillis();  
    }
    
    private void enterPhase14() {
        phase = 14;
        phaseStartTime = System.currentTimeMillis();  
    }
    
    private void enterPhase15() {
        phase = 15;
        phaseStartTime = System.currentTimeMillis();  
    }
    
    
}
