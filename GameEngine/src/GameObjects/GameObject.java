// Blake Freer
// May 27, 2020
// Container for all GameObjects
package GameObjects;

import Engine.GameMath.Vector2;
import java.util.ArrayList;

import GameObjects.Components.Component;
import GameObjects.Components.Transform;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameObject {
    
    static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    
    public String name;
    private String tag;
    public boolean enabled;     // Objects will not be updated if this is false

    public Transform transform;
    public ArrayList<Component> components;

    // Hierarchical Information
    public GameObject parent;
    public ArrayList<GameObject> children;

    public GameObject(String name, GameObject parent) {
        this.name = name;
        this.enabled = true;

        this.tag = "untagged";

        // Create components
        components = new ArrayList<>();
        transform = new Transform(Vector2.zero());
        AddComponent(transform);

        // Place in hierarchy
        this.children = new ArrayList<>();
        this.parent = parent;
        if (parent != null) {
            parent.AddChild(this);
        }
    }

    // Component adding / getting
    public void AddComponent(Component c) {
        // Check to ensure the object doesn't already have this component
        for (Component i : components) {
            if (i.getClass().isInstance(c)) {
                throw new IllegalArgumentException("Object already has component of type: " + c.getClass());
            }
        }
        components.add(c);
        c.SetParentGameObject(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T GetComponent(Class<T> componentType) {
        // Search through components for one of the passed type and return it
        for (Component c : components) {
            if (componentType.isInstance(c)) {
                // Since component is instanceOf T, cast to T before returning
                return (T) c;
            }
        }
        return null;
    }

    public void AddChild(GameObject child) {
        // Add a GameObject to the children ArrayList
        children.add(child);
        child.parent = this;
    }
    
    /**
     * Destroy an object after a delay.
     * @param objToDestroy
     * @param delaySeconds 
     */
    public static void Destroy(GameObject objToDestroy, double delaySeconds){
        executorService.schedule(new Callable(){
            @Override
            public Object call(){
                Destroy(objToDestroy);
                return null;
            }
        }, (long)(delaySeconds*1000), TimeUnit.MILLISECONDS);
    }
    
    /**
     * Remove an object from the list of children. It will not be updated after this.
     * @param objToDestroy 
     */
    public static void Destroy(GameObject objToDestroy){
        Component.gameData.Destroy(objToDestroy);
        
        // Destroy all children as well
        
        for(GameObject child : objToDestroy.children){
            Destroy(child);
        }
        
    }
    
    /**
     * Looks through the children and returns a GameObject if it matches the tag.
     * @param tag
     * @param parent GameObject to begin search at.
     * @return 
     */
    public static GameObject FindGameObjectWithTag(String tag, GameObject parent){
        //System.out.println("Searching in " +parent.name);
        
        // If this node has the tag, return it
        if(parent.tag.equals(tag)){
            return parent;
        }
        
        GameObject returnObj = null;
        
        // Recursively search through the children
        for(GameObject child : parent.children){
            returnObj = FindGameObjectWithTag(tag, child);
            // If found, return immediately
            if(returnObj != null){
                return returnObj;
            }
        }
        
        // Return object if found, otherwise null
        return returnObj;
        
    }

    public void Start() {
        // Run Start for all GameObjects and their Components
        // It is not necessary for an entity to be enabled to call Start, as this is only for initialization
        for (Component c : components) {
            c.Start();
        }
        for (GameObject ob : children) {
            ob.Start();
        }
    }

    public void Update() {
        // Update the components and children, as long as they are enabled
        // Notice that if a parent object is disabled, none of its children will be updated
        // This removes the need to enable/disable all descendants of an object, only the parent
        for (Component c : components) {
            if (c.enabled) {
                c.Update();
            }
        }

        for (GameObject ob : children) {
            if (ob.enabled) {
                ob.Update();
            }
        }
    }

    /**
     * Change the tag of this GameObject.
     * @param newTag The new tag of the GameObject
     * @param changeChildren Should all descendants also receive this tag?
     */
    public void SetTag(String newTag, boolean changeChildren) {
        tag = newTag;
        if (changeChildren) {
            for (GameObject c : children) {
                //Recursively change the tag of the children
                c.SetTag(newTag, true);
            }
        }
    }
    
    /**
     * @return The tag of this GameObject.
     */
    public String GetTag(){
        return tag;
    }

    public static GameObject Clone(GameObject objToClone) {
        // Copy over the properties of the objToClone to allow reuse of objects
        // Necessary for creating the tilemap, as tiles must be cloned

        GameObject clone = new GameObject(objToClone.name, objToClone.parent);
        clone.tag = objToClone.tag;

        for (Component comp : objToClone.components) {
            if (comp instanceof Transform) {
                // Trasforms must be handled separately, as a new GameObject already has a transform
                clone.transform = objToClone.transform.Clone();
                clone.transform.SetParentGameObject(clone);
                continue;
            }
            // Copy the components
            clone.AddComponent(comp.Clone());
        }
        for (GameObject child : objToClone.children) {
            // Copy all children and their components
            clone.AddChild(GameObject.Clone(child));
        }

        return clone;
    }

}
