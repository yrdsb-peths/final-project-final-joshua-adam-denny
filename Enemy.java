import greenfoot.*;
import java.util.*;

public abstract class Enemy extends Actor {
    protected int speed;
    protected int health;
    private List<BurnEffect> burnEffects = new ArrayList<>();
    private GreenfootImage baseImage;
    private boolean isBurning = false;

    private boolean isDead = false;
    private ParticleManager pm;

    private int moneyOnDeath = 10;
    public Enemy(int speed, int health, int money) {
        this.speed = speed;
        this.health = health;
        this.pm = ParticleManager.getInstance();
        this.moneyOnDeath = money;
    }

    protected void setBaseImage(GreenfootImage img) {
        baseImage = img;
        setImage(new GreenfootImage(baseImage));
    }

    private void updateImage() {
        if (baseImage == null) return;
        GreenfootImage img = new GreenfootImage(baseImage);
        if (isBurning) {
            img.setColor(new Color(255, 0, 0, 100));
            img.fill();
        }
        setImage(img);
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
        updateBurns();
        if (!isDead)
        {
            move(speed);
        }
        updateImage();
        World world = getWorld();
        if (world != null && getX() >= world.getWidth() - 160) {
            ((GameWorld) world).loseLife(getLifeDamage());
            world.removeObject(this);
        }
        
        if (isDead)
        {
            if (totalCount < 5)
            {
                totalCount++;
                pm.addParticle(
                    getX(), 
                    getY(), 
                    Greenfoot.getRandomNumber(360) - 135.0,
                    Greenfoot.getRandomNumber(5) + 2.5, 
                    Color.RED 
                );
                
            } else 
            {
                getWorld().removeObject(this);
            }
        }
    }

    public int getSpeed() { return speed; }
    public int getHealth() { return health; }
    public int getLifeDamage() { return 1; }
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0 && getWorld() != null) {
            ((GameWorld) getWorld()).addMoney(moneyOnDeath);
            isDead = true;
            
        }
    }
}
