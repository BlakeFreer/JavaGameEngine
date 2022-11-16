// Blake Freer
// May 26, 2020
// Class that holds a 2-dimensional vector
package _Unused;

public class Vector2_v1 {

    // Allow modifications to the variables
    public double x;
    public double y;

    // Constructors
    public Vector2_v1() {
        // Zero vector
        this.x = 0;
        this.y = 0;
    }
    public Vector2_v1(double x, double y) {
        // Construct a Vector2 from components
        this.x = x;
        this.y = y;
    }
    public Vector2_v1(Vector2_v1 v) {
        // Construct a Vector2 from another Vector2
        this.x = v.x;
        this.y = v.y;
    }

    
    // Information
    public double mag() {
        // Returns length of Vector2
        return Math.hypot(x, y);
    }
    public double sqmag() {
        // Returns square of magnitude
        return x * x + y * y;
    }
    public double angle(){
        // Returns the angle of the vector CCW from standard position in radians
        double a = Vector2_v1.angleBetween(this, Vector2_v1.Right());
        
        if(this.y < 0){
            // If vector is below x axis, convert it t standard position
            a *= -1;
            a += Math.PI * 2;
        }
        return a;
    }

    
    // Default Vectors
    public static Vector2_v1 Left(){
        return new Vector2_v1(-1, 0);
    }
    public static Vector2_v1 Right(){
        return new Vector2_v1(1, 0);
    }
    public static Vector2_v1 Up(){
        return new Vector2_v1(0, 1);
    }
    public static Vector2_v1 Down(){
        return new Vector2_v1(0, -1);
    }
    public static Vector2_v1 Zero(){
        return new Vector2_v1(0, 0);
    }
    
    // Math operators on current Vector2
    // Modifies the Vector2, then returns the new result
    public Vector2_v1 Scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }
    public Vector2_v1 Add(Vector2_v1 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }
    public Vector2_v1 Subtract(Vector2_v1 v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }
    public Vector2_v1 Normalize() {
        // Reduce length to 1
        double m = this.mag();
        this.x /= m;
        this.y /= m;
        return this;
    }
    public Vector2_v1 Limit(double length) {
        // Limit a vector to a certain length
        if (length * length >= this.sqmag()) {
            // Do nothing if length is acceptable
            return this;
        }
        double m = this.mag();
        this.x *= length / m;
        this.y *= length / m;
        return this;
    }
    
    // Comparisons
    public boolean equals(Vector2_v1 v){
        // Returns if two vectors are equal
        return this.x == v.x && this.y == v.y;
    }
    public boolean perpendicular(Vector2_v1 v){
        // Vectors perpendicular if dot=0
        return Vector2_v1.dot(this, v) == 0;
    }
    public boolean collinear(Vector2_v1 v){
        // Collinear if dot product magnitude is the same as product of magnitudes
        // As this means cos(theta) = 1 or -1
        return Math.abs(Math.pow(Vector2_v1.dot(this, v), 2)) == this.sqmag() * v.sqmag();
    }
    
    // Static methods
    // Takes in one / two Vector2 and returns something else,
    // Without modifying the inputs
    public static Vector2_v1 add(Vector2_v1 v1, Vector2_v1 v2) {
        return new Vector2_v1(v1.x + v2.x, v1.y + v2.y);
    }
    public static Vector2_v1 subtract(Vector2_v1 v1, Vector2_v1 v2) {
        return new Vector2_v1(v1.x - v2.x, v1.y - v2.y);
    }
    public static Vector2_v1 scale(Vector2_v1 v, double scalar){
        return new Vector2_v1(v).Scale(scalar);
    }
    public static double dot(Vector2_v1 v1, Vector2_v1 v2){
        // Calculate the dot product of two vectors
        return v1.x*v2.y + v1.y*v2.x;
    }
    public static double angleBetween(Vector2_v1 v1, Vector2_v1 v2){
        // Returnt the angle between two Vector2 in radians
        return Math.acos(Vector2_v1.dot(v1, v2) / (v1.mag() * v2.mag()));
    }

    // Comparisons
    public static boolean equals(Vector2_v1 v1, Vector2_v1 v2){
        return v1.x == v2.x && v1.y == v2.y;
    }
    public static boolean perpendicular(Vector2_v1 v1, Vector2_v1 v2){
        return Vector2_v1.dot(v1, v2) == 0;
    }    
    public static boolean collinear(Vector2_v1 v1, Vector2_v1 v2){
        // Collinear if dot product magnitude is the same as product of magnitudes
        // As this means cos(theta) = 1 or -1
        return Math.abs(Math.pow(Vector2_v1.dot(v1, v2), 2)) == v1.sqmag() * v2.sqmag();
    }
    
    @Override
    public String toString() {
        // Return the components within square brackets
        return "[" + x + ", " + y + "]";
    }
    public double[] toArray(){
        // Get vector as an array
        return new double[] {x, y};
    }

}
