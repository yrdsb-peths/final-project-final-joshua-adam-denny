import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Init world real
 * 
 * @author Denny Ung
 * @version Version 1.4.4 (June 4, 2025)
 */
public class _InitWorld extends World {
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
    private List<Long> elapsed = new ArrayList<>();

    private int newImageStartX, newImageStartY;
    private int newImageTargetY;

    private Button startButton;
    private Label startLabel;

    
    private Label statusLabel;
    private Boolean connectionResult = null; 
    private int scuffedAPIAttempts = 0;
    private boolean scuffedAPIConnectioninProgress = false;
    
    public _InitWorld() {
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

        phase = 0;
        phaseStartTime = System.currentTimeMillis();
    }

    @Override
    public void act() {
        long now = System.currentTimeMillis();
        elapsed.add(0, now - phaseStartTime);
        
        switch (phase) {
            case 0:
                if (elapsed.get(phase) < FADE_DURATION_MS) {
                    int alpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, FADE_DURATION_MS, 0, 255)
                    );
                    bg1Scuffed.setTransparency(alpha);
                } else {
                    bg1Scuffed.setTransparency(255);
                    bg1Engine.setTransparency(255);
                    enterPhase(1);
                }
                break;

            case 1:
                if (elapsed.get(0) < PHASE1_DURATION_MS) {
                    double t = (double) elapsed.get(0) / PHASE1_DURATION_MS;
                    double ease = (1 - Math.cos(Math.PI * t)) * 0.5;

                    double newXScuffed = centerX - halfMoveScuffed * ease;
                    bg1Scuffed.setLocation((int) Math.round(newXScuffed), centerY);

                    double newXEngine = centerX + halfMoveEngine * ease;
                    bg1Engine.setLocation((int) Math.round(newXEngine), centerY);
                } else {
                    bg1Scuffed.setLocation(centerX - halfMoveScuffed, centerY);
                    bg1Engine.setLocation(centerX + halfMoveEngine, centerY);
                    int engineX = bg1Engine.getX();
                    int engineY = bg1Engine.getY();
                    newImageStartX = engineX;
                    newImageStartY = engineY;
                    bg1PoweredByGreenfoot.setLocation(engineX, engineY);
                    bg1PoweredByGreenfoot.setTransparency(255);
            
                    int engineHalfH = bg1Engine.getImage().getHeight() / 2;
                    int newHalfH = bg1PoweredByGreenfoot.getImage().getHeight() / 2;
                    newImageTargetY = (engineY + engineHalfH + PADDING) + newHalfH;
                    enterPhase(2);
                }
                break;

            case 2:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < PHASE2_DURATION_MS) {
                    double t = (double) elapsed.get(phase) / PHASE2_DURATION_MS;
                    double ease = (1 - Math.cos(Math.PI * t)) * 0.5;

                    double newY = newImageStartY + (newImageTargetY - newImageStartY) * ease;
                    bg1PoweredByGreenfoot.setLocation(newImageStartX, (int) Math.round(newY));
                } else {
                    bg1PoweredByGreenfoot.setLocation(newImageStartX, newImageTargetY);
                    enterPhase(3);
                }
                break;
                
            case 3:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) >= PHASE2_DELAY_MS) {
                    blackOverlay.setTransparency(0);
                    enterPhase(4);
                }
                break;

            case 4:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < PHASE3_DURATION_MS) {
                    int alpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, PHASE3_DURATION_MS, 0, 255)
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
                    enterPhase(5);
                }
                break;
            case 5:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 750) {
                    int alpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 750, 0, 255)
                    );
                    
                    alpha = (int) Utils.clamp(alpha, 0, 255);
                    
                    bg2PolyRender.setTransparency(alpha);
                    bg2PRCube.setTransparency(alpha);
                } else {
                    bg2PolyRender.setTransparency(255);
                    bg2PRCube.setTransparency(255);
                    enterPhase(6);
                }
                break;
                
            case 6:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) >= 1500) {
                    blackOverlay.setColor(new Color(179,179,179));
                    blackOverlay.fill();
                    enterPhase(7);
                }
                
                break;
            case 7:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 600) {
                    int actorPosition = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 600, bg2PRCube.getX(), centerX)
                    );
                    
                    int cubeRotation1 = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 600, 35, 90)
                    );
                    int cubeRotation2 = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 600, 45, 180)
                    );
                    int cubeRotation3 = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 600, 0, 360)
                    );
                    
                    int cubePosition = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 600, 500, 0)
                    );

                    bg2PRCubeModel.position(0,0,cubePosition);
                    bg2PRCubeModel.rotate(Math.toRadians(cubeRotation1),Math.toRadians(cubeRotation2),Math.toRadians(cubeRotation3));
                    bg2PRCubeModel.act();
                    bg2PRCube.setImage(bg2PRCubeModel.getGreenfootImage());
                    
                    bg2PRCube.setLocation(actorPosition,bg2PRCube.getY());
                } else {
                    bg2PolyRenderModel.setScale(0);
                    bg2PRCubeModel.setScale(0);
                    bg2PolyRenderModel.act();
                    bg2PRCubeModel.act();
                    bg2PolyRender.setImage(bg2PolyRenderModel.getGreenfootImage());
                    bg2PRCube.setImage(bg2PRCubeModel.getGreenfootImage());
                    enterPhase(8);
                }
                
                if (elapsed.get(phase) < 650 && elapsed.get(phase) > 150) {
                    int overlayAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 500, 0, 255)
                    );
            
                    overlayAlpha = (int)Utils.clamp(overlayAlpha,0,255);
                    blackOverlay.setTransparency(overlayAlpha);
                }
                break;
                
            case 8:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) >= 0) {
                    enterPhase(9);
                }
                break;
            case 9:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) >= 0) {
                    enterPhase(10);
                }
                break;
            case 10:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) >= 0) {
                    enterPhase(11);
                }
                break;
            case 11:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 3000) {
                    blackOverlay.setTransparency(0);
                    double expo = Utils.map(elapsed.get(phase), 0, 3000, 0, 100);
                    double raw = Math.pow(1.05, expo);
                    double blurPower = Utils.map(raw, 0, 100, 0, 1);
                    blurPower = Utils.clamp(blurPower, 0.0, 1.0);
                    bg3TitleBlur = BlurHelper.fastBlur(bg3Title, blurPower);
                    setBackground(bg3TitleBlur);
                } else {
                    enterPhase(12);
                }
                break;
            case 12:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) >= 1500) {
                    enterPhase(13);
                }
                break;
            case 13:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 500) {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 500, 0, 255)
                    );
                    
                    int progressPos = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 500, WORLD_HEIGHT, WORLD_HEIGHT - bg3fade.getHeight()/2)
                    );
                    
                    progressAlpha = (int)Utils.clamp(progressAlpha,0,255);
                    progressPos = (int)Utils.clamp(progressPos,WORLD_HEIGHT - bg3fade.getHeight()/2,WORLD_HEIGHT);
                    
                    
                    bg3fade.setTransparency(progressAlpha);
                    bg3fade.setLocation(bg3fade.getX(),progressPos);
                    
                    
                } else {
                    bg3fade.setTransparency(255);
                    bg3fade.setLocation(bg3fade.getX(),WORLD_HEIGHT - bg3fade.getHeight()/2);
                    enterPhase(14);
                }
                break;
            case 14:
                if (statusLabel == null) {
                    statusLabel = new Label("ScuffedAPI: Connecting...", 24);
                    addObject(statusLabel, WORLD_WIDTH / 2, WORLD_HEIGHT - statusLabel.getImage().getHeight()/2 - 20);
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
                        elapsed.add(phase, now - phaseStartTime);
                        if (elapsed.get(phase) <= 500) {
                            statusLabel.setValue("ScuffedAPI: Connecting...");
                        }
                        else
                        {
                            if (connectionResult == true)
                            {
                                statusLabel.setValue("ScuffedAPI: Leaderboard Connected!");
                                if (elapsed.get(phase) >= 1500) {
                                    enterPhase(15);
                                }
                                
                            }
                            else
                            {
                                statusLabel.setValue("ScuffedAPI: Failed to connect! Attempt: " + Integer.toString(scuffedAPIAttempts + 1));
                    
                                if (elapsed.get(phase) >= 1500) {
                                    scuffedAPIConnectioninProgress = false;
                                    scuffedAPIAttempts++;
                                }

                            }
                        }
                    }
                }
                
                if (scuffedAPIAttempts >= 3)
                {
                    statusLabel.setValue("ScuffedAPI: Failed to connect! Continuing...");
                    connectionResult = false;
                    enterPhase(15);
                }
                break;
            case 15:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) >= 1500) {
                    blackOverlay.setColor(new Color(0,0,0));
                    blackOverlay.fill();
                    enterPhase(16);
                }
                break;
            case 16:
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 500) {
                    int alpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 500, 0, 255)
                    );
                    alpha = (int) Utils.clamp(alpha, 0, 255);
                    blackOverlay.setTransparency(alpha);
                } else {
                    Greenfoot.setWorld(new MainMenu());
                }
                break;
            default:
                break;
        }
    }
    
    private void enterPhase(int phaseInt)
    {
        this.phase = phaseInt;
        phaseStartTime = System.currentTimeMillis(); 
    }
    
    
}
