package klondike.gui;

import klondike.klondikeInterface.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Graphical representation of Card.
 *
 * @author xandri03
 * @author xivanc03
 */
public class GraphicCard implements MouseListener {
	GraphicPack pack;
    Card card;
	JLabel label;

	/**
	 * Constructor.
	 * @param p parent pack
	 * @param c standard representation of card
	 * @param x x coordinate position
	 * @param y y coordinate position
	 */
    public GraphicCard(GraphicPack p, Card c, int x, int y) {
    	pack = p;
        card = c;
        label = new JLabel("");
        label.addMouseListener(this);

        // Set image according to face
		ImageIcon image;
		if (card.isFacedUp())
			image = new ImageIcon("images/" + card.color() + "/" + card.value() + ".png");
		else
			image = new ImageIcon("images/back.png");

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
