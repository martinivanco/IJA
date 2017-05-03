package klondike.gui;

import javax.swing.*;
import java.awt.event.*;

/**
 * Main GUI implementation.
 *
 * @author xandri03
 * @author xivanc03
 */
public class GraphicMain implements ActionListener {
	JFrame mainWindow;
	JMenuBar menuBar;
	GraphicBoard[] boards;

	/**
	 * Constructor.
	 */
	public GraphicMain() {
		// Create main window and add a board to it
		mainWindow = new JFrame("Klondike");
		boards = new GraphicBoard[4];
		boards[0] = new GraphicBoard("board1", null);
		mainWindow.getContentPane().add(boards[0].pane);

		// Create menu
		menuBar = new JMenuBar();
		menuSetup("Board 1");
		mainWindow.setJMenuBar(menuBar);

		// Set up and show the window
		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainWindow.pack();
		mainWindow.setVisible(true);
		mainWindow.setResizable(false);
	}

	/**
	 * Setup menu.
	 * @param board menu name - should correspond to board
	 */
    private void menuSetup(String board) {
    	// Create menu and add it to menu bar
		JMenu menu = new JMenu(board);
		menu.addActionListener(this);
		menuBar.add(menu);

		// Create menu items
		String[] titles = {"Save", "Load", "Undo", "Hint"};
		for (String s: titles) {
			JMenuItem menuItem = new JMenuItem(s);
			menuItem.addActionListener(this);
			menu.add(menuItem);
		}
		//menuBar.revalidate();
	}

	/**
	 * Load game.
	 */
	public void load() {
		// Get the file name from user
		String name = (String) JOptionPane.showInputDialog(mainWindow, "Name:", "Load game", JOptionPane.QUESTION_MESSAGE, null, null, "board1");

		// Check if file exists
		if (!boards[0].board.saveFileExists(name)) {
			JOptionPane.showMessageDialog(mainWindow, "The save file \"" + name + "\" does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Reset the window
		mainWindow.getContentPane().removeAll();
		boards[0] = new GraphicBoard("board1", name);
		mainWindow.getContentPane().add(boards[0].pane);
		mainWindow.getContentPane().revalidate();
		mainWindow.repaint();
	}

	/**
	 * Handle menu item click.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "Save":
				boards[0].save();
				break;
			case "Load":
				load();
				break;
			case "Undo":
				boards[0].undo();
				break;
			case "Hint":
				boards[0].hint();
				break;
		}
	}

	/**
	 * Launch program.
	 * @param args arguments of program
	 */
	public static void main(String[] args) {
		GraphicMain klondike = new GraphicMain();
	}
}
