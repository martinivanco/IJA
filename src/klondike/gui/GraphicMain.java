package klondike.gui;

import javax.swing.*;

/**
 * Main GUI implementation.
 *
 * @author xandri03
 * @author xivanc03
 */
public class GraphicMain {
    /**
     * Main window setup.
     * @param args arguments of program
     */
    public static void main(String[] args) {
        JFrame mainWindow = new JFrame("Klondike");
        GraphicBoard board1 = new GraphicBoard("board1");

        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setContentPane(board1.pane);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }
}
