// Blake Freer
// Jun 12, 2020
// Class for determining 2D collisions with colliders using rays.
// Does so by sending out a line along a Vector, and determining if it collides with any geometry.
package Engine.Physics2D;

import Engine.GameMath.Mathf;
import Engine.GameMath.Vector2;
import GameObjects.Components.Colliders.CircleCollider;
import GameObjects.Components.Colliders.Collider;
import java.util.ArrayList;
import java.util.Arrays;

public class Physics {

    /**
     * Send a Raycast to determine if there are any colliders along a path.
     *
     * @param rayOrigin Global position where ray originates.
     * @param direction Direction that the ray follows.
     * @param length Length of the ray.
     * @param tagMask Only check for colliders with given tags. If none are
     * passed, all colliders are checked.
     * @return A {@code RaycastHit} if there is a collision, otherwise
     * {@code null}.
     */
    public static RaycastHit Raycast(Vector2 rayOrigin, Vector2 direction, double length, String... tagMask) {
        // Calculate ray end point
        // Uses Liang-Barsky algorithm for line-rectangle collisions.
        Vector2 deltaRay = Vector2.Scale(direction.normalized(), length);
        ArrayList<Collider> possibleColliders = Collider.gameData.scene.GetColliders();

        // Convert tags to list
        ArrayList<String> tagMaskList = new ArrayList<>(Arrays.asList(tagMask));

        RaycastHit closestHit = null;

        for (Collider col : possibleColliders) {

            if (col instanceof CircleCollider) {
                // Ignore circles for now
                continue;
            }

            if (!(tagMask.length == 0 || tagMaskList.contains(col.gameObject.GetTag()))) {
                // Skip collider if it is not in tagMask, unless no mask was provided.
                continue;
            }

            // Check for each collider
            Vector2 min = col.GetBounds().min;
            Vector2 max = col.GetBounds().max;

            // Calculate "u_k" values
            double[] u = new double[4];
            u[0] = (rayOrigin.x - min.x) / -deltaRay.x;
            u[1] = (max.x - rayOrigin.x) / deltaRay.x;
            u[2] = (rayOrigin.y - min.y) / -deltaRay.y;
            u[3] = (max.y - rayOrigin.y) / deltaRay.y;

            for (double uk : u) {
                // Find closest hit point
                if (uk < 0 || uk > 1) {
                    // Ignore if uk is not in [0, 1], as the hit point is outside of the line.
                    continue;
                }
                // Calculate hit point and distance
                Vector2 hitPoint = Vector2.Add(rayOrigin, Vector2.Scale(deltaRay, uk));
                
                if (!(Mathf.InRange(hitPoint.x, min.x, max.x) && Mathf.InRange(hitPoint.y, min.y, max.y))) {
                    // If the hitpoint is outside the rectangle, skip the point
                    continue;
                }
                
                double hitLength = Vector2.Distance(rayOrigin, hitPoint);

                if (closestHit == null || hitLength < closestHit.distance) {
                    // If the new hit is closer that the current closest, reset closestHit
                    closestHit = new RaycastHit(col, hitLength, hitPoint);
                }
            }
        }

        // Return the closest hit, or null if there were none.
        return closestHit;

    }

    /**
     * Class for storing information about a Raycast collision
     */
    public static class RaycastHit {
        /**
         * Collider component that was hit.
         */
        public final Collider collider;

        /**
         * The distance of the vector from the ray origin to collision point.
         */
        public final double distance;

        /**
         * Tag of the GameObject that was hit. Same as
         * {@code collider.gameObject.tag}.
         */
        public final String tag;

        /**
         * World space position where the ray collided with the collider.
         */
        public final Vector2 hitPoint;

        private RaycastHit(Collider collider, double distance, Vector2 hitPoint) {
            // Constructor
            this.collider = collider;
            this.distance = distance;
            this.hitPoint = hitPoint;
            this.tag = collider.gameObject.GetTag();
        }
        
    }

}
