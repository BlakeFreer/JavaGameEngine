// Blake Freer
// May 26, 2020
package MainPackage;

import javax.swing.*;

public class Main {

    public static JFrame frame;

    public static void main(String[] args) {

        frame = new JFrame("Game");

        // Setup frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        ContentScreen c = new ContentScreen();
        c.setFocusable(true);

        frame.add(c);
        frame.pack();
        frame.setVisible(true);

    }

}
