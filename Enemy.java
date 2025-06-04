import greenfoot.*;
import java.util.*;

public abstract class Enemy extends Actor {
    protected int speed;
    protected int health;
    private List<BurnEffect> burnEffects = new ArrayList<>();
    private GreenfootImage baseImage;
    private GreenfootImage burnedImage;
    private boolean isBurning = false;

    public boolean isDead = false;
    private ParticleManager pm;
    private World world;
    private int worldWidth;
    private int worldHeight;
    private int moneyOnDeath = 10;
    public Enemy(int speed, int health, int money) {
        this.speed = speed;
        this.health = health;
        this.pm = ParticleManager.getInstance();
        this.moneyOnDeath = money;
    }
    
    @Override
    protected void addedToWorld(World w)
    {
        this.world = w;
        this.worldWidth = world.getWidth();
        this.worldHeight = world.getHeight();
    }

    protected void setBaseImage(GreenfootImage img) {
        baseImage = new GreenfootImage(img);
        burnedImage = new GreenfootImage(img);
        burnedImage.setColor(new Color(255, 0, 0, 100));
        burnedImage.fill();
        
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
        updateBurns();
        if (!isDead)
        {
            move(speed);
        }
        updateImage();
        if (world != null && getX() >= worldWidth - 160) {
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
