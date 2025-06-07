import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Slider extends Actor {
    private final int WIDTH = 120;
    private final int HEIGHT = 20;
    private final int KNOB_SIZE = 20;

    private int value;  // 0 to 100
    private boolean dragging = false;

    private GreenfootImage baseImage;  // slider background
    private GreenfootImage knobImage;

    public Slider(int maxValue, int initialValue) {
        // Note: maxValue is unused here but could be used to scale differently if you want.
        value = clamp(initialValue, 0, 100);

        // Create background image (slider track)
        baseImage = new GreenfootImage(WIDTH, HEIGHT);
        baseImage.setColor(greenfoot.Color.LIGHT_GRAY);
        baseImage.fillRect(0, HEIGHT / 4, WIDTH, HEIGHT / 2);

        // Create knob image (circle)
        knobImage = new GreenfootImage(KNOB_SIZE, KNOB_SIZE);
        knobImage.setColor(greenfoot.Color.DARK_GRAY);
        knobImage.fillOval(0, 0, KNOB_SIZE, KNOB_SIZE);

        redraw();
    }

    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();

        if (mouse != null) {
            int mouseX = mouse.getX();
            int mouseY = mouse.getY();

            int sliderX = getX() - WIDTH / 2;
            int sliderY = getY() - HEIGHT / 2;

            int knobPosX = sliderX + valueToPosition(value);

            // Start dragging if mouse pressed on knob
            if (Greenfoot.mousePressed(this)) {
                // Check if mouse is inside knob circle
                int knobCenterX = knobPosX + KNOB_SIZE / 2;
                int knobCenterY = getY();
                int dx = mouseX - knobCenterX;
                int dy = mouseY - knobCenterY;
                if (dx * dx + dy * dy <= (KNOB_SIZE / 2) * (KNOB_SIZE / 2)) {
                    dragging = true;
                }
            }

            // Dragging updates value
            if (dragging && Greenfoot.mouseDragged(this)) {
                int relativeX = mouseX - sliderX - KNOB_SIZE / 2;
                setValue(positionToValue(relativeX));
            }

            // Stop dragging when mouse released
            if (dragging && Greenfoot.mouseClicked(null)) {
                dragging = false;
            }
        }
    }

    private void redraw() {
        GreenfootImage image = new GreenfootImage(baseImage);
        int knobX = valueToPosition(value);
        int knobY = (HEIGHT - KNOB_SIZE) / 2;
        image.drawImage(knobImage, knobX, knobY);
        setImage(image);
    }

    private int valueToPosition(int val) {
        return (int) ((val / 100.0) * (WIDTH - KNOB_SIZE));
    }

    private int positionToValue(int pos) {
        pos = clamp(pos, 0, WIDTH - KNOB_SIZE);
        return (int) ((pos / (double)(WIDTH - KNOB_SIZE)) * 100);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int v) {
        int newValue = clamp(v, 0, 100);
        if (newValue != value) {
            value = newValue;
            redraw();
        }
    }

    private int clamp(int val, int min, int max) {
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }
}
