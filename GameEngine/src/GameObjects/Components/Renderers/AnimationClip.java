// Blake Freer
// Jun 17, 2020
// Holds information about a single animation frame

package GameObjects.Components.Renderers;

import java.awt.image.BufferedImage;

/**
 * Holds information about a single animation frame.
 * @author bkfre
 */
public class AnimationClip {
    
    public BufferedImage sprite;
    public long durationMillis;
    
    public AnimationClip(BufferedImage sprite, long durationMillis){
        // Constructor
        this.sprite = sprite;
        this.durationMillis = durationMillis;
    }
    
}
