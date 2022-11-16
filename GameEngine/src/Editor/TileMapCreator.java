// Blake Freer
// Jun 10, 2020
// Read a .map file and convert into a GameObject holding all of the individual tiles
// Use a .tileset file to extract tile sprites from a png tileset
package Editor;

import GameObjects.Components.Colliders.RectangleCollider;
import GameObjects.Components.Renderers.SpriteRenderer;
import GameObjects.GameObject;
import Engine.GameMath.Vector2;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class TileMapCreator {

    private final File tilesetData;
    private int[] resolution;
    private BufferedImage tilesetImage;
    private Map<String, GameObject> tiles;

    public TileMapCreator(File tilesetData) {
        // "tileset" is a .tileset file explaining how to create the individual tiles
        this.tilesetData = tilesetData;

        ReadData();

    }

    private void ReadData() {
        // Read the file to extract the resolution and image file
        this.tilesetImage = null;
        this.resolution = new int[]{0, 0};

        Scanner fileScanner;

        try {
            fileScanner = new Scanner(this.tilesetData);

            // Read the image path from the first line, removing "img="
            String imagePath = fileScanner.nextLine().substring(4);
            tilesetImage = ImageIO.read(new File(imagePath));

            // Read resolution from next line, removing "res="
            String resString = fileScanner.nextLine().substring(4);
            String[] resComps = resString.split("x");
            // Create resolution Vector2 by parsing components as doubles
            resolution = new int[]{Integer.parseInt(resComps[0]), Integer.parseInt(resComps[1])};

            // Create all of the tiles
            tiles = CreateTiles(fileScanner);

        } catch (Exception e) {
            // If file is not found, return an error message
            System.err.println("Error when creating tiles from .tileset file");
            e.printStackTrace();
            return;
        }

    }

    private HashMap<String, GameObject> CreateTiles(Scanner fileScanner) {
        // Use the data lines from the .tileset file to create the tiles
        HashMap<String, GameObject> tileObjs = new HashMap<>();

        while (!fileScanner.nextLine().equalsIgnoreCase("tiles")) {
            // Read file until past TILES line (fileScanner should already be here, but do it anyways)
        }

        String curLine = fileScanner.nextLine();

        while (!curLine.equalsIgnoreCase("END TILES")) {
            // Repeat tile creation until end of file is reached

            String[] tileLine = curLine.split(" ");

            // Separate data
            String tileCode = tileLine[0];  // Name of tile
            int row = Integer.parseInt(tileLine[1]); // Column that sprite is located in
            int column = Integer.parseInt(tileLine[2]);    // Row that sprite is located in
            boolean hasCollider = tileLine[3].equalsIgnoreCase("col");  // Does the tile have a collider?

            // Get sub-sprite from the tileset image
            // position is the row / column scaled by the resolution (the -1 is to make the first row start at 0, etc)
            BufferedImage sprite = tilesetImage.getSubimage((column - 1) * resolution[0], (row - 1) * resolution[1], resolution[0], resolution[1]);

            // Save the isolated sprites to ensure proper cropping
//            try {
//                File output = new File("src/Assets/Images/"+tileCode +".png");
//                ImageIO.write(sprite, "png", output);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            GameObject newTile = new GameObject(tileCode, null);
            // Create the sprite renderer component and add to object
            SpriteRenderer sr = new SpriteRenderer(1, 1, Vector2.zero());
            sr.SetSprite(sprite);
            newTile.AddComponent(sr);

            // Add a collider if necessary
            if (hasCollider) {
                RectangleCollider collider = new RectangleCollider(1, 1, Vector2.zero());
                newTile.AddComponent(collider);
            }

            // Add the new tile to the Map
            tileObjs.put(tileCode, newTile);

            curLine = fileScanner.nextLine();
        }
        // Return the new tile map
        return tileObjs;
    }

    public GameObject CreateTileMapGameObject(File tilemap, int renderLayer) {
        // Create a new GameObject that displays a tilemap
        // "tilemap" is a .map file holding the grid of tiles in the world, separated by |

        // Create empty parent object
        GameObject tileMapParent = new GameObject("TILEMAP", null);

        Scanner fileScanner;

        try {
            // Create scanner for reading .map file
            fileScanner = new Scanner(tilemap);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find .map file: " + tilemap.toString());
            e.printStackTrace();
            return null;
        }

        // Get file lines
        ArrayList<String[]> mapLines = new ArrayList<>();
        String curLine = fileScanner.nextLine();

        while (!curLine.equalsIgnoreCase("END")) {
            // Read all lines into the ArrayList
            mapLines.add(curLine.split("\\|")); // pipe character must be escaped to be used as a delimiter
            curLine = fileScanner.nextLine();
        }

        // Use lines to create the map
        for (int y = 0; y < mapLines.size(); y++) {
            // Iterate through lines
            for (int x = 0; x < mapLines.get(y).length; x++) {
                // Iterate through elements of a line
                String code = mapLines.get(y)[x];
                if (code.equals("   ")) {
                    code = "air";
                }
                // Get new tile
                GameObject tile = GameObject.Clone(tiles.get(code));
                tile.GetComponent(SpriteRenderer.class).layerHeight = renderLayer;
                // Place tile in parent at proper position
                tileMapParent.AddChild(tile);
                tile.transform.SetLocalPosition(new Vector2(x, -y));
            }
        }

        return tileMapParent;

    }
}
