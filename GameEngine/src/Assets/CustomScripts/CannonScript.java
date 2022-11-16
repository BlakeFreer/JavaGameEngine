// Blake Freer
// Jun 18, 2020
// Makes the cannon fire cannonballs
package Assets.CustomScripts;

import Engine.GameMath.Vector2;
import GameObjects.Components.Colliders.CircleCollider;
import GameObjects.Components.Component;
import GameObjects.Components.Renderers.Animator;
import GameObjects.Components.Renderers.SpriteRenderer;
import GameObjects.GameObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CannonScript extends Component {

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    // Components
    public Animator animator;

    // Firing variables
    public double cooldownSeconds = 2; // How long do the cannon wait before firing again
    public boolean facingRight = true;

    // Timing
    private boolean readyToFire = true;

    @Override
    public void Start() {
        // Get components
        animator = gameObject.GetComponent(Animator.class);

    }

    @Override
    public void Update() {
        if (readyToFire) {
            animator.PlayOneShot("fire");
            readyToFire = false;
            executorService.schedule(this::Fire, 500, TimeUnit.MILLISECONDS);
            executorService.schedule(this::Reload, (long) (cooldownSeconds * 1000), TimeUnit.MILLISECONDS);
        }
    }

    private void Fire() {

        // Create and launch cannonball
        GameObject ball = new GameObject("cannonball", this.gameObject);
        ball.transform.SetLocalPosition(Vector2.zero());

        SpriteRenderer sr = new SpriteRenderer(0.5, 0.5, Vector2.zero());
        sr.SetSprite("src/Assets/Images/Items/Cannon/CannonBall.png");
        sr.layerHeight = 2;
        ball.AddComponent(sr);

        ball.AddComponent(new CircleCollider(0.2, new Vector2(0.2, 0.2)));

        CannonBall newCBall = new CannonBall(facingRight);
        newCBall.collider = ball.GetComponent(CircleCollider.class);

        ball.AddComponent(newCBall);
        
        GameObject.Destroy(ball, 5);

    }

    private void Reload() {
        readyToFire = true;
    }

    @Override
    public Component Clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
