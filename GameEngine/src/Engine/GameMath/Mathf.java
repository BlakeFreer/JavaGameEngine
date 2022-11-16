// Blake Freer
// May 26, 2020
// Additional or modified math functions
package Engine.GameMath;

import GameObjects.Components.Transform;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A collection of modified or additional mathematical functions and constants.
 *
 * @author bkfre
 */
public class Mathf {

    // Constants
    /**
     * PI = 3.1415...
     */
    final static double PI = Math.PI;

    /**
     * Conversion factor for converting from degrees to radians.
     */
    final static double Deg2Rad = Math.PI / 180;

    /**
     * Conversion factor for converting from radians to degrees.
     */
    final static double Rad2Deg = 180 / Math.PI;

    /**
     * A small value used in determining if two values are approximately equal.
     */
    final static double Epsilon = Math.pow(10, -5);

    /**
     * Negative Infinity.
     */
    final static double NegativeInfinity = Double.MIN_VALUE;

    /**
     * Positive Infinity.
     */
    final static double Infinity = Double.MAX_VALUE;

    /**
     * An alternate equality test that determines if two values are within a
     * very small difference, epsilon.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean Approximately(double a, double b) {
        // Returns if two values are within epsilon
        return Math.abs(a - b) < Epsilon;
    }
    /**
     * Round up a value to the nearest integer.
     *
     * @param value
     * @return
     */
    public static int Ciel(double value) {
        return (int) Math.ceil(value);
    }
    /**
     * Returns a value, clamped between a minimum and maximum value.
     *
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static double Clamp(double value, double min, double max) {
        // Clamps a value into a given range
        return Math.max(min, Math.min(max, value));
    }
    /**
     * Returns a value, clamped between 0 and 1
     *
     * @param value
     * @return
     */
    public static double Clamp01(double value) {
        // Clamp between 0 and 1
        return Mathf.Clamp(value, 0, 1);
    }
    /**
     * Round a value down to the nearest integer.
     *
     * @param value
     * @return
     */
    public static int Floor(double value) {
        return (int) Math.floor(value);
    }
    /**
     * Determine if {@code value} is within the inclusive range [{@code min},
     * {@code max}].
     *
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static boolean InRange(double value, double min, double max) {
        // Deterine if a value is within a range (including edges)
        return value >= min && value <= max;
    }

    /**
     * Return a random integer in a given range.
     *
     * @param min Minimum of range (inclusive)
     * @param max Maximum of range (exclusive)
     * @return
     */
    public static int RandInt(int min, int max) {
        if (max == min) {
            // If range is one value, return that value.
            return min;
        }
        if (max < min) {
            // Check that maximum is greater than minimum.
            throw new IllegalArgumentException("RandInt: Maximum cannot be less than minimum.");
        }
        
        // Return a random integer in the range.
        return ThreadLocalRandom.current().nextInt(min, max);
        
    }

    /**
     * Returns sign of value:<br>1 if > 0<br>-1 if < 0<br>0 if = 0.
     *
     * @param value
     * @return
     */
    public static double Sign(double value) {
        if (value > 0) {
            return 1;
        }
        if (value < 0) {
            return -1;
        }
        return 0;
    }

    /**
     * Dampen the movement of a double towards a value.
     *
     * @param current Current value.
     * @param target Value to approach.
     * @param refCurrent Reference to a Vector2
     * @param smoothTime Time taken to reach new value.
     * @return
     */
    public static double SmoothDamp(double current, double target, Vector2 refCurrent, double smoothTime) {
        return SmoothDamp(current, target, refCurrent, smoothTime, Mathf.Infinity, Transform.gameData.deltaTime);
    }
    private static double SmoothDamp(double current, double target, Vector2 refCurrent, double smoothTime, double maxSpeed, double deltaTime) {
        smoothTime = Math.max(Mathf.Epsilon, smoothTime);

        double num1 = 2f / smoothTime;
        double num2 = num1 * deltaTime;
        double num3 = 1 / (1 + num2 + 0.48 * num2 * num2 + 0.235 * num2 * num2 * num2);
        double num4 = current - target;
        double num5 = target;
        double num6 = maxSpeed * smoothTime;
        num4 = Mathf.Clamp(num4, -num6, num6);
        target = current - num4;
        double num7 = (refCurrent.x + num1 * num4) * deltaTime;
        refCurrent.x = (refCurrent.x - num1 * num7) * num3;
        double num8 = target + (num4 + num7) * num3;

        if (num5 - current > 0 == num8 > num5) {
            num8 = num5;
            refCurrent.x = (num8 - num5) / deltaTime;
        }
        return num8;

    }

    // Other coins
    /**
     * Determine if an array contains an element.
     *
     * @param array Array to search.
     * @param strToSearch String to search for.
     * @return
     */
    public static boolean ArrayContains(String[] array, String strToSearch) {
        for (String str : array) {
            if (str.equals(strToSearch)) {
                // If the element is the same as the desired element, return true.
                return true;
            }
        }
        // If true wasn't returned, return false.
        return false;
    }

}
