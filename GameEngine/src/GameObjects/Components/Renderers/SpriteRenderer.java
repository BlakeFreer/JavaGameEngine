// Blake Freer
// May 30, 2020
// Draws a Sprite to the screen
package GameObjects.Components.Renderers;

import Engine.GameMath.Mathf;
import GameObjects.Camera;
import java.awt.Graphics;
import java.io.File;

import javax.imageio.ImageIO;

import Engine.GameMath.Vector2;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteRenderer extends Renderer {

    public BufferedImage sprite;

    public SpriteRenderer(double width, double height, Vector2 offset) {
        super(width, -height, offset);
        // Note that sprites must be flipped since the graphics2D is flipped vertically
    }

    public boolean SetSprite(String fileName) {
        // Set the image of this sprite
        // Returns a boolean stating success
        sprite = null;
        try {
            this.sprite = ImageIO.read(new File(fileName));
            return true;
        } catch (IOException e) {
            System.out.println("Error reading image " + fileName);
            System.err.print(e);
            return false;
        }
    }
    public boolean SetSprite(BufferedImage sprite){
        // Set the sprite with a buffered image
        this.sprite = sprite;
        return true;
    }

    @Override
    public void Draw(Graphics g, Camera cam) {
        // Adjust calculate world space position with offset
        Vector2 pos = Vector2.Add(gameObject.transform.GetGlobalPosition(), totalOffset);
        Vector2 size = new Vector2(width, height);
        
        // Adjust based on camera
        pos = cam.WorldToPixel(pos);
        size = cam.ResizeDimensions(size);
        
        if(pos.x + size.x < 0 || pos.x > gameData.WIDTH || pos.y < 0 || pos.y + size.y > gameData.HEIGHT){
            return;
        }
        
        // Draw the image to the graphics
        g.drawImage(sprite, Mathf.Floor(pos.x), Mathf.Ciel(pos.y), Mathf.Ciel(size.x), Mathf.Floor(size.y), null);
    }
    
    @Override
    public SpriteRenderer Clone(){
        // Return a copy of this renderer
        SpriteRenderer newSr = new SpriteRenderer(this.width, -this.height, new Vector2(this.offset));
        newSr.SetSprite(this.sprite);
        
        return newSr;
        
    }

}
