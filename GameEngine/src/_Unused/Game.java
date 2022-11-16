// Blake Freer
// May 27, 2020
// Holds the game objects

// *** OBSELETE ***
// All functionality is now handled by GameData

package _Unused;

import java.awt.Graphics;

import GameObjects.Scene;
import MainPackage.GameData;

public class Game {

    // Everything is held within the hierarchy
    public Scene scene;
    public final static GameData gameData = new GameData();

    public Game() {
        System.out.println("bad boy");
        scene = new Scene();
    }

    public void Update() {
        // This will update the hierarchy and everything beneath it recursively
        scene.Update();
    }

    public void Draw(Graphics g) {
        gameData.Draw(g);
    }

}
