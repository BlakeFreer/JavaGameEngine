// Blake Freer
// May 27, 2020
// Holds the components on the screen
package MainPackage;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class ContentScreen extends JPanel implements Runnable, KeyListener {

    // Create the GameData
    public final static GameData gameData = new GameData();

    public ContentScreen() {
        // Setup panel
        gameData.Start();

        setPreferredSize(new Dimension(gameData.WIDTH, gameData.HEIGHT));

        addKeyListener(this);

        gameData.Start(this);
    }

    @Override
    public void run() {
        // Timing variables
        long start, elapsed, wait;
        while (gameData.isRunning) {
            start = System.nanoTime();

            gameData.Update(); // Update the game
            repaint();  // Update game and redraw screen
            gameData.EndFrame();
            
            elapsed = System.nanoTime() - start;
            wait = gameData.targetTime - elapsed / 1000000;

            if (wait <= 0) {
                wait = 5;
            }
            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(1, -1);
        g2d.translate(0, -getHeight());
        // Display the game
        gameData.Draw(g);

    }

    // Input
    // Pass to gameData, as all components have a reference to gameData
    @Override
    public void keyTyped(KeyEvent e) {
        gameData.NewKeyEvent(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameData.NewKeyEvent(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gameData.NewKeyEvent(e);
    }

}
