// Blake Freer
// Jun 13, 2020
// Move the player around, checking for collisions with the ground

package _Unused;

import Engine.Physics2D.Physics;
import Engine.Physics2D.Physics.RaycastHit;
import Engine.GameMath.Vector2;
import GameObjects.Components.Colliders.RectangleCollider;
import GameObjects.Components.Component;
import MainPackage.InputManager;
import java.awt.event.KeyEvent;

public class PlayerController_v1 extends Component {
    
    Vector2 velocity;
    double speed = 5;
    double gravity = 9.8;
    
    private RectangleCollider collider;
    
    @Override
    public void Start(){
        collider = gameObject.GetComponent(RectangleCollider.class);
        velocity = Vector2.zero();
    }
    
    @Override
    public void Update(){
        velocity.x = gameData.input.GetAxis(InputManager.HORIZONTAL) * speed;
        
        velocity.y -= gravity * gameData.deltaTime;
        
        RaycastHit hit = Physics.Raycast(collider.GetBounds().min, Vector2.down(), -velocity.y*gameData.deltaTime, "obstacle");
        
        if(hit != null){
            velocity.y = -hit.distance;
        }
        if(gameData.input.GetKeyDown(KeyEvent.VK_W)){
            velocity.y = 5;
        }
        gameObject.transform.Move(Vector2.Scale(velocity, gameData.deltaTime));
    }
    
    @Override
    public Component Clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
