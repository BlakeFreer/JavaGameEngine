// Blake Freer
// Jun 19, 2020
// Handles coin behaviour
package Assets.CustomScripts;

import GameObjects.Components.Colliders.CircleCollider;
import GameObjects.Components.Colliders.Collider;
import GameObjects.Components.Colliders.RectangleCollider;
import GameObjects.Components.Component;
import GameObjects.Components.Renderers.Animator;
import GameObjects.GameObject;

public class Coin extends Component {

    // Components
    CircleCollider collider;
    Animator animator;
    RectangleCollider playerCollider;

    private boolean pickedUp;

    @Override
    public void Start() {
        // Get components from this object
        collider = gameObject.GetComponent(CircleCollider.class);
        animator = gameObject.GetComponent(Animator.class);
        // Get player's collider
        playerCollider = GameObject.FindGameObjectWithTag("player", gameData.scene).GetComponent(RectangleCollider.class);
    }

    @Override
    public void Update() {
        if (!animator.IsPlaying()) {
            if (pickedUp) {
                // If animator is not playing and the coin has been picked up, destroy the object
                GameObject.Destroy(gameObject);
            } else {
                // If the animator is not playing and the coin has not been picked up, replay spin animation
                animator.PlayOneShot("coinSpin");

            }
        }
        if (!pickedUp && Collider.CheckCollision(collider, playerCollider)) {
            pickedUp = true;
            animator.PlayOneShot("coinGet");
        }
    }

    @Override
    public Component Clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
