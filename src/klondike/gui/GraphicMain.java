package klondike.gui;

import klondike.klondikeInterface.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main GUI implementation.
 *
 * @author xandri03
 * @author xivanc03
 */
public class GraphicMain implements ActionListener {
	// Constants
	private static final int LRI_WIDTH = 34;
	private static final int LRI_HEIGHT = 49;
	// Frame
	private JFrame mainWindow;
	private JMenuBar menuBar;
	// Boards
	private GraphicBoard[] boards;
	private int numOfBoards;
	// Images
	ImageIcon[] highres;
	ImageIcon[] lowres;

	/**
	 * Constructor.
	 */
	public GraphicMain() {
		loadIcons();

		// Create main window and add first board
		mainWindow = new JFrame("Klondike");
		boards = new GraphicBoard[4];
		boards[0] = new GraphicBoard(this, null, 1);
		numOfBoards = 1;
		mainWindow.getContentPane().setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		mainWindow.getContentPane().add(boards[0].pane);

		// Create menu
		menuBar = new JMenuBar();
		menuSetup();
		mainWindow.setJMenuBar(menuBar);

		// Set up and show the window
		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainWindow.pack();
		mainWindow.setVisible(true);
		mainWindow.setResizable(false);
	}

	/**
	 * Setup menu according to current state of window.
	 */
    private void menuSetup() {
    	menuBar.removeAll();

    	// Create menu for open boards
		int i;
		for (i = 1; i <= numOfBoards; i++) {
			// Create menu and add it to menu bar
			JMenu menu = new JMenu("Board " + i);
			menuBar.add(menu);

			// Create menu items
			String[] titles = {"New", "Save", "Load", "Undo", "Hint", "Close"};
			for (String s: titles) {
				JMenuItem menuItem = new JMenuItem(s);
				menuItem.addActionListener(this);
				menuItem.setActionCommand(s + i);
				menu.add(menuItem);
			}
		}

		//Create menu for possible new board
		if (numOfBoards < 4) {
			JMenu menu = new JMenu("Board " + i);
			menuBar.add(menu);

			//Create menu items
			String[] titles = {"New", "Load"};
			for (String s: titles) {
				JMenuItem menuItem = new JMenuItem(s);
				menuItem.addActionListener(this);
				menuItem.setActionCommand(s + i);
				menu.add(menuItem);
			}
		}
	}

	/**
	 * Load all icons and images.
	 */
	private void loadIcons() {
		// Create the arrays
		highres = new ImageIcon[54];
		lowres = new ImageIcon[54];

		// Get background
		highres[0] = new ImageIcon("images/board.png");
		lowres[0] = new ImageIcon(highres[0].getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));

		// Get card back
		highres[1] = new ImageIcon("images/back.png");
		lowres[1] = new ImageIcon(highres[1].getImage().getScaledInstance(LRI_WIDTH, LRI_HEIGHT, Image.SCALE_DEFAULT));

		// Get cards faces
		int c = 1;
		for (Card.Color color: Card.Color.values()) {
			for (int i = 1; i <= 13; i++) {
				highres[c + i] = new ImageIcon("images/" + color + "/" + i + ".png");
				lowres[c + i] = new ImageIcon(highres[c + i].getImage().getScaledInstance(LRI_WIDTH, LRI_HEIGHT, Image.SCALE_SMOOTH));
			}
			c += 13;
		}
	}

	/**
	 * Get background image.
	 * @param size size quotient used to determine the resolution to be used
	 * @return background image
	 */
	public ImageIcon getBackground(double size) {
		if (size == 1) {
			return highres[0];
		}
		else {
			return lowres[0];
		}
	}

	/**
	 * Get back face card icon.
	 * @param size size quotient used to determine the resolution to be used
	 * @return back face card icon
	 */
	public ImageIcon getCardBack(double size) {
		if (size == 1) {
			return highres[1];
		}
		else {
			return lowres[1];
		}
	}

	/**
	 * Get icon of a card.
	 * @param card the card of which icon shall be returned
	 * @param size size quotient used to determine the resolution to be used
	 * @return icon of the given card
	 */
	public ImageIcon getIcon(Card card, double size) {
		// Get index
		int i = 1;
		switch (card.color()) {
			case SPADES:
				break;
			case DIAMONDS:
				i += 13;
				break;
			case CLUBS:
				i += 26;
				break;
			case HEARTS:
				i += 39;
				break;
			default:
				return null;
		}
		i += card.value();

		// Return right resolution
		if (size == 1) {
			return highres[i];
		}
		else {
			return lowres[i];
		}
	}

	/**
	 * Redraw boards in window.
	 */
	private void redrawBoards() {
		mainWindow.getContentPane().removeAll();
		for (int i = 0; i < numOfBoards; i++) {
			mainWindow.getContentPane().add(boards[i].pane);
			//mainWindow.getContentPane().getComponent(i).setBounds((i%2)*300, (i/2)*300);
		}
		mainWindow.getContentPane().revalidate();
		mainWindow.repaint();
	}

	/**
	 * Resize board 1.
	 * Used when switching from multiple to one board or vice versa.
	 */
	private void resizeBoard() {
		String state = boards[0].board.toString();
		if (numOfBoards == 1) {
			boards[0] = new GraphicBoard(this, null, 1);
			boards[0].setState(state);
		}
		else {
			boards[0] = new GraphicBoard(this, null, 0.5);
			boards[0].setState(state);
		}
	}

	/**
	 * Create new game.
	 * @param b board number on which create the new game
	 */
	public void newGame(int b) {
		// Get user to approve create
		Object[] options = {"Yes", "No"};
		int ok = JOptionPane.showOptionDialog(mainWindow, "Do you really want to create a new game on board " + b + "?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (ok == JOptionPane.NO_OPTION)
			return;

		// Create new game
		if (numOfBoards > 1) {
			boards[b - 1] = new GraphicBoard(this, null, 0.5);
			if (b > numOfBoards)
				numOfBoards += 1;
		}
		else {
			if (b != 1) {
				boards[b - 1] = new GraphicBoard(this, null, 0.5);
				numOfBoards += 1;
				resizeBoard();
			}
			else
				boards[b - 1] = new GraphicBoard(this, null, 1);
		}

		redrawBoards();
		menuSetup();
	}

	/**
	 * Load game.
	 * @param b board number on which to load the game
	 */
	public void loadGame(int b) {
		// Get the file name from user
		String name = (String) JOptionPane.showInputDialog(mainWindow, "Name:", "Load game", JOptionPane.QUESTION_MESSAGE, null, null, "board" + b);
		if (name == null)
			return;

		// Check if file exists
		if (!boards[numOfBoards - 1].board.saveFileExists(name)) {
			JOptionPane.showMessageDialog(mainWindow, "The save file \"" + name + "\" does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Load the game
		if (numOfBoards > 1) {
			boards[b - 1] = new GraphicBoard(this, name, 0.5);
			if (b > numOfBoards)
				numOfBoards += 1;
		}
		else {
			if (b != 1) {
				boards[b - 1] = new GraphicBoard(this, name, 0.5);
				numOfBoards += 1;
				resizeBoard();
			}
			else
				boards[b - 1] = new GraphicBoard(this, name, 1);
		}

		redrawBoards();
		menuSetup();
	}

	/**
	 * Close game.
	 * @param b board number on which to close the game
	 */
	public void closeGame(int b) {
		// Get user to approve close
		Object[] options = {"Yes", "No"};
		int ok = JOptionPane.showOptionDialog(mainWindow, "Do you really want to close the game on board " + b + "?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (ok == JOptionPane.NO_OPTION)
			return;

		// Check if last game
		if (numOfBoards <= 1) {
			mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));
		}

		// Close the game
		for (int i = b; i < numOfBoards; i++) {
			boards[i - 1] = boards[i];
		}
		boards[--numOfBoards] = null;

		// Redraw boards
		if (numOfBoards == 1)
			resizeBoard();
		redrawBoards();
		menuSetup();
	}

	/**
	 * Handle menu item click.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		// Get menu item and board and call the right function
		switch (s.substring(0, 3)) {
			case "New":
				newGame(Character.getNumericValue(s.charAt(3)));
				break;
			case "Sav":
				boards[Character.getNumericValue(s.charAt(4)) - 1].save(Character.getNumericValue(s.charAt(4)));
				break;
			case "Loa":
				loadGame(Character.getNumericValue(s.charAt(4)));
				break;
			case "Und":
				boards[Character.getNumericValue(s.charAt(4)) - 1].undo();
				break;
			case "Hin":
				boards[Character.getNumericValue(s.charAt(4)) - 1].hint();
				break;
			case "Clo":
				closeGame(Character.getNumericValue(s.charAt(5)));
				break;
			default:
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
