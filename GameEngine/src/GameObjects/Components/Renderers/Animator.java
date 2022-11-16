// Blake Freer
// Jun 17, 2020
// Animates a SpriteRender
package GameObjects.Components.Renderers;

import GameObjects.Components.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

public class Animator extends Component {

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private boolean isPlaying = false;
    private int curClipIndex = 0;
    ArrayList<AnimationClip> curAnim;

    private Map<String, ArrayList<AnimationClip>> clipsMap;

    private SpriteRenderer renderer;

    public Animator(){
        // Constructor
        clipsMap = new HashMap<>();
    }
    
    @Override
    public void Start() {
        // Get components
        this.renderer = gameObject.GetComponent(SpriteRenderer.class);
    }

    public void AddAnimation(String name, ArrayList<AnimationClip> newClip){
        // Add a new animation to the map
        clipsMap.put(name, newClip);
    }
    
    /**
     * Play an animation
     * @param clipName The name of the animation to play.
     */
    public void PlayOneShot(String clipName) {
        // Play the animation

        // Cancel previous
        executorService.shutdownNow();
        executorService = Executors.newSingleThreadScheduledExecutor();
        
        // Reset current clip counter
        curClipIndex = 0;
        curAnim = clipsMap.get(clipName);
        isPlaying = true;

        NextClip();
    }

    public void NextClip() {
        // Check if animation is complete
        if (curClipIndex == curAnim.size()) {
            isPlaying = false;
            return;
        }
        
        // Get current clip and set image of SpriteRenderer
        AnimationClip curAnimClip = curAnim.get(curClipIndex);
        renderer.SetSprite(curAnimClip.sprite);
        
        // Schedule next frame
        executorService.schedule(this::NextClip, curAnimClip.durationMillis, TimeUnit.MILLISECONDS);
        
        // Increment counter
        curClipIndex++;
    }

    public static ArrayList<AnimationClip> ReadAnimFile(String filePath) {
        // Read a .anim file into an animation clip array
        ArrayList<AnimationClip> clips = new ArrayList<>();
        
        // Create a scanner to read the file
        Scanner fileScanner;
        try{
            fileScanner = new Scanner(new File(filePath));
            
            String curLine = fileScanner.nextLine();
            
            // Read until end of file
            while(!curLine.equalsIgnoreCase("END")){
                // Separate line into elements
                String[] lineEles = curLine.split(" ");
                
                // Read image into BufferedImage and get duration as a long
                BufferedImage img = ImageIO.read(new File(lineEles[0]));
                long durationMillis = (long)(Double.parseDouble(lineEles[1]) * 1000);

                // Create the new clip
                clips.add(new AnimationClip(img, durationMillis));
                
                curLine = fileScanner.nextLine();
            }
            
        } catch (IOException e){
            System.out.println("Error reading from "+filePath);
            e.printStackTrace();
            return null;
        }
        
        // Return the completed list
        return clips;
    }
    
    // Accessor
    public boolean IsPlaying(){
        return isPlaying;
    }

    @Override
    public Component Clone() {
        // Replicate the Component
        Animator anim = new Animator();
        for(String k : clipsMap.keySet()){
            anim.AddAnimation(k, clipsMap.get(k));
        }
        return anim;
    }

}
