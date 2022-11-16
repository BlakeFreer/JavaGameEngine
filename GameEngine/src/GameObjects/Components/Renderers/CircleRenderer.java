// Blake Freer
// May 28, 2020
// Defines how to draw a circle to the screen
package GameObjects.Components.Renderers;

import GameObjects.Camera;
import java.awt.Color;
import java.awt.Graphics;

import Engine.GameMath.Vector2;

public class CircleRenderer extends Renderer {

    public Color color;     // Color
    public boolean fill;    // True: Fill, False: Outline

    public CircleRenderer(double radius, Color color, boolean fill){
        // Call constructor for renderer with zero for offset
    	super(radius*2, radius*2, Vector2.zero());
        
        this.color = color;
        this.fill = fill;
    }
    public CircleRenderer(double radius, Color color, boolean fill, Vector2 offset){
        // Call constructor for renderer using passed offset value
    	super(radius*2, radius*2, offset);
        
        this.color = color;
        this.fill = fill;
    }
    
    @Override
    public void Draw(Graphics g, Camera cam) {
        // Adjust calculate world space position with offset
        Vector2 pos = Vector2.Add(gameObject.transform.GetGlobalPosition(), totalOffset);
        Vector2 size = new Vector2(width, height);
        
        // Adjust based on camera
        size = cam.ResizeDimensions(size);
        pos = cam.WorldToPixel(pos);
        
        g.setColor(color);
        
        if (fill) {
            g.fillOval((int) pos.x, (int) pos.y, (int)size.x, (int)size.y);
        } else {
            g.drawOval((int) pos.x, (int) pos.y, (int)size.x, (int)size.y);
        }
    }
    
    @Override
    public CircleRenderer Clone(){
        return new CircleRenderer(this.width/2, this.color, this.fill, new Vector2(this.offset));
    }

}
