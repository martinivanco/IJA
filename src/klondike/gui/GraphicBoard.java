package klondike.gui;

import klondike.board.KlondikeBoard;

import javax.swing.*;
import java.awt.*;

/**
 * Graphical representation of Board.
 *
 * @author xandri03
 * @author xivanc03
 */
public class GraphicBoard {
    JLayeredPane pane;
    KlondikeBoard board;
    GraphicPack deck;
    GraphicPack source;
    GraphicPack[] targetPacks;
    GraphicPack[] workingPacks;

	/**
	 * Constructor.
	 * @param id the id of the board used for autosave
	 * @param load the name of the save file to load from. If null, a new board is created.
	 */
	public GraphicBoard(String id, String load) {
		// Set up pane
        pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(600, 600));
		pane.setOpaque(true);

        setBackground();

        board = new KlondikeBoard(id, load);

        addPacks();
    }

	/**
	 * Set the background of board pane.
	 */
	private void setBackground() {
        ImageIcon image = new ImageIcon("images/board.png");
        JLabel background = new JLabel("");
        background.setOpaque(true);
        background.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
        background.setIcon(image);
        pane.add(background, new Integer(0));
    }

    private void setActive() {
		int i;
		switch (board.getActivePack().charAt(0)) {
			case 'S':
				source.setActive(0);
				break;
			case 'T':
				i = Character.getNumericValue(board.getActivePack().charAt(1));
				targetPacks[i].setActive(targetPacks[i].pack.size() - board.getActiveCard() - 1);
				break;
			case 'W':
				i = Character.getNumericValue(board.getActivePack().charAt(1));
				workingPacks[i].setActive(workingPacks[i].pack.size() - board.getActiveCard() - 1);
				break;
			default:
				break;
		}
	}

	/**
	 * Add packs to the board pane.
	 */
	private void addPacks() {
		// Add deck
		deck = new GraphicPack(this, board.deck, 30, 40, true);
		pane.add(deck.pane, new Integer(1));

		// Add source pack
		source = new GraphicPack(this, board.sourcePack, 110, 40, true);
		pane.add(source.pane, new Integer(1));

		// Add target packs
		targetPacks = new GraphicPack[4];
		for (int i = 0; i < 4; i++) {
			targetPacks[i] = new GraphicPack(this, board.targetPacks[i], 247 + 82*i, 40, true);
			pane.add(targetPacks[i].pane, new Integer(4));
		}

		// Add working packs
		workingPacks = new GraphicPack[7];
		for (int i = 0; i < 7; i++) {
			workingPacks[i] = new GraphicPack(this, board.workingPacks[i], 30 + 78*i, 170, false);
			pane.add(workingPacks[i].pane, new Integer(1));
		}

		setActive();
	}

	/**
	 * Redraw the packs on board.
	 */
    private void redrawPacks() {
    	// Remove all packs
    	for (int i = 0; i < 13; i++)
        	pane.remove(0);

        addPacks();
        pane.repaint();
    }

	/**
	 * Handle click on card or pack.
	 * @param card clicked card
	 * @param pack clicked pack
	 */
	public void click(GraphicCard card, GraphicPack pack) {
		// Pass message to KlondikeBoard
    	if (card == null)
    		board.clickCard(null, pack.pack);
    	else
    		board.clickCard(card.card, pack.pack);

    	redrawPacks();
	}

	/**
	 * Save the game.
	 */
	public void save() {
		// Get the file name from user
		String name = (String) JOptionPane.showInputDialog(pane, "Name:", "Save game", JOptionPane.QUESTION_MESSAGE, null, null, "board1");

		// Check if file exists
		if (board.saveFileExists(name)) {
			Object[] options = {"Yes", "No"};
			int ok = JOptionPane.showOptionDialog(pane, "There is already a save file named \"" + name + "\".\nDo you want to overwrite it?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (ok == JOptionPane.NO_OPTION)
				return;
		}

		board.saveGame(name);
	}

	/**
	 * Undo last move.
	 */
	public void undo() {
		board.undo();
		redrawPacks();
	}

	/**
	 * Show a hint of possible moves.
	 */
	public void hint() {
		//TODO
	}
}
