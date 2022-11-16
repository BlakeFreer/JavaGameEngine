// Blake Freer
// Jun 19, 2020
// Spawns coins in the world
package Assets.CustomScripts;

import Engine.GameMath.Mathf;
import Engine.GameMath.Vector2;
import GameObjects.Components.Colliders.CircleCollider;
import GameObjects.Components.Component;
import GameObjects.Components.Renderers.Animator;
import GameObjects.Components.Renderers.SpriteRenderer;
import GameObjects.GameObject;
import java.util.ArrayList;

public class CoinSpawn extends Component {

    ArrayList<GameObject> worldTiles;

    /**
     * Number of coins to spawn
     */
    int numCoins;
    /**
     * Tiles that coins can be spawned on. (On = above, not in the same place)
     */
    public String[] validTiles;

    public CoinSpawn(int coins, String... validTileNames) {
        this.numCoins = coins;
        this.validTiles = validTileNames;
    }

    @Override
    public void Start() {
        // Get tiles from parent
        worldTiles = gameObject.parent.children;

        worldTiles = CleanTiles(worldTiles, validTiles);

        SpawnAllCoins();
        
    }

    /**
     * Remove tiles from the list where coins cannot be spawned.
     * @param tiles List of tiles in the world.
     * @param validNames Names that are acceptable for spawning.
     */
    private ArrayList<GameObject> CleanTiles(ArrayList<GameObject> tiles, String[] validNames) {
        ArrayList<GameObject> tilesToKeep = new ArrayList<>();
        
        for(GameObject t : tiles){
            // If the current tile's name is valid, add it to the "keep" list
            if(Mathf.ArrayContains(validNames, t.name)){
                tilesToKeep.add(t);
            }
        }
        
        return tilesToKeep;
    }
    
    /**
     * Spawn coins on the remaining tiles
     */
    private void SpawnAllCoins(){
        
        for(int i = 0; i < numCoins; i++){
            if(worldTiles.isEmpty()){
                // Check to ensure there are remaining tiles to spawn coins
                throw new ArrayIndexOutOfBoundsException("Not enough tiles to spawn coins");
            }
            
            // Choose a random tile
            GameObject spawnTile = worldTiles.get(Mathf.RandInt(0, worldTiles.size()));
            
            // Spawn a coin
            SpawnCoin(Vector2.Add(spawnTile.transform.GetGlobalPosition(), Vector2.up()));
            
            // Remove the tile from the valid tiles list
            worldTiles.remove(spawnTile);
        }
        
    }
    private void SpawnCoin(Vector2 globalPosition){
        
        // Create the GameObject and set its position
        GameObject newCoin = new GameObject("Coin "+globalPosition.toString(), gameObject);
        newCoin.transform.SetGlobalPosition(globalPosition);
        
        // Renderer
        SpriteRenderer sr = new SpriteRenderer(0.7, 0.7, Vector2.zero());
        sr.SetSprite("src/Assets/Images/Items/Coin/coin1.png");
        sr.layerHeight = 5;
        newCoin.AddComponent(sr);
        
        // Animator
        Animator anim = new Animator();
        anim.AddAnimation("coinSpin", Animator.ReadAnimFile("src/Assets/Animations/CoinSpin.anim"));
        anim.AddAnimation("coinGet", Animator.ReadAnimFile("src/Assets/Animations/CoinGet.anim"));
        newCoin.AddComponent(anim);
        
        // Collider
        newCoin.AddComponent(new CircleCollider(0.5, new Vector2(-0.5, -0.5)));
        
        // Coin behaviour
        newCoin.AddComponent(new Coin());
        
        
    }    

    @Override
    public Component Clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
