// Blake Freer
// May 31, 2020
// Moves the player in a circle

package _Unused;

import GameObjects.Components.Component;
import Engine.GameMath.Vector2;


public class MovePlayerCircle extends Component {

    Vector2 x;
    Vector2 velocity;
    
    @Override
    public void Start(){
        x = Vector2.zero();
        velocity = new Vector2(6, 0);
    }
    
    @Override
    public void Update() {
        gameObject.transform.SetLocalPosition(x);
        x.Add(Vector2.Scale(velocity, gameData.deltaTime));
        if(x.magnitude() > 5){
            velocity.Scale(-1);
        }
    }

    @Override
    public MovePlayerCircle Clone(){
        System.out.println("MovePlayerCircle cannot be cloned");
        return null;
    }
    
}
