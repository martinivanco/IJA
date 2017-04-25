package klondike.board;

import klondike.klondikeInterface.*;
import klondike.model.KlondikePackFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Klondike board.
 * @author xivanc03
 */
public class KlondikeBoard {

	private PackFactory factory;
	private Pack deck;
	private Pack sourcePack;
	private Pack[] targetPacks;
	private Pack[] workingPacks;
	private Card activeCard;
	private Pack activePack;

	public KlondikeBoard() {
		newGame();
	}

	void newGame() {
		factory = new KlondikePackFactory();
		deck = factory.createDeck(null);
		sourcePack = factory.createSourcePack(null);

		targetPacks = new Pack[4];
		int i = 0;
		for (Card.Color color: Card.Color.values()) {
			targetPacks[i] = factory.createTargetPack(color, null);
			i += 1;
		}

		workingPacks = new Pack[7];
		for (i = 0; i < 7; i++) {
			workingPacks[i] = factory.createWorkingPack(null);
			workingPacks[i].move(deck, deck.get(deck.size() - i - 1));
			workingPacks[i].get(i).flipUp();
		}

		activeCard = null;
		activePack = null;
	}

	public void printPacks() {
		System.out.println("DECK: " + deck);
		System.out.println("SOURCE: " + sourcePack);

		int i = 0;
		for (Card.Color color: Card.Color.values()) {
			System.out.println("TARGET " + color + ": " + targetPacks[i]);
			i += 1;
		}

		for (i = 0; i < 7; i++) {
			System.out.println("WORKING " + i + ": " + workingPacks[i]);
		}
	}

	public void clickCard(Card clickedCard, Pack clickedPack) {

		if (clickedPack == deck) {
			sourcePack.push(deck.pop());
			return;
		}

		if (activeCard == null) {
			if ((clickedPack == sourcePack) && (clickedPack.get(clickedPack.size() - 1) != clickedCard))
				return;
			if (!clickedCard.isFacedUp())
				return;

			activeCard = clickedCard;
			activePack = clickedPack;
		}
		else {
			if (clickedPack == sourcePack)
				return;
			if ((clickedPack == activePack) && (clickedCard != activeCard))
				return;

			if (clickedCard != activeCard) {
				if (clickedPack.move(activePack, activeCard)) {
					if (activePack.size() > 0)
						activePack.get(activePack.size() - 1).flipUp();
				}
				else
					return;
			}

			activeCard = null;
			activePack = null;
		}
	}

	/*private boolean isWorkingPack(Pack pack) {
		for (Pack wPack: workingPacks) {
			if (wPack == pack)
				return true;
		}
		return false;
	}

	private boolean isTargetPack(Pack pack) {
		for (Pack tPack: targetPacks) {
			if (tPack == pack)
				return true;
		}
		return false;
	}*/

	public void textCommand() {
		Card clickedCard;
		Pack clickedPack;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String command;

		while (true) {
			printPacks();

			try {
				command = br.readLine();
			}
			catch (IOException e) {
				e.printStackTrace();
				return;
			}

			switch (command.charAt(0)) {
				case 'D':
					clickedPack = deck;
					break;
				case 'S':
					clickedPack = sourcePack;
					break;
				case 'T':
					clickedPack = targetPacks[Character.getNumericValue(command.charAt(1))];
					break;
				case 'W':
					clickedPack = workingPacks[Character.getNumericValue(command.charAt(1))];
					break;
				case 'Q':
					return;
				default:
					clickedPack = deck;
			}

			if (command.length() > 2) {
				clickedCard = clickedPack.get(Integer.parseInt(command.substring(2, command.length())));
			}
			else {
				clickedCard = null;
			}

			clickCard(clickedCard, clickedPack);
		}

	}

}
