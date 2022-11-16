// Blake Freer
// May 29, 2020
// Runs the game by initializing and updating all objects and components
// Holds values that are shared and used amongst many scripts
package MainPackage;

import GameObjects.Camera;
import GameObjects.Components.Colliders.Collider;
import GameObjects.Components.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import GameObjects.Components.Renderers.Renderer;
import GameObjects.GameObject;
import GameObjects.Scene;
import java.awt.event.KeyEvent;

public class GameData {

    // Scene object that holds the game
    public Scene scene;
    public InputManager input;

    // EndFrame
    public ArrayList<GameObject> objectsToDestroy;

    // Graphics that are drawn to
    public Graphics graphics;
    public final int WIDTH = 1000; // 800
    public final int HEIGHT = 750; // 600

    Camera camera;

    // Timing
    private Thread thread;
    public boolean isRunning;
    
    // Framerate of the game
    public final int FPS = 60;
    
    
    public final long targetTime = 1000 / FPS;
    public final double deltaTime = 1.0 / FPS;

    // Graphics
    private ArrayList<Renderer> renderQueue;

    public void Start() {
        scene = new Scene();
        input = new InputManager();
        objectsToDestroy = new ArrayList<>();
    }

    public void Start(Runnable target) {

        scene.Start();

        // Start the thread
        isRunning = true;
        thread = new Thread(target);
        thread.start();

        renderQueue = new ArrayList<>();

    }

    public void Update() {
        // Update the scene
        scene.Update();
    }

    /**
     * Finish off the frame. Removes GameObjects that were destroyed.
     */
    public void EndFrame() {
        // Remove each object
        for (GameObject obj : objectsToDestroy) {

            if (obj.GetComponent(Collider.class) != null) {
                // Remove the collider from the scene, if the object had one
                Component.gameData.scene.RemoveCollider(obj.GetComponent(Collider.class));
            }

            // Remove from parent
            obj.parent.children.remove(obj);
        }
        // Reset list of objects to destroy
        objectsToDestroy.clear();
    }
    /**
     * Add a new object to destroy after the frame
     *
     * @param obj
     */
    public void Destroy(GameObject obj) {
        objectsToDestroy.add(obj);
    }

    public void AddRenderJob(Renderer r) {
        int layer = r.layerHeight; // Get layer height of renderer
        // Insertion Sort
        // Start at front and work backwards to find position to insert element
        // Array is sorted by increasing layer height

        for (int i = 0; i < renderQueue.size(); i++) {
            if (renderQueue.get(i) == null) {
                System.out.println("null  " + renderQueue.size());
            }
            if (renderQueue.get(i).layerHeight > layer) {
                // If the tested layer height is greater than the new layer height, insert the new one here
                renderQueue.add(i, r);
                return;
            }
        }
        // If the object wasn't lower than any other layers, add to the end
        renderQueue.add(renderQueue.size(), r);
    }

    public void Draw(Graphics g) {
        for (int i = 0; i < renderQueue.size(); i++) {
            // Draw each renderer to the screen
            renderQueue.get(i).Draw(g, camera);
        }
        // Remove all elements from the queue
        renderQueue.clear();
    }

    public void SetCamera(Camera cam) {
        // Set the camera of the game
        camera = cam;
    }

    // Keyboard input
    public void NewKeyEvent(KeyEvent e) {
        // Update the InputManager's map
        input.NewKeyEvent(e);
    }

}
