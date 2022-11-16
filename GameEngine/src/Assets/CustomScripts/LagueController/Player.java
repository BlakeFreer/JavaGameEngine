// Blake Freer
// Jun 13, 2020
// Takes player input and determines the velocity that the player should attempt to move by.
package Assets.CustomScripts.LagueController;

import Engine.GameMath.Mathf;
import Engine.GameMath.Vector2;
import GameObjects.Components.Component;
import GameObjects.Components.Renderers.Animator;
import MainPackage.InputManager;
import java.awt.event.KeyEvent;

public class Player extends Component {

    // Input
    InputManager controls;
    Vector2 input;

    // Player movement variables
    double jumpHeight = 3.2;
    double timeToJumpApex = 0.4;
    double accelerationTimeAirborne = 0.18;
    double accelerationTimeGrounded = 0.05;
    double moveSpeed = 6;

    double jumpSpeed;
    double gravity;
    boolean waitingToJump;

    Vector2 velocity;
    double targetVelX;
    Vector2 smoothing;
    Vector2 oldVelocity; // Used for Verlet Integration for numerical accuracy in movement

    // Components
    Controller2D controller;
    Animator animator;

    // Death
    boolean isDying = false;

    @Override
    public void Start() {
        // Get components
        controls = gameData.input;
        controller = gameObject.GetComponent(Controller2D.class);
        animator = gameObject.GetComponent(Animator.class);
        input = Vector2.zero();

        // Calculate jump values
        gravity = -(2 * jumpHeight / Math.pow(timeToJumpApex, 2));
        jumpSpeed = -gravity * timeToJumpApex;

        velocity = Vector2.zero();
        smoothing = Vector2.zero();
    }

    @Override
    public void Update() {

        if (isDying) {
            if (animator.IsPlaying()) {
                // Don't do anything if the animation is playing
                return;
            }
            // If the player has died and the animation is done, reset player position to start
            gameObject.transform.SetLocalPosition(Vector2.zero());
            isDying = false;
            velocity = Vector2.zero();

        }

        if (!animator.IsPlaying()) {
            // If animator is not playing, change the animation depending on the direction the player is moving
            if (velocity.x < -0.02) {
                animator.PlayOneShot("left");
            } else if (velocity.x > 0.02) {
                animator.PlayOneShot("right");
            } else {
                animator.PlayOneShot("idle");
            }
        }

        if (controller.collisionFlags.above || controller.collisionFlags.below) {
            // Prevent graivty from building up
            velocity.y = 0;
        }

        //input = new Vector2(controls.GetAxis2D(InputManager.WASD));
        input.x = controls.GetAxis(InputManager.HORIZONTAL);
        input.y = 1;//controls.GetAxis(InputManager.VERTICAL);

        // Jumping input
        if (controls.GetKeyDown(KeyEvent.VK_SPACE) && controller.collisionFlags.below) {
            // Jump if player presses jump and is on ground
            velocity.y = jumpSpeed;
            animator.PlayOneShot("jump");
        }

        // Set horizontal velocity
        targetVelX = input.x * moveSpeed;
        velocity.x = Mathf.SmoothDamp(velocity.x, targetVelX, smoothing, controller.collisionFlags.below ? accelerationTimeGrounded : accelerationTimeAirborne);
        oldVelocity = new Vector2(velocity);
        // Add gravity
        velocity.y += gravity * gameData.deltaTime;
        // Use Verlet integration and call Move with the velocity
        controller.Move(Vector2.Scale(Vector2.Add(velocity, oldVelocity), 0.5 * gameData.deltaTime));
    }

    /**
     * End the player. Play death animation.
     */
    public void Die() {
        // Make sure player isn't already dying (prevents dying again while dying)
        if (!isDying) {
            isDying = true;
            animator.PlayOneShot("die");
        }
    }

    @Override
    public Component Clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
