package klondike.gui;

import klondike.klondikeInterface.Pack;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Graphical representation of Pack.
 *
 * @author xivanc03
 */
public class GraphicPack implements MouseListener {
	// Constants
    private static final int CARD_WIDTH = 68;
    private static final int CARD_HEIGHT = 97;

    GraphicBoard board;
	JLayeredPane pane;
    Pack pack;

	/**
	 * Constructor.
	 * @param b parent graphic board
	 * @param p standard representation of pack
	 * @param x x coordinate position
	 * @param y y coordinate position
	 * @param q size quotient
	 * @param stacked pack is stacked
	 */
    public GraphicPack(GraphicBoard b, Pack p, int x, int y, double q, boolean stacked) {
    	board = b;
        pack = p;
        pane = new JLayeredPane();
		pane.addMouseListener(this);

		// All cards are on top of each other
        if (stacked) {
        	pane.setBounds((int)(x * q), (int)(y * q), (int)(CARD_WIDTH * q), (int)(CARD_HEIGHT * q));
			for (int i = 0; i < pack.size(); i++)
				pane.add((new GraphicCard(this, pack.get(i), 0, 0, q)).label, new Integer(i + 1));
		}
		// The cards are just partly overlapping (working packs)
        else {
        	int margin = (p.size() == 0) ? 0 : 25 * (p.size() - 1);
			pane.setBounds((int)(x * q), (int)(y * q), (int)(CARD_WIDTH * q), (int)((margin + CARD_HEIGHT) * q));
			for (int i = 0; i < pack.size(); i++)
				pane.add((new GraphicCard(this, pack.get(i), 0, (int)(25 * i * q), q)).label, new Integer(i + 1));
		}
    }

	/**
	 * Highlight active card.
	 * @param i index of card in pane
	 * @param q size quotient for width of highlighting border
	 */
	public void setActive(int i, double q) {
		// Create highlight
		JLabel highlight = new JLabel();
		highlight.setIcon(board.manager.getHighlight(Color.red, q));

		// Position it
    	JLabel card = (JLabel) pane.getComponent(i);
		highlight.setBounds(card.getX(), card.getY(), (int)(CARD_WIDTH * q), (int)(CARD_HEIGHT * q));

		// Show it
		pane.add(highlight, new Integer(pack.size() - i + 1));
	}

	/**
	 * Highlight hinted card.
	 * @param i index of card in pane
	 * @param q size quotient for size of highlighting border
	 */
	public void showHint(int i, double q) {
		// Create highlight
		JLabel highlight = new JLabel();
		highlight.setIcon(board.manager.getHighlight(Color.blue, q));

		// Position it
		if (pack.size() != 0) {
			JLabel card = (JLabel) pane.getComponent(i);
			highlight.setBounds(card.getX(), card.getY(), (int)(CARD_WIDTH * q), (int)(CARD_HEIGHT * q));
		}
		else {
			highlight.setBounds(0, 0, (int)(CARD_WIDTH * q), (int)(CARD_HEIGHT * q));
		}

		// Show it
		pane.add(highlight, new Integer(pack.size() - i + 1));
	}

	/**
	 * Handle click on card from this pack.
	 * @param card clicked card
	 */
	public void click(GraphicCard card) {
    	board.click(card, this);
	}

	/**
	 * Handle click on this pack when it is empty.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		board.click(null, this);
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
