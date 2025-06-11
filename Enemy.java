import greenfoot.*;
import java.util.*;

public abstract class Enemy extends Actor {
    protected int speed;
    protected int health;
    private List<BurnEffect> burnEffects = new ArrayList<>();
    private GreenfootImage baseImage;
    private GreenfootImage burnedImage;
    private boolean isBurning = false;
    public GameWorld gw;
    public boolean isDead = false;
    private ParticleManager pm;
    private World world;
    private int worldWidth;
    private int moneyOnDeath = 10;
    private int originalSpeed;
    private static GreenfootImage fireOverlay;
    protected GreenfootSound movement;


    // Constructor for Enemy
    /**
     * Constructor for Enemy
     * 
     * @param speed Speed of the enemy
     * @param health Health of the enemy
     * @param money Money awarded upon death
     */
    public Enemy(int speed, int health, int money) {
        this.speed = speed;
        this.health = health;
        this.pm = ParticleManager.getInstance();
        this.moneyOnDeath = money;

        if (fireOverlay == null) {
            fireOverlay = new GreenfootImage("fire.png");
        }
    }

    @Override
    protected void addedToWorld(World w) {
        this.world = w;
        this.worldWidth = world.getWidth();
        gw = (GameWorld) getWorld();
    }

    protected void setBaseImage(GreenfootImage img) {
        baseImage = new GreenfootImage(img);
        burnedImage = new GreenfootImage(img);
        int x = (img.getWidth() - fireOverlay.getWidth()) / 2;
        int y = (img.getHeight() - fireOverlay.getHeight()) / 2 + 15;
        burnedImage.drawImage(fireOverlay, x, y);
        setImage(baseImage);
    }

    private void updateImage() {
        if (baseImage == null) return;
        if (isBurning) {
            setImage(new GreenfootImage(burnedImage));
        } else {
            setImage(new GreenfootImage(baseImage));
        }
    }

    public void applyBurn(BurnEffect burn) {
        burnEffects.add(burn);
        isBurning = true;
        updateImage();
    }

    private void updateBurns() {
        Iterator<BurnEffect> it = burnEffects.iterator();
        while (it.hasNext()) {
            BurnEffect burn = it.next();
            if (burn.updateAndApply(this)) {
                it.remove();
            }
        }
        boolean stillBurning = !burnEffects.isEmpty();
        if (isBurning != stillBurning) {
            isBurning = stillBurning;
            updateImage();
        }
    }

    private int totalCount = 0;

    public void act() {
        if (gw == null) return;

        updateBurns();

        if (isDead) {
            if (movement != null) {
                AudioManager.stopLoopingSFX(movement);
            }
            if (totalCount < 5 && !(boolean) PlayerPrefs.getData("PreformanceMode", false)) {
                totalCount++;
                pm.addParticle(getX(), getY(),
                        Greenfoot.getRandomNumber(360),
                        Greenfoot.getRandomNumber(5) + 2.5,
                        Color.RED);
            } else {
                gw.removeObject(this);
            }
            return;
        }

        if (movement != null && !movement.isPlaying() && gw.getStatus() == GameWorld.Status.RUNNING) {
            AudioManager.playLoopingSFX(movement);
        }

        if (gw.getStatus() == GameWorld.Status.RUNNING) {
            move(speed);
        }

        updateImage();

        if (getX() >= worldWidth - 160) {
            gw.loseLife(getLifeDamage());
            if (movement != null) {
                AudioManager.stopLoopingSFX(movement);
            }
            gw.removeObject(this);
            isDead = true;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }

    public int getLifeDamage() {
        return 1;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0 && gw != null) {
            gw.addMoney(moneyOnDeath);
            isDead = true;
        }
    }

    public void applySlow(int amount, int duration) {
        if (originalSpeed == 0) originalSpeed = speed;
        speed = Math.max(1, originalSpeed - amount);
    }
}
