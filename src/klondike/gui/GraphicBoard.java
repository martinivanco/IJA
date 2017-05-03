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
	 */
	public GraphicBoard(String id) {
		// Set up pane
        pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(600, 600));
		pane.setOpaque(true);

        setBackground();

        board = new KlondikeBoard(id, null);

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
}
