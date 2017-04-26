package klondike.board;

import klondike.klondikeInterface.*;
import klondike.model.KlondikePackFactory;

import java.io.*;

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

	/**
	 * Constructor.
	 * @param load the name of the save file to load from. If null, a new game is created.
	 */
	public KlondikeBoard(String load) {
		if (load == null)
			newGame();
		else
			loadGame(load);
	}

	/**
	 * Create new game.
	 * All packs are initialized appropriately.
	 */
	public void newGame() {
		// Create factory, deck and source pack
		factory = new KlondikePackFactory();
		deck = factory.createDeck(null);
		sourcePack = factory.createSourcePack(null);

		// Create target packs of each color
		targetPacks = new Pack[4];
		int i = 0;
		for (Card.Color color: Card.Color.values()) {
			targetPacks[i] = factory.createTargetPack(color, null);
			i += 1;
		}

		// Create 7 working packs and deal cards to them from deck
		workingPacks = new Pack[7];
		for (i = 0; i < 7; i++) {
			workingPacks[i] = factory.createWorkingPack(null);
			Card tmp = deck.get(deck.size() - i - 1);
			if (tmp == null)
				System.out.println("Shit");
			workingPacks[i].move(deck, tmp);
			workingPacks[i].get(i).flip(true);
		}

		// Set activity to null
		activeCard = null;
		activePack = null;
	}

	/**
	 * Save current game.
	 * The game is saved in a text form.
	 * @param name filename of the saved game. The file will be: saved/{name}.sv
	 */
	public void saveGame(String name) {
		// Check saved folder
		File folder = new File("saved");
		if (!folder.exists()) {
			if (!folder.mkdir())
				// TODO -> GUI MESSAGE
				return;
		}

		// Check if user is okay with overwriting if file already exists.
		File file = new File("saved" + File.separator + name + ".sv");
		if (file.exists()) {
			// TODO -> GUI MESSAGE
			System.out.println("Do you want to overwrite save? (yes/no)");
			if (!getUserInput().equals("yes"))
				return;
		}

		// Write to file
		FileWriter fw;
		try {
			fw = new FileWriter(file);
		}
		catch (IOException e) {
			// TODO -> GUI MESSAGE
			e.printStackTrace();
			return;
		}

		String saveStr = makePackString();
		try {
			fw.write(saveStr);
			fw.close();
		}
		catch (IOException e) {
			// TODO -> GUI MESSAGE
			e.printStackTrace();
		}
	}

	/**
	 * Load game.
	 * All packs are initialized appropriately.
	 */
	public void loadGame(String name) {
		// Check if file exists
		File file = new File("saved" + File.separator + name + ".sv");
		if (!file.exists()) {
			// TODO -> GUI MESSAGE
			System.out.println("The save file does not exist.");
			return;
		}

		// Create readers
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
		}
		catch (IOException e) {
			// TODO -> GUI MESSAGE
			e.printStackTrace();
			return;
		}

		// Create packs from file
		factory = new KlondikePackFactory();
		try {
			// Create deck and source pack
			deck = factory.createDeck(br.readLine());
			sourcePack = factory.createSourcePack(br.readLine());

			// Create target packs of each color
			targetPacks = new Pack[4];
			int i = 0;
			for (Card.Color color: Card.Color.values()) {
				targetPacks[i] = factory.createTargetPack(color, br.readLine());
				i += 1;
			}

			// Create 7 working packs and deal cards to them from deck
			workingPacks = new Pack[7];
			for (i = 0; i < 7; i++) {
				workingPacks[i] = factory.createWorkingPack(br.readLine());
			}

			// Set activity to null
			activeCard = null;
			activePack = null;
		}
		catch (IOException e) {
			// TODO -> GUI MESSAGE
			e.printStackTrace();
		}
	}

	/**
	 * Click card.
	 * All necessary actions are carried out.
	 */
	public void clickCard(Card clickedCard, Pack clickedPack) {
		// If the deck was clicked
		if (clickedPack == deck) {
			// Reset activity
			activeCard = null;
			activePack = null;

			if(deck.size() != 0) {
				// Move top card to the source pack
				sourcePack.push(deck.pop());
			}
			else {
				// Return cards from the source pack
				while(sourcePack.size() != 0)
					deck.push(sourcePack.pop());
			}
			
			return;
		}

		// Regular pack
		if (activeCard == null) {
			// Card selection
			if (!clickedCard.isFacedUp())
				return;
			// Only top card from source pack can be selected
			if ((clickedPack == sourcePack) && (clickedPack.get(clickedPack.size() - 1) != clickedCard))
				return;

			activeCard = clickedCard;
			activePack = clickedPack;
		}
		else {
			// Card move
			clickedPack.move(activePack, activeCard);

			activeCard = null;
			activePack = null;
		}
	}

	/**
	 * Make a string containing all the packs of game.
	 * Each pack is on new line.
	 */
	private String makePackString() {
		StringBuffer buf = new StringBuffer();
		buf.append(deck); buf.append("\n");
		buf.append(sourcePack); buf.append("\n");
		for (int i = 0; i < 4; i++) {
			buf.append(targetPacks[i]); buf.append("\n");
		}
		for (int i = 0; i < 7; i++) {
			buf.append(workingPacks[i]); buf.append("\n");
		}

		return buf.toString();
	}

	/**
	 * Print packs to standard output.
	 * Auxiliary function.
	 */
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


	/**
	 * Read text commands from standard input.
	 * Auxiliary function.
	 * Commands are in format PNI where:
	 *     P is type of pack:
	 *         D is Deck
	 *         S is Source pack
	 *         T is Target pack
	 *         W is Working pack
	 *     N is number of pack where necessary. In case of D and S, this char is ignored.
	 *     I is index of card in the pack. This can be more than one character.
	 * Special commands:
	 *     SVname saves the game - replace "name" with the desired filename without extension
	 *     LDname loads the game - as with save
	 *     NG makes new game
	 *     Q quits this function - other chars are ignored.
	 */
	public void textCommand() {
		Card clickedCard;
		Pack clickedPack;
		String command;

		while (true) {
			printPacks();

			command = getUserInput();
			if (command == null) {
				return;
			}

			clickedPack = null;

			switch (command.charAt(0)) {
				case 'D':
					clickedPack = deck;
					break;
				case 'S':
					if (command.charAt(1) == 'V') {
						saveGame(command.substring(2, command.length()));
						break;
					}
					clickedPack = sourcePack;
					break;
				case 'T':
					clickedPack = targetPacks[Character.getNumericValue(command.charAt(1))];
					break;
				case 'W':
					clickedPack = workingPacks[Character.getNumericValue(command.charAt(1))];
					break;
				case 'L':
					loadGame(command.substring(2, command.length()));
					break;
				case 'N':
					newGame();
					break;
				case 'Q':
					return;
				default:
					System.out.println("Unrecognised command.");
					break;
			}

			if (clickedPack == null)
				continue;

			if (command.length() > 2) {
				clickedCard = clickedPack.get(Integer.parseInt(command.substring(2, command.length())));
			}
			else {
				clickedCard = null;
			}

			clickCard(clickedCard, clickedPack);
		}
	}

	/**
	 * Reads line from standard input.
	 * Auxiliary function.
	 * @return user input
	 */
	private String getUserInput() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input;

		try {
			input = br.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return input;
	}

}
