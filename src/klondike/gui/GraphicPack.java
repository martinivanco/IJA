package klondike.gui;

import klondike.klondikeInterface.Pack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Graphical representation of Pack.
 *
 * @author xandri03
 * @author xivanc03
 */
public class GraphicPack implements MouseListener {
    private static final int CARD_WIDTH = 68;
    private static final int CARD_HEIGHT = 97;

    GraphicBoard board;
    Pack pack;
	JLayeredPane pane;

	/**
	 * Constructor.
	 * @param b parent graphic board
	 * @param p standard representation of pack
	 * @param x x coordinate position
	 * @param y y coordinate position
	 * @param stacked pack is stacked
	 */
    public GraphicPack(GraphicBoard b, Pack p, int x, int y, boolean stacked) {
    	board = b;
        pack = p;
        pane = new JLayeredPane();
		pane.addMouseListener(this);

		// All cards are on top of each other
        if (stacked) {
        	pane.setBounds(x, y, CARD_WIDTH, CARD_HEIGHT);
			for (int i = 0; i < pack.size(); i++)
				pane.add((new GraphicCard(this, pack.get(i), 0, 0)).label, new Integer(i + 1));
		}
		// The cards are just partly overlapping (working packs)
        else {
			pane.setBounds(x, y, CARD_WIDTH, 25*(p.size() - 1) + CARD_HEIGHT);
			for (int i = 0; i < pack.size(); i++)
				pane.add((new GraphicCard(this, pack.get(i), 0, 25*i)).label, new Integer(i + 1));
		}
    }

    public void setActive(int i) {
    	JLabel card = (JLabel) pane.getComponent(i);
    	card.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
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
