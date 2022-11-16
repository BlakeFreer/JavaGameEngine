// Blake Freer
// May 27, 2020
// Holds location and other transform data
// All objects must have this component
package GameObjects.Components;

import Engine.GameMath.Vector2;

/**
 * A component describing the position of a GameObject relative to its parent GameObject.<br>
 * All GameObjects have a Transform.
 * @author Blake Freer
 */
public class Transform extends Component {

    /**
     * LocalPosition is the location of a GameObject relative to its parent.
    */
    private Vector2 localPosition;

    /**
     * Constructions requires a Vector2 for the position.
     * @param localPos 
     */
    public Transform(Vector2 localPos) {
        super();
        this.localPosition = new Vector2(localPos);
    }

    /**
     * Returns the position of the Transform relative to the world.
     * @return 
     */
    public Vector2 GetGlobalPosition() {
        // Global position should only be read, not written

        // Recursive algorithm to calculate global position
        // Uses sum of relative positions until reaching highest parent ("Scene")
        if (gameObject.name.equalsIgnoreCase("scene")) {
            return Vector2.zero();
        } else {
            return Vector2.Add(this.localPosition, gameObject.parent.transform.GetGlobalPosition());
        }
    }
    
    /**
     * Set the global position of a transform.
     * @param newGlobalPos 
     */
    public void SetGlobalPosition(Vector2 newGlobalPos){
        SetLocalPosition(Vector2.Subtract(newGlobalPos, gameObject.parent.transform.GetGlobalPosition()));
    }

    /**
     * Returns the position of the Transform relative to its parent Transform.
     * @return Vector2
     */
    public Vector2 GetLocalPosition() {
        return localPosition;
    }
    
    /**
     * Set the position of the Transform relative to its parent Transform.
     * @param newLocalPos 
     */
    public void SetLocalPosition(Vector2 newLocalPos) {
        localPosition = newLocalPos;
    }
    
    /**
     * Move the GameObject 
     * @param movement Vector2 to move the player.
     */
    public void Move(Vector2 movement){
        localPosition.Add(movement);
    }
    
    @Override
    public Transform Clone() {
        // Return a new transform identical to this
        return new Transform(new Vector2(this.localPosition));
    }

}
