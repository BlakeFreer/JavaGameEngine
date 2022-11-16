// Blake Freer
// May 28, 2020
// Abstract class for defining visuals of an object
package GameObjects.Components.Renderers;

import GameObjects.Camera;
import java.awt.Graphics;

import GameObjects.Components.Component;
import Engine.GameMath.Vector2;

public abstract class Renderer extends Component {

    // Size and positions
    public double width;
    public double height;
    public Vector2 offset;
    protected Vector2 sizeOffset;
    protected Vector2 totalOffset;

    public int layerHeight;// Objects on lowest layers are drawn first

    public Renderer(double width, double height, Vector2 offset) {
        super();

        this.layerHeight = 0;
        
        this.width = width;
        this.height = height;
        
        this.offset = new Vector2(offset); // Use passed offset
        this.sizeOffset = new Vector2(width / 2, height / 2);

        // Modify offset to be based off of object's position
        this.totalOffset = Vector2.Subtract(this.offset, this.sizeOffset);

    }

    @Override
    public void Update() {
        // Add this component to the list of renderers to be displayed this frame
        gameData.AddRenderJob(this);
        
    }

    // Each renderer must implement Draw in some way
    public abstract void Draw(Graphics g, Camera camera);
}
