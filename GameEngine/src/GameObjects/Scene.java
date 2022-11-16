// Blake Freer
// May 28, 2020
// Holds and creates all of the objects in the game
// Only difference from a regular GameObject is it doesn't have a parent
package GameObjects;

import Assets.CustomScripts.*;
import Assets.CustomScripts.LagueController.Controller2D;
import Assets.CustomScripts.LagueController.Player;
import Editor.TileMapCreator;
import java.awt.Color;

import GameObjects.Components.Renderers.*;
import GameObjects.Components.Colliders.*;
import MainPackage.GameData;
import Engine.GameMath.Vector2;
import java.io.File;
import java.util.ArrayList;

public class Scene extends GameObject {

    static GameData gameData = MainPackage.ContentScreen.gameData;

    private final ArrayList<Collider> sceneColliders;

    public Scene() {
        // Create the Scene GameObject
        super("Scene", null);
        sceneColliders = new ArrayList<>();

        // Create the objects
        
        // Player
        // GameObject
        GameObject player = new GameObject("Player", this);
        player.transform.SetLocalPosition(new Vector2(0, 0));
        player.SetTag("player", false);

        // Renderer
        SpriteRenderer player_sr = new SpriteRenderer(1, 1, Vector2.zero());
        player_sr.SetSprite("src/Assets/Images/FurryBall/furry_ball1.png");
        player_sr.layerHeight = 4;
        player.AddComponent(player_sr);
        player.AddComponent(new RectangleCollider(0.8, 0.8, Vector2.zero()));
        
//        RectangleRenderer prr = new RectangleRenderer(1, 0.8, Color.red, true, Vector2.zero());
//        prr.layerHeight = 10;
//        player.AddComponent(prr);

        // Custom Controller Components
        player.AddComponent(new Player());
        Controller2D player_c2d = new Controller2D();
        player_c2d.collisionMask = new String[]{"obstacle"};
        player.AddComponent(player_c2d);

        // Animator
        Animator player_anim = new Animator();
        player_anim.AddAnimation("idle", Animator.ReadAnimFile("src/Assets/Animations/SlimeIdle.anim"));
        player_anim.AddAnimation("jump", Animator.ReadAnimFile("src/Assets/Animations/SlimeJump.anim"));
        player_anim.AddAnimation("left", Animator.ReadAnimFile("src/Assets/Animations/SlimeLeft.anim"));
        player_anim.AddAnimation("right", Animator.ReadAnimFile("src/Assets/Animations/SlimeRight.anim"));
        player_anim.AddAnimation("die", Animator.ReadAnimFile("src/Assets/Animations/SlimeDie.anim"));
        player.AddComponent(player_anim);
        
        // Camera
        Camera camera = new Camera("Camera", player, 14);
        camera.transform.SetLocalPosition(new Vector2(0, 0)); // Camera is centered over player
        camera.SetTag("camera", false);

        gameData.SetCamera(camera); // Set game's camera to this camera

        // Background
        GameObject background = new GameObject("Back", camera);
        background.transform.SetLocalPosition(new Vector2(0, 0));

        RectangleRenderer background_renderer = new RectangleRenderer(20, 20, new Color(176, 223, 229), true);
        background_renderer.layerHeight = 0;
        background.AddComponent(background_renderer);

        // Ball - Shows where 0,0 is
        GameObject ball = new GameObject("Ball", this);
        ball.transform.SetLocalPosition(new Vector2(0, 0));
        //ball.AddComponent(new CircleCollider(3, Vector2.zero));
        CircleRenderer ball_cr = new CircleRenderer(0.1, Color.yellow, true, Vector2.zero());
        ball_cr.layerHeight = 5;
        //ball.AddComponent(ball_cr);

        // TileMap (Level)
        TileMapCreator tileCreator = new TileMapCreator(new File("src/Assets/TilemapData/sand.tileset"));
        GameObject levelTilesParent = tileCreator.CreateTileMapGameObject(new File("src/Assets/TileMapData/TileMap1.map"), 1);
        this.AddChild(levelTilesParent);
        levelTilesParent.transform.SetLocalPosition(new Vector2(-10, 14));
        levelTilesParent.SetTag("obstacle", true);
        
        // Coin spawner
        GameObject coinSpawner = new GameObject("Coin Spawner", levelTilesParent);
        coinSpawner.AddComponent(new CoinSpawn(25, "gtm", "gtl", "gtr", "hll", "hmm", "hrr", "vtt", "one"));
        
        
        // Cannon 1
        GameObject cannon1 = new GameObject("Cannon1", levelTilesParent);
        cannon1.transform.SetLocalPosition(new Vector2(13.5, -8.75));
        
        SpriteRenderer cannon1_renderer = new SpriteRenderer(1.5, 1.5, Vector2.zero());
        cannon1_renderer.SetSprite("src/Assets/Images/Items/Cannon/CannonR1.png");
        cannon1_renderer.layerHeight = 2;
        cannon1.AddComponent(cannon1_renderer);

        Animator cannon1_anim = new Animator();
        cannon1_anim.AddAnimation("fire", Animator.ReadAnimFile("src/Assets/Animations/CannonFireRight.anim"));
        cannon1.AddComponent(cannon1_anim);
        
        CannonScript c1_scr = new CannonScript();
        c1_scr.facingRight = true;
        cannon1.AddComponent(c1_scr);
        
        // Cannon 2
        GameObject cannon2 = new GameObject("Cannon2", levelTilesParent);
        cannon2.transform.SetLocalPosition(new Vector2(31, -13.75));
        
        SpriteRenderer cannon2_renderer = new SpriteRenderer(1.5, 1.5, Vector2.zero());
        cannon2_renderer.SetSprite("src/Assets/Images/Items/Cannon/CannonL1.png");
        cannon2_renderer.layerHeight = 2;
        cannon2.AddComponent(cannon2_renderer);

        Animator cannon2_anim = new Animator();
        cannon2_anim.AddAnimation("fire", Animator.ReadAnimFile("src/Assets/Animations/CannonFireLeft.anim"));
        cannon2.AddComponent(cannon2_anim);
        
        CannonScript c2_scr = new CannonScript();
        c2_scr.facingRight = false;
        c2_scr.cooldownSeconds = 1;
        cannon2.AddComponent(c2_scr);
        
        

    }

    
    public void AddCollider(Collider newCollider) {
        // Add a new collider to the scene's list.
        sceneColliders.add(newCollider);
    }

    public void RemoveCollider(Collider collider) {
        // Remove a collider from the scene.
        sceneColliders.remove(collider);
    }

    public ArrayList<Collider> GetColliders() {
        // Get all colliders in the scene.
        return sceneColliders;
    }

}
