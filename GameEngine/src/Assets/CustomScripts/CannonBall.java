// Blake Freer
// Jun 18, 2020
// The cannon ball will fly through the sky and check to see if it hits the player.
package Assets.CustomScripts;

import Assets.CustomScripts.LagueController.Player;
import Engine.GameMath.Vector2;
import GameObjects.Components.Colliders.CircleCollider;
import GameObjects.Components.Colliders.Collider;
import GameObjects.Components.Colliders.RectangleCollider;
import GameObjects.Components.Component;
import GameObjects.GameObject;

public class CannonBall extends Component {

    // Does the cannonball move left or right?
    boolean facingRight;

    // Collider of the player
    RectangleCollider playerCollider;

    // Collider of this object
    public CircleCollider collider;

    // Movement
    Vector2 velocity;
    double horizontalVelocity = 6;
    double verticalVelocity = 3;
    Vector2 gravity = new Vector2(0, -9 * gameData.deltaTime);

    // Constructor
    public CannonBall(boolean facingRight) {
        
        this.facingRight = facingRight;

        playerCollider = GameObject.FindGameObjectWithTag("player", gameData.scene).GetComponent(RectangleCollider.class);
        
        // Create initial velocity with horizontal and vertical components
        velocity = new Vector2(facingRight ? horizontalVelocity : -horizontalVelocity, verticalVelocity);

    }

    @Override
    public void Update() {
        // Update the gravity and move the ball
        velocity.Add(gravity);
        gameObject.transform.Move(Vector2.Scale(velocity, gameData.deltaTime));

        if (Collider.CheckCollision(playerCollider, collider)) {
            playerCollider.gameObject.GetComponent(Player.class).Die();
            GameObject.Destroy(this.gameObject, 0.05);
        }
    }

    @Override
    public Component Clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
