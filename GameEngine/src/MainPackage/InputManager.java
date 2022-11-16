// Blake Freer
// Jun 5, 2020
// Stores the states of keys to be used by other scripts
package MainPackage;

import Engine.GameMath.Vector2;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class InputManager {

    // Default axes
    /**
     * 1D axis<br> {@code Positive}: D<br> {@code Negative}: A
     */
    public static final Axis1D HORIZONTAL = new Axis1D(KeyEvent.VK_A, KeyEvent.VK_D);
    /**
     * 1D axis<br> {@code Positive}: W<br> {@code Negative}: S
     */
    public static final Axis1D VERTICAL = new Axis1D(KeyEvent.VK_S, KeyEvent.VK_W);

    /**
     * 1D axis<br> {@code Positive}: RIGHT<br> {@code Negative}: LEFT
     */
    public static final Axis1D HORIZONTAL2 = new Axis1D(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
    /**
     * 1D axis<br> {@code Positive}: UP<br> {@code Negative}: DOWN
     */
    public static final Axis1D VERTICAL2 = new Axis1D(KeyEvent.VK_DOWN, KeyEvent.VK_UP);

    /**
     * 1D axis<br> {@code Positive}: D<br> {@code Negative}: A
     */
    public static final Axis2D WASD = new Axis2D(KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_W);
    /**
     * 1D axis<br> {@code Positive}: D<br> {@code Negative}: A
     */
    public static final Axis2D ARROWS = new Axis2D(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_UP);

    private Map<Integer, Boolean> keyStates;

    public InputManager() {
        // Create the map
        keyStates = new HashMap<>();
    }

    public void NewKeyEvent(KeyEvent event) {
        if (event.getID() == KeyEvent.KEY_TYPED) {
            // Ignore keyTyped events
            return;
        }
        // Modify the keyStates map with the new key event information
        keyStates.put(event.getKeyCode(), event.getID() == KeyEvent.KEY_PRESSED);
    }

    public boolean GetKeyDown(int keyCode) {
        // Return whether or not a key is pressed
        try {
            return keyStates.get(keyCode);
        } catch (NullPointerException err) {
            keyStates.put(keyCode, false);
            return false;
        }
    }

    // Get an axis from -1 -> 0 -> 1 from keyCodes or predefined Axis1D
    public double GetAxis(int keyCodeNegative, int keyCodePositive) {
        // Convert 2 keys into an axis
        boolean neg = GetKeyDown(keyCodeNegative);
        boolean pos = GetKeyDown(keyCodePositive);
        if (!(neg ^ pos)) {
            // If both or neither (XNOR) are pressed, return 0
            return 0;
        }
        // Otherwise, only one is pressed. If that is neg, return -1, otherwise 1
        return neg ? -1 : 1;
    }
    public double GetAxis(Axis1D axis) {
        // Get info for a predefined axis
        return GetAxis(axis.negative, axis.positive);
    }

    public Vector2 GetAxis2D(Axis2D axis) {
        // Return a Vector2 for the composite input from a 2D axis
        // Output is normalized (eg. cannot move faster by going on 45 degree angle)
        Vector2 vec = new Vector2(0, 0);
        vec.x = GetAxis(axis.xAxis);
        vec.y = GetAxis(axis.yAxis);
        return vec.normalized();
    }

    public static class Axis1D {

        // Hold two integers to represent a 1D axis
        public final int negative;
        public final int positive;

        public Axis1D(int keyCodeNegative, int keyCodePositive) {
            // Create a new axis
            this.negative = keyCodeNegative;
            this.positive = keyCodePositive;
        }
    }

    public static class Axis2D {

        // Holds 2 axis, x and y
        public final Axis1D xAxis;
        public final Axis1D yAxis;

        public Axis2D(Axis1D x, Axis1D y) {
            // Constructor from two predefined axis
            xAxis = x;
            yAxis = y;
        }

        public Axis2D(int x_neg, int x_pos, int y_neg, int y_pos) {
            // Constructor from 4 keys
            xAxis = new Axis1D(x_neg, x_pos);
            yAxis = new Axis1D(y_neg, y_pos);
        }
    }

}
