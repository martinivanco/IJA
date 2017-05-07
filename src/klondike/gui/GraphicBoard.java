package klondike.gui;

import klondike.board.*;
import klondike.klondikeInterface.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Graphical representation of Board.
 *
 * @author xivanc03
 */
public class GraphicBoard implements MouseListener {
	GraphicMain manager;
    JLayeredPane pane;
    double size;

    KlondikeBoard board;
    private GraphicPack deck;
    private GraphicPack source;
    private GraphicPack[] targetPacks;
    private GraphicPack[] workingPacks;

	/**
	 * Constructor.
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

    	if (board.isFinished())
			JOptionPane.showMessageDialog(pane, "You have won the game!\nThank you for playing!", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
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

	/**
	 * Find graphic pack corresponding to logic pack.
	 * @param pack logic model pack
	 * @return corresponding graphic pack
	 */
	private GraphicPack findGraphicPack(Pack pack) {
		// Check deck
		if (deck.pack == pack)
			return deck;

		// Check source pack
		if (source.pack == pack)
			return source;

		// Check target packs
		for (int i = 0; i < 4; i++) {
			if (targetPacks[i].pack == pack)
				return targetPacks[i];
		}

		// Check working packs
		for (int i = 0; i < 7; i++) {
			if (workingPacks[i].pack == pack)
				return workingPacks[i];
		}

		return null;
	}

	/**
	 * Highlight active card.
	 */
	private void setActive() {
		// Get active pack and index of active card in it
		GraphicPack active = findGraphicPack(board.getActivePack());
		if (active != null)
			active.setActive(active.pack.size() - active.pack.indexOf(board.getActiveCard()) - 1, size);
	}

	/**
	 * Show a hint of possible moves.
	 */
	public void hint() {
		// Remove possible last highlight
		redrawPacks();

		// Get hint
		Hint h = board.hint();
		if (h == null)
			return;

		// Get packs
		GraphicPack src = findGraphicPack(h.source);
		GraphicPack dst = findGraphicPack(h.target);

		// Highlight
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
