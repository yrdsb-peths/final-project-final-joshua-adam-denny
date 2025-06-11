import greenfoot.Actor;

/**
 * BurnEffect Class is an effect that applies damage over time to an enemy.
 * 
 * @author Joshua Stevens
 * @version Version 1.0
 */
public class BurnEffect extends Actor{
    private int damagePerTick;
    private int ticksRemaining;
    private int tickInterval;
    private int tickCounter;


    /**
     * Constructor for BurnEffect.
     * Initializes the burn effect with damage per tick, total ticks, and tick interval.
     * 
     * @param damagePerTick The amount of damage dealt per tick.
     * @param totalTicks The total number of ticks the burn effect will last.
     * @param tickInterval The interval in ticks between each damage application.
     */
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
