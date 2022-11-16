// Blake Freer
// Jun 13, 2020
// Modifies the velocity from Player class by checking for collisions.
// Translated and modified from C# code by Sebastian Lague
package Assets.CustomScripts.LagueController;

import Engine.Physics2D.Physics;
import Engine.Physics2D.Physics.RaycastHit;
import Engine.GameMath.Mathf;
import Engine.GameMath.Vector2;
import GameObjects.Components.Colliders.Collider.Bounds;
import GameObjects.Components.Colliders.RectangleCollider;
import GameObjects.Components.Component;
import GameObjects.Components.Transform;

public class Controller2D extends Component {

    // Variables
    public String[] collisionMask;

    // Raycasting
    final double skinWidth = 0.02;
    int horizontalRayCount = 4; // Number of rays to send horizontally
    int verticalRayCount = 4; // Number of rays to send vertically
    double horizontalRaySpacing; // Separation between horizontal rays
    double verticalRaySpacing;   //      "                     "

    // Components / Classes
    RectangleCollider rectCollider;
    Transform transform;
    RaycastOrigins raycastOrigins;
    public CollisionInfo collisionFlags;

    public Controller2D() {
        collisionFlags = new CollisionInfo();
        raycastOrigins = new RaycastOrigins();
    }

    @Override
    public void Start() {
        rectCollider = gameObject.GetComponent(RectangleCollider.class);
        transform = gameObject.GetComponent(Transform.class);

        collisionFlags.Reset();
        CalculateRaySpacing();
    }

    public void Move(Vector2 velocity) {
        UpdateRaycastOrigins();
        collisionFlags.Reset();

        collisionFlags.velocityOld = velocity;

        if (velocity.x != 0) {
            HorizontalCollisions(velocity);
        }
        if (velocity.y != 0) {
            VerticalCollisions(velocity);
        }

        transform.Move(velocity);

    }

    // Determine horizontal and vertical collisions, and modify velocity accordingly
    void HorizontalCollisions(Vector2 velocity) {
        double directionX = Mathf.Sign(velocity.x);
        double rayLength = Math.abs(velocity.x) + skinWidth;

        for (int i = 0; i < horizontalRayCount; i++) {
            // If moving left, start rays at bottomLEft, otherwise bottomRight
            Vector2 rayOrigin = new Vector2((directionX == -1) ? raycastOrigins.bottomLeft : raycastOrigins.bottomRight);

            // Actual position of rayOrigin depends on spacing and iteration in loop
            rayOrigin.Add(Vector2.Scale(Vector2.up(), i * horizontalRaySpacing));

            // Send out a raycast from RayOrigin
            RaycastHit hit = Physics.Raycast(rayOrigin, Vector2.Scale(Vector2.right(), directionX), rayLength, collisionMask);

            if (hit != null) {
                // If the ray found something...

                // Modify the velocity to move the player to the obstacle
                velocity.x = (hit.distance - skinWidth) * directionX;
                // Reduce the ray length to ensure a different iteration does not find a further object and try to move the player to there
                rayLength = hit.distance;

                collisionFlags.left = directionX == -1;
                collisionFlags.right = directionX == 1;
            }
        }
    }
    void VerticalCollisions(Vector2 velocity) {

        double directionY = Mathf.Sign(velocity.y);
        double rayLength = Math.abs(velocity.y) + skinWidth;

        for (int i = 0; i < verticalRayCount; i++) {
            // If moving down, rayOrigin is at bottom, otherwise at top
            Vector2 rayOrigin = new Vector2((directionY == -1) ? raycastOrigins.bottomLeft : raycastOrigins.topLeft);
            // Actual position depends on spacing and iteration, as well as the velocity.x since the player has moved due to horizontalCollisions
            rayOrigin.Add(Vector2.Scale(Vector2.right(), verticalRaySpacing * i + velocity.x));

            // Send out a Raycast
            RaycastHit hit = Physics.Raycast(rayOrigin, Vector2.Scale(Vector2.up(), directionY), rayLength, collisionMask);

            if (hit != null) {
                // If ray found something, move player to vertically to the obstacle
                velocity.y = (hit.distance - skinWidth) * directionY;
                // Ensure another ray does not hit a further object
                rayLength = hit.distance;

                collisionFlags.below = directionY == -1;
                collisionFlags.above = directionY == 1;
            }
        }
        
    }

    void UpdateRaycastOrigins() {
        // Update the world positions of the corners of the box
        Bounds bounds = rectCollider.GetBounds();
        bounds.Expand(-skinWidth);

        // Set raycastOrigins to the corners of the bounds
        raycastOrigins.bottomLeft = new Vector2(bounds.min.x, bounds.min.y);
        raycastOrigins.bottomRight = new Vector2(bounds.max.x, bounds.min.y);
        raycastOrigins.topLeft = new Vector2(bounds.min.x, bounds.max.y);
//        raycastOrigins.topRight = new Vector2(bounds.max.x, bounds.max.y);
//        System.out.println("Bottom left: "+raycastOrigins.bottomLeft);
//        System.out.println("Top Left: "+raycastOrigins.topLeft);
//        System.out.println("Bottom right: "+raycastOrigins.bottomRight);
//        System.out.println("Top right: "+raycastOrigins.topRight);

    }
    void CalculateRaySpacing() {
        // Determine spacing between rays
        Bounds bounds = rectCollider.GetBounds();
        bounds.Expand(-skinWidth);

        //Ensure there are at least 2 rays in each direction
        horizontalRayCount = (int) Mathf.Clamp(horizontalRayCount, 2, Integer.MAX_VALUE);
        verticalRayCount = (int) Mathf.Clamp(verticalRayCount, 2, Integer.MAX_VALUE);

        // There is one less space than ray in each direction
        horizontalRaySpacing = bounds.GetSize().y / (horizontalRayCount - 1);
        verticalRaySpacing = bounds.GetSize().x / (verticalRayCount - 1);
    }

    public class RaycastOrigins {
        // Class holding th ecorners of the collider, shrunken by skinWidth
        public Vector2 topLeft, topRight, bottomLeft, bottomRight;
    }

    public class CollisionInfo {
        public boolean above, below, left, right;

        public Vector2 velocityOld;

        public void Reset() {
            // Reset all flags to false
            above = false;
            below = false;
            left = false;
            right = false;
        }

    }

    @Override
    public Component Clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
