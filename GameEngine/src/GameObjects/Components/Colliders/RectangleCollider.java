// Blake Freer
// Jun 5, 2020
package GameObjects.Components.Colliders;

import Engine.GameMath.Vector2;

public class RectangleCollider extends Collider {

    public RectangleCollider(double width, double height, Vector2 offset) {
        super(width, height, offset);
    }

    public boolean CheckCollision(RectangleCollider r) {
        // Collision between this rectangle collider and another rectangle collider

        // Call static function from Collider class
        return Collider.CheckCollision(this, r);
    }
    public boolean CheckCollision(CircleCollider c) {
        // Collision between this rectangle collider and a circle collider

        return Collider.CheckCollision(this, c);
    }
    
    @Override
    public RectangleCollider Clone(){
        return new RectangleCollider(this.width, this.height, new Vector2(this.offset));
    }

}
