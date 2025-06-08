import greenfoot.Actor;

/**
 * Write a description of class Base here.
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class BurnEffect extends Actor{
    private int damagePerTick;
    private int ticksRemaining;
    private int tickInterval;
    private int tickCounter;

    public BurnEffect(int damagePerTick, int totalTicks, int tickInterval) {
        this.damagePerTick = damagePerTick;
        this.ticksRemaining = totalTicks;
        this.tickInterval = tickInterval;
        this.tickCounter = 0;
    }

    public boolean updateAndApply(Enemy enemy) {
        tickCounter++;
        if (tickCounter >= tickInterval && ticksRemaining > 0) {
            enemy.takeDamage(damagePerTick);
            tickCounter = 0;
            ticksRemaining--;
        }
        return ticksRemaining <= 0; // return true when done
    }
}
