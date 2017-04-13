package klondike.board;

import klondike.klondikeInterface.*;
import klondike.model.KlondikePackFactory;

/**
 * Klondike board.
 * @author xivanc03
 */
public class KlondikeBoard {
	
	public static void main(String argv[]) {
		
		PackFactory factory = new KlondikePackFactory();
		
		Pack pack1 = factory.createDeck("C1UC2UC3U");
		Pack pack2 = factory.createSourcePack("H0DHJDHQD");
		System.out.println("1:\t" + pack1);
		System.out.println("2:\t" + pack2);
		
		pack1.move(pack2, pack2.get(1));
		System.out.println("---------------------------");
		System.out.println("1:\t" + pack1);
		System.out.println("2:\t" + pack2);
	}
}
