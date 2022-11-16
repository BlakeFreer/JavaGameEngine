// Blake Freer
// May 30, 2020
// Handles the positioning and zoom of the viewer, but does not render the objects
package GameObjects;

import GameObjects.Components.Transform;
import Engine.GameMath.Vector2;

public class Camera extends GameObject {

    private final double screenWidth;
    private final double screenHeight;
    public final double aspectRatio; // Ratio of height over width (below 1 for landscape, above 1 for portrait)

    public final double viewportWidth; // How wide is the viewport?

    /**
     * Construct a new Camera.
     * @param name Name of the GameObject
     * @param parent Parent GameObject
     * @param viewWidth Horizontal viewport width of the camera
     */
    public Camera(String name, GameObject parent, double viewWidth) {
        super(name, parent);
        // Calculate aspect ratio
        screenWidth = (double) Transform.gameData.WIDTH;
        screenHeight = (double) Transform.gameData.HEIGHT;
        aspectRatio = screenHeight / screenWidth;

        this.viewportWidth = viewWidth;
    }

    /**
     * Converts a world position to a screen position.
     * @param worldPos The location of the point in game space.
     * @return The point on the screen where the point should appear.
     */
    public Vector2 WorldToPixel(Vector2 worldPos) {
        // Convert a world position to screen position
        // Find relative position of objcet to camera
        Vector2 relativePositionToCamera = Vector2.Subtract(worldPos, transform.GetGlobalPosition());
        // Scale to pizel size
        Vector2 pixelPos = Vector2.Scale(relativePositionToCamera, screenWidth / viewportWidth);
        // Add half of screen to center
        pixelPos.x += screenWidth / 2;
        pixelPos.y += screenHeight / 2;

        return pixelPos;
        
    }

    public Vector2 ResizeDimensions(Vector2 size) {
        // Resize dimensions based on viewport width
        // return 
        return Vector2.Scale(size, screenWidth / viewportWidth);
    }

}
