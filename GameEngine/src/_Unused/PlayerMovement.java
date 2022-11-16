// Blake Freer
// May 31, 2020
// Uses keyboard input to move the player
package _Unused;

import GameObjects.Components.Colliders.*;
import GameObjects.Components.*;
import MainPackage.InputManager;
import Engine.GameMath.Vector2;
import Engine.Physics2D.Physics;
import Engine.Physics2D.Physics.RaycastHit;
import GameObjects.Components.Renderers.RectangleRenderer;
import java.awt.Color;

public class PlayerMovement extends Component {

    Vector2 position;
    Vector2 velocity;
    double speed = 3.5;

    public CircleCollider ball;
    private RectangleCollider playerCollider;

    @Override
    public void Start() {
        position = gameObject.transform.GetLocalPosition();
        velocity = Vector2.zero();
        playerCollider = gameObject.GetComponent(RectangleCollider.class);
    }

    @Override
    public void Update() {
        // Move the player
        velocity = gameData.input.GetAxis2D(InputManager.WASD);

        position.Add(Vector2.Scale(velocity, gameData.deltaTime * speed));
        gameObject.transform.SetLocalPosition(position);

        boolean colliding = playerCollider.CheckCollision(ball);

        gameObject.GetComponent(RectangleRenderer.class).color = colliding ? Color.red : Color.green;

        // Raycast
        RaycastHit hit = Physics.Raycast(playerCollider.GetBounds().min, Vector2.down(), 0.1, "obstacle");
        
        if (hit != null) {
            System.out.println(hit.collider.gameObject.name + "  " + hit.distance);
        } else {
            System.out.println("null");
        }
    }

    @Override
    public PlayerMovement Clone() {
        System.out.println("PlayerMovement cannot be cloned");
        return null;
    }

}
