// Blake Freer
// May 26, 2020
// Vector class for 2d vectors and operations
// Based off of Unity's Vector3 (originally in C#)

package _Unused;

// Right hand rule: x - index, y - thumb, z - middle

import Engine.GameMath.Mathf;
import Engine.GameMath.Vector2;

// x - right
// y - up
// z - forward

public class Vector3 {
    // Instance variables
    public double x;
    public double y;
    public double z;
    
    // Static properties
    public static Vector3 back = new Vector3(0, 0, -1);
    public static Vector3 down = new Vector3(0, -1, 0);
    public static Vector3 left = new Vector3(-1, 0, 0);
    public static Vector3 forward = new Vector3(0, 0, 1);
    public static Vector3 up = new Vector3(0, 1, 0);
    public static Vector3 right = new Vector3(1, 0, 0);
    public static Vector3 zero = new Vector3(0, 0, 0);
    public static Vector3 negativeInfinity = new Vector3(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    public static Vector3 positiveInfinity = new Vector3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
    
    // Constructors
    public Vector3(double x, double y, double z){
        // Constructor that sets components directly
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3(Vector3 v){
        // Constructor that makes a copy of another vector3
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    public Vector3(Vector2 v){
        // Create a new vector3 based on x and y of Vector2, z = 0
        this.x = v.x;
        this.y = v.y;
        this.z = 0;
    }
    
    // Properties
    public double magnitude(){
        // Returns the magnitude of the vector
        return Math.sqrt(x*x + y*y + z*z);
    }
    public Vector3 normalized(){
        // Return this vector with magnitude 1
        return Vector3.Divide(this, magnitude());
    }
    public double sqrMagnitude(){
        // Return tehe sqaure of magnitude to save a sqrt call
        return x*x + y*y + z*z;
    }
    
    // Public Methods
    public boolean Equals(Vector3 v){
        // Two vectors are equal iff their components are equal
        return x == v.x && y == v.y && z == v.z;
    }
    public void Set(double newX, double newY, double newZ){
        x = newX;
        y = newY;
        z = newZ;
    }
    @Override
    public String toString(){
        return "["+x+", "+y+", "+z+"]";
    }
    public double[] toArray(){
        return new double[] {x, y, z};
    }
    
    public void Add(Vector3 v){
        // Modify the vector by adding another
        x += v.x;
        y += v.y;
        z += v.z;
    }
    public void Subtract(Vector3 v){
        // Modify the vector by subtracting another from it
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }
    public void Scale(double scalar){
        // Modify the vector by scaling it
        x *= scalar;
        y *= scalar;
        z *= scalar;
    }
    public void Divide(double divisor){
        // Modify the vector by scaling it down
        x /= divisor;
        y /= divisor;
        z /= divisor;
    }
    
    // Static Methods
    public static double Angle(Vector3 from, Vector3 to){
        // Angle between two vectors, never greater than 180
       return Math.acos(Vector3.Dot(from, to)/(from.magnitude()*to.magnitude()));
    }
    public static Vector3 ClampMagnitude(Vector3 vector, double maxLength){
        // Return a copy of a vector with a magnitude clamped to maxLength
        Vector3 newVector = new Vector3(vector);
        if(vector.sqrMagnitude() > maxLength*maxLength){
            newVector.Scale(maxLength/vector.magnitude());
        }
        return newVector;
    }
    public static Vector3 Cross(Vector3 a, Vector3 b){
        // Calculate and return the cross product of two vectors
        double cx, cy, cz;
        cx = a.y*b.z - a.z*b.y;
        cy = a.z*b.x - a.x*b.z;
        cz = a.x*b.y - a.y*b.x;
        return new Vector3(cx, cy, cz);
    }
    public static double Distance(Vector3 from, Vector3 to){
        // Return the distance between two points
        return Vector3.Subtract(from, to).magnitude();
    }
    public static double Dot(Vector3 v1, Vector3 v2){
        // Calculate the dot prodcut of the two vectors
        return v1.x * v2.x + v1.y * v2.y + v1.z + v2.z;
    }
    public static Vector3 Lerp(Vector3 a, Vector3 b, double t){
        // Linearly interpolates between 'a' and 'b' by 't'
        // returns a if t = 0
        // returns b if t = 1
        // t clamped to [0,1]
        t = Mathf.Clamp01(t);
        Vector3 offset = Vector3.Subtract(b, a);
        offset.Scale(t);
        return Vector3.Add(a, offset);
    }
    public static Vector3 LerpUnclamped(Vector3 a, Vector3 b, double t){
        // Interpolates or extrapolates between 'a' and 'b' by 't'
        Vector3 offset = Vector3.Subtract(b, a);
        offset.Scale(t);
        return Vector3.Add(a, offset);
    }
    
    // Basic math operators
    public static Vector3 Add(Vector3 v1, Vector3 v2){
        // Add two vectors and return the sum
        Vector3 sum = new Vector3(v1);
        sum.Add(v2);
        return sum;
    }
    public static Vector3 Subtract(Vector3 v1, Vector3 v2){
        // Subtract v2 from v1
        Vector3 difference = new Vector3(v1);
        difference.Subtract(v2);
        return difference;
    }
    public static Vector3 Scale(Vector3 v, double scalar){
        // Scale a vector by a scalar
        Vector3 scaled = new Vector3(v);
        scaled.Scale(scalar);
        return scaled;
    }
    public static Vector3 Divide(Vector3 v, double divisor){
        // Scale down a vector and return the quotient
        Vector3 quotient = new Vector3(v);
        quotient.Divide(divisor);
        return quotient;
    }
}
