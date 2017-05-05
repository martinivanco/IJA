package klondike.gui;

import klondike.board.*;
import klondike.klondikeInterface.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Graphical representation of Board.
 *
 * @author xandri03
 * @author xivanc03
 */
public class GraphicBoard implements MouseListener {
	// Graphic variables
	GraphicMain manager;
    JLayeredPane pane;
    double size;

    // Logic variables
    KlondikeBoard board;
    GraphicPack deck;
    GraphicPack source;
    GraphicPack[] targetPacks;
    GraphicPack[] workingPacks;

	/**
	 * Default constructor.
	 * @param m the parent main window
	 * @param load the name of the save file to load from. If null, a new board is created.
	 * @param q the size quotient of board
	 */
	public GraphicBoard(GraphicMain m, String load, double q) {
		// Set variables
		manager = m;
		size = q;

		// Set up pane
        pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension((int)(600 * size), (int)(600 * size)));
		pane.setOpaque(true);
        setBackground();

        // Set up packs
        board = new KlondikeBoard(load);
        addPacks();
    }

	/**
	 * Set the background of board pane.
	 */
	private void setBackground() {
        ImageIcon image = manager.getBackground(size);
        JLabel background = new JLabel("");
        background.setOpaque(true);
        background.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
        background.setIcon(image);
		background.addMouseListener(this);
        pane.add(background, new Integer(0));
    }

	/**
	 * Highlight active card.
	 */
	private void setActive() {
		int i;
		// Determine correct pack and call it
		switch (board.getActivePack().charAt(0)) {
			case 'S':
				source.setActive(0, size);
				break;
			case 'T':
				i = Character.getNumericValue(board.getActivePack().charAt(1));
				targetPacks[i].setActive(targetPacks[i].pack.size() - board.getActiveCard() - 1, size);
				break;
			case 'W':
				i = Character.getNumericValue(board.getActivePack().charAt(1));
				workingPacks[i].setActive(workingPacks[i].pack.size() - board.getActiveCard() - 1, size);
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
		deck = new GraphicPack(this, board.deck, 30, 40, size, true);
		pane.add(deck.pane, new Integer(1));

		// Add source pack
		source = new GraphicPack(this, board.sourcePack, 110, 40, size, true);
		pane.add(source.pane, new Integer(1));

		// Add target packs
		targetPacks = new GraphicPack[4];
		for (int i = 0; i < 4; i++) {
			targetPacks[i] = new GraphicPack(this, board.targetPacks[i], 247 + 82*i, 40, size, true);
			pane.add(targetPacks[i].pane, new Integer(1));
		}

		// Add working packs
		workingPacks = new GraphicPack[7];
		for (int i = 0; i < 7; i++) {
			workingPacks[i] = new GraphicPack(this, board.workingPacks[i], 30 + 78*i, 170, size, false);
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
	 * Set state of board.
	 * Used for resizing.
	 * @param state state of the board in string form
	 */
	protected void setState(String state) {
    	board.fromString(state);
    	redrawPacks();
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
	 * @param b board that is being saved used for default name
	 */
	public void save(int b) {
		// Get the file name from user
		String name = (String) JOptionPane.showInputDialog(pane, "Name:", "Save game", JOptionPane.QUESTION_MESSAGE, null, null, "board" + b);

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

	private GraphicPack findGraphicPack(Pack pack) {
		if (deck.pack == pack)
			return deck;

		if (source.pack == pack)
			return source;

		for (int i = 0; i < 4; i++) {
			if (targetPacks[i].pack == pack)
				return targetPacks[i];
		}

		for (int i = 0; i < 7; i++) {
			if (workingPacks[i].pack == pack)
				return workingPacks[i];
		}

		return null;
	}

	/**
	 * Show a hint of possible moves.
	 */
	public void hint() {
		redrawPacks();
		Hint h = board.hint();
		GraphicPack src = findGraphicPack(h.source);
		GraphicPack dst = findGraphicPack(h.target);
		src.showHint(h.source.size() - h.source.indexOf(h.card) - 1, size);
		dst.showHint(0, size);
	}

	/**
	 * Handle click on board. Deselect card if right click.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			board.resetActivity();
			redrawPacks();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
