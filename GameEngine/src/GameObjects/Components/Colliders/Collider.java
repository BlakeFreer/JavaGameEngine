// Blake Freer
// Jun 5, 2020
// Abstract class for defining collisions between objects
package GameObjects.Components.Colliders;

import GameObjects.Components.Component;
import Engine.GameMath.Mathf;
import Engine.GameMath.Vector2;

public abstract class Collider extends Component {

    // Relative position from transform
    public Vector2 offset;
    protected Vector2 sizeOffset;
    protected Vector2 totalOffset;

    // Size
    public double width;
    public double height;

    public Collider(double width, double height, Vector2 offset) {
        super();

        this.width = width;
        this.height = height;

        this.offset = new Vector2(offset);
        this.sizeOffset = new Vector2(width / 2, height / 2);

        // Modify offset based on size
        this.totalOffset = Vector2.Subtract(this.offset, this.sizeOffset);

    }

    public Vector2 GetGlobPos() {
        // Short for getting the centre of the object collider
        return Vector2.Add(gameObject.transform.GetGlobalPosition(), offset);
    }

    // Collision detection
    public static boolean CheckCollision(CircleCollider c1, CircleCollider c2) {
        // Returns true if colliding
        Vector2 separationVec = Vector2.Subtract(c1.GetGlobPos(), c2.GetGlobPos());
        double sepDistSq = separationVec.sqrMagnitude();
        // Colliding if distance is less than sum radii
        return sepDistSq <= Math.pow(c1.radius + c2.radius, 2);
    }
    public static boolean CheckCollision(RectangleCollider r1, RectangleCollider r2) {
        // Use AABB collision detection algorithm
        Bounds b1 = r1.GetBounds();
        Bounds b2 = r2.GetBounds();

        return b1.min.x <= b2.max.x && b1.max.x >= b2.min.x
                && b1.min.y <= b2.max.y && b1.max.y >= b2.min.y;
    }
    public static boolean CheckCollision(RectangleCollider r, CircleCollider c) {
        // Can use clamping to determine closest point in rectangle to circle since there is no rotation
        Vector2 closestPoint = c.GetGlobPos();
        Bounds rBounds = r.GetBounds();
        closestPoint.x = Mathf.Clamp(closestPoint.x, rBounds.min.x, rBounds.max.x);
        closestPoint.y = Mathf.Clamp(closestPoint.y, rBounds.min.y, rBounds.max.y);

        // If the closest point in the rectangle is within the radius, the colliders overlap
        return Vector2.SqrDistance(closestPoint, c.GetGlobPos()) < c.radius * c.radius;

    }
    public static boolean CheckCollision(CircleCollider c, RectangleCollider r) {
        // Repetitive from previous method, so just call it with reordered parameters
        return CheckCollision(r, c);
    }

    public Bounds GetBounds() {
        // Return the bounds of this object
        Bounds bounds = new Bounds();
        bounds.min = Vector2.Subtract(GetGlobPos(), new Vector2(width / 2, 0));
        bounds.max = Vector2.Add(GetGlobPos(), new Vector2(width / 2, height));

        return bounds;
    }

    public class Bounds {
        // Stores the edges of the bounding rectangle of a collider
        // Min is the bottom left of the rectangle, max is the top right
        public Vector2 min, max;

        /**
         * Make the bounds larger by some amount.
         *
         * @param distance Distance that each edge will be expanded. Note the
         * total change in size over each dimension is {@code 2 * distance}.
         */
        public void Expand(double distance) {
            min.Subtract(new Vector2(distance, distance));
            max.Add(new Vector2(distance, distance));
        }
        public Vector2 GetSize() {
            // Return the size of the bounds
            return new Vector2(max.x - min.x, max.y - min.y);
        }
    }

    @Override
    public void Start() {
        gameData.scene.AddCollider(this);
    }

}
