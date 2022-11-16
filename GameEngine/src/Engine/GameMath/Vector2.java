// Blake Freer
// May 26, 2020
// Vector class for 2d vectors and operations
// Based off of Unity's Vector2 (originally in C#)
package Engine.GameMath;

import _Unused.Vector3;

/**
 * Mathematical representation and operations on 2-dimensional geometric vectors.
 * @author Blake Freer
 */
public class Vector2 {

    // Instance variables
    /**
     * Horizontal component of the Vector2.
     */
    public double x;
    /**
     * Vertical component of the Vector2.
     */
    public double y;

    // Static properties
    // These used to be variables, but this led to multiple references to the same Vector2.
    // Now they are replaced with methods that return a new object.
    /**
     * Short form for a left-pointing unit vector [-1, 0].
     * @return 
     */
    public static Vector2 left(){
        return new Vector2(-1.0, 0.0);
    }
    
    /**
     * Short form for a right-pointing unit vector [1, 0].
     * @return 
     */
    public static Vector2 right(){
        return new Vector2(1.0, 0.0);
    }
    
    /**
     * Short form for a upwards-pointing unit vector [0, 1].
     * @return 
     */
    public static Vector2 up(){
        return new Vector2(0.0, 1.0);
    }
    
    /**
     * Short form for a downwards-pointing unit vector [0, -1].
     * @return 
     */
    public static Vector2 down(){
        return new Vector2(0.0, -1.0);
    }
    
    /**
     * Short form for the zero vector [0, 0].
     * @return 
     */
    public static Vector2 zero(){
        return new Vector2(0.0, 0.0);
    }
    
    /**
     * Short form for a vector that is left and below all other vectors [-inf, -inf].
     * @return 
     */
    public static Vector2 negativeInfinity(){
        return new Vector2(Mathf.NegativeInfinity, Mathf.NegativeInfinity);
    }
    
    /**
     * Short form for a vector that is right and above all other vectors [inf, inf].
     * @return 
     */
    public static Vector2 positiveInfinity(){
        return new Vector2(Mathf.Infinity, Mathf.Infinity);
    }

    // Constructors
    /**
     * Construct a Vector2 with two components.
     * @param x
     * @param y 
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Construct a Vector2 identical to another.
     * @param v 
     */
    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    /**
     * Constructs a Vector2 with the {@code x} and {@code y} components of a Vector3.
     * @param v 
     */
    public Vector2(Vector3 v) {
        this.x = v.x;
        this.y = v.y;
    }

    // Properties
    /**
     * Returns the length of the Vector2.
     * @return 
     */
    public double magnitude() {
        return Math.hypot(x, y);
    }
    
    /**
     * Returns the colinear unit Vector2. Does not modify the vector.
     * @return 
     */
    public Vector2 normalized() {
        if (this.sqrMagnitude() == 0) {
            // Ensure the magnitude isn't 0 to prevent division by zero error
            return Vector2.zero();
        }
        return Vector2.Divide(this, magnitude());
    }
    
    /**
     * Returns the square of the magnitude to save a square-root call.
     * @return 
     */
    public double sqrMagnitude() {
        return x * x + y * y;
    }

    // Public Methods
    
    /**
     * Vectors are equal iff components are equal.
     * @param v
     * @return 
    */
    public boolean Equals(Vector2 v) {
        return x == v.x && y == v.y;
    }
    
    /**
     * Modify the vector to make its length 1.0.
     */
    public void Normalize() {
        double m = magnitude();
        if (m == 0) {
            return; // Avoid division by zero
        }
        x /= m;
        y /= m;
    }
    
    /**
     * Reset the components of the Vector2.
     * @param newX
     * @param newY 
     */
    public void Set(double newX, double newY) {
        x = newX;
        y = newY;
    }
    
    /**
     * Returns a nicely formatted textual representation of the Vector2.
     * @return 
     */
    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
    
    /**
     * Returns an array of the vector's components.
     * @return 
     */
    public double[] toArray() {
        // Returns an array of the components
        return new double[]{x, y};
    }

    /**
     * Modify the Vector2 by adding another Vector2 {@code v} to it.
     * @param v 
     */
    public void Add(Vector2 v) {
        x += v.x;
        y += v.y;
    }
    
    /**
     * Modify the Vector2 by subtracting another Vector2 {@code v} from it.
     * @param v 
     */
    public void Subtract(Vector2 v) {
        x -= v.x;
        y -= v.y;
    }
    
    /**
     * Modify the Vector2 by scaling it by some {@code scalar}.
     * @param scalar 
     */
    public void Scale(double scalar) {
        x *= scalar;
        y *= scalar;
    }
    
    /**
     * Modify the Vector2 by dividing its length by {@code divisor}. (Same as scaling by 1/{@code divisor}).
     * @param divisor 
     */
    public void Divide(double divisor) {
        if(divisor == 0){
            // Check for division by zero.
            System.err.println("Divisor cannot be 0");
            return;
        }
        x /= divisor;
        y /= divisor;
    }

    // Static Methods
    
    /**
     * Calculates and returns the angle between two vectors.
     * @param from
     * @param to
     * @return 
     */
    public static double Angle(Vector2 from, Vector2 to) {
        // Angle between two vectors, never greater than 180
        return Math.acos(Vector2.Dot(from, to) / (from.magnitude() * to.magnitude()));
    }
    
    /**
     * Shortens a vector if it is longer than {@code maxLength}.
     * @param vector
     * @param maxLength
     * @return 
     */
    public static Vector2 ClampMagnitude(Vector2 vector, double maxLength) {
        // Return a copy of a vector with a magnitude clamped to maxLength
        Vector2 newVector = new Vector2(vector);
        if (vector.sqrMagnitude() > maxLength * maxLength) {
            newVector.Scale(maxLength / vector.magnitude());
        }
        return newVector;
    }
    
    /**
     * Returns the distance between two Vector2s (each representing a point).
     * @param p1
     * @param p2
     * @return 
     */
    public static double Distance(Vector2 p1, Vector2 p2){
        // Return the distance between two Vector2s (representing points)
        return Vector2.Subtract(p1, p2).magnitude();
    }
    
    /**
     * Returns the dot product of two Vector2s.
     * @param v1
     * @param v2
     * @return 
     */
    public static double Dot(Vector2 v1, Vector2 v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }
    
    /**
     * Linearly interpolates between {@code a} and {@code b} by {@code t}.<br>
     * Returns {@code a} if {@code t = 0}<br>
     * Returns {@code b} if {@code t = 1}<br>
     * {@code t} is clamped to [0,1]
     * @param a
     * @param b
     * @param t
     * @return
     */
    public static Vector2 Lerp(Vector2 a, Vector2 b, double t) {
        t = Mathf.Clamp01(t);
        Vector2 offset = Vector2.Subtract(b, a);
        offset.Scale(t);
        return Vector2.Add(a, offset);
    }
    
    /**
     * Linearly interpolates between {@code a} and {@code b} by {@code t}.<br>
     * Returns {@code a} if {@code t = 0}<br>
     * Returns {@code b} if {@code t = 1}<br>
     * {@code t} is not clamped
     * @param a
     * @param b
     * @param t
     * @return
     */
    public static Vector2 LerpUnclamped(Vector2 a, Vector2 b, double t) {
        // Interpolates or extrapolates between 'a' and 'b' by 't'
        Vector2 offset = Vector2.Subtract(b, a);
        offset.Scale(t);
        return Vector2.Add(a, offset);
    }
    
    /**
     * Returns the square of the distance between two Vector2s (each representing a point). Saves from calling square root.
     * @param p1
     * @param p2
     * @return 
     */
    public static double SqrDistance(Vector2 p1, Vector2 p2){
        // Return the sqr distance between 2 Vector2s, represting points
        return Vector2.Subtract(p1, p2).sqrMagnitude();
    }
    
    // Basic math operators
    
    /**
     * Adds two Vector2's and returns the sum. Does not modify either argument.
     * @param v1
     * @param v2
     * @return 
     */
    public static Vector2 Add(Vector2 v1, Vector2 v2) {
        Vector2 sum = new Vector2(v1);
        sum.Add(v2);
        return sum;
    }
    
    /**
     * Subtracts {@code v2} from {@code v1} and returns the difference. Does not modify either argument.
     * @param v1
     * @param v2
     * @return 
     */
    public static Vector2 Subtract(Vector2 v1, Vector2 v2) {
        // Subtract v2 from v1
        Vector2 difference = new Vector2(v1);
        difference.Subtract(v2);
        return difference;
    }
    
    /**
     * Returns {@code v} scaled by {@code scalar}. Does not modify the inputs.
     * @param v
     * @param scalar
     * @return 
     */
    public static Vector2 Scale(Vector2 v, double scalar) {
        // Scale a vector by a scalar
        Vector2 scaled = new Vector2(v);
        scaled.Scale(scalar);
        return scaled;
    }
    
    /** 
     * Returns {@code v} scaled by 1/{@code divisor}. Does not modify the inputs.
     * @param v
     * @param divisor
     * @return 
     */
    public static Vector2 Divide(Vector2 v, double divisor) {
        // Scale down a vector and return the quotient
        Vector2 quotient = new Vector2(v);
        quotient.Divide(divisor);
        return quotient;
    }

}
