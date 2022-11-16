// Blake Freer
// Jun 5, 2020
package GameObjects.Components.Colliders;

import Engine.GameMath.Vector2;

public class CircleCollider extends Collider {

    // Size
    public double radius;

    public CircleCollider(double radius, Vector2 offset) {
        super(radius * 2, radius * 2, Vector2.Add(offset, new Vector2(radius, radius)));
        this.radius = radius;
    }

    public boolean CheckCollision(RectangleCollider r) {
        // Collision between this circle collider and a rectangle collider

        // Call static function from Collider class
        return Collider.CheckCollision(this, r);
    }
    public boolean CheckCollision(CircleCollider c) {
        // Collision between this circle collider and another circle collider

        return Collider.CheckCollision(this, c);
    }
    
    @Override
    public CircleCollider Clone(){
        // Return a copy of this component
        return new CircleCollider(this.radius, new Vector2(this.offset));
    }

}
