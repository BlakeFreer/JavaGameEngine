// Blake Freer
// May 30, 2020
// Defines how to draw a rectangular shape on screen
package GameObjects.Components.Renderers;

import GameObjects.Camera;
import java.awt.Color;
import java.awt.Graphics;

import Engine.GameMath.Vector2;

public class RectangleRenderer extends Renderer {

    public Color color;
    public boolean fill;

    public RectangleRenderer(double width, double height, Color color, boolean fill) {
        super(width, height, Vector2.zero());

        this.color = color;
        this.fill = fill;
    }

    public RectangleRenderer(double width, double height, Color color, boolean fill, Vector2 offset) {
        super(width, height, offset);

        this.color = color;
        this.fill = fill;
    }

    @Override
    public void Draw(Graphics g, Camera cam) {
        // Get position and adjust to offset
        Vector2 pos = Vector2.Add(gameObject.transform.GetGlobalPosition(), totalOffset);
        Vector2 size = new Vector2(width, height);

        // Adjust positions based on camera
        pos = cam.WorldToPixel(pos);
        size = cam.ResizeDimensions(size);

        g.setColor(color);

        if (fill) {
            g.fillRect((int) pos.x, (int) pos.y, (int) size.x, (int) size.y);
        } else {
            g.drawRect((int) pos.x, (int) pos.y, (int) size.x, (int) size.y);
        }
    }
    
    @Override
    public RectangleRenderer Clone(){
        return new RectangleRenderer(this.width, this.height, this.color, this.fill, new Vector2(this.offset));
    }
}
