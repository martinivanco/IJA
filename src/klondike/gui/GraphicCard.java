package klondike.gui;

import klondike.klondikeInterface.Card;

import javax.swing.*;
import java.awt.event.*;

/**
 * Graphical representation of Card.
 *
 * @author xivanc03
 */
public class GraphicCard implements MouseListener {
	GraphicPack pack;
	JLabel label;
    Card card;

	/**
	 * Constructor.
	 * @param p parent pack
	 * @param c standard representation of card
	 * @param x x coordinate position
	 * @param y y coordinate position
	 * @param q size quotient
	 */
    public GraphicCard(GraphicPack p, Card c, int x, int y, double q) {
    	pack = p;
        card = c;
        label = new JLabel("");
        label.addMouseListener(this);

        // Set image according to face
		ImageIcon image;
		if (card.isFacedUp())
			image = pack.board.manager.getIcon(card, q);
		else
			image = pack.board.manager.getCardBack(q);

		label.setIcon(image);
		label.setBounds(x, y, image.getIconWidth(), image.getIconHeight());
    }

	/**
	 * Handle click on this card.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		pack.click(this);
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
