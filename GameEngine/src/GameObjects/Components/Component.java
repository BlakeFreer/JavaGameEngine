// Blake Freer
// May 27, 2020
// Abstract class for components
package GameObjects.Components;

import GameObjects.GameObject;
import MainPackage.*;

/**
 * An abstract class for creating various behaviors or properties for
 * GameObjects.
 *
 * @author Blake Freer
 */
public abstract class Component {

    /**
     * Only enabled Components are updated each frame. All Components will have
     * {@code Start} called regardless of {@code enabled}.
     */
    public boolean enabled;

    /**
     * The GameObject to which this Component belongs.
     */
    public GameObject gameObject;

    /**
     * A reference to the GameData that runs the game.
     */
    public static GameData gameData = MainPackage.ContentScreen.gameData;

    /**
     * Components are assumed to be enabled and without a parent.
     */
    public Component() {
        enabled = true;
        gameObject = null;
    }

    /**
     * Set the GameObject that this component is for. Failing to properly set
     * the Parent may result in the Component not behaving properly, or at all.
     *
     * @param parent
     */
    public void SetParentGameObject(GameObject parent) {
        // Set the parent
        // Do not let the parent be changed after it is set
        if (gameObject == null) {
            this.gameObject = parent;
        } else {
            throw new IllegalArgumentException("Cannot reset the parent of a component");
        }
    }

    // A component can choose how to use Start and Update by overriding, or not use it at all
    /**
     * Defines what a Component should do when the game begins.
     */
    public void Start() {
    }
    /**
     * Defines what a Component does on each frame.
     */
    public void Update() {
    }

    /**
     * Returns a new Component that is identical to the original.
     *
     * @return
     */
    public abstract Component Clone();

}
