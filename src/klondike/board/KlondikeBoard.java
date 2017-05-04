package klondike.board;

import klondike.klondikeInterface.*;
import klondike.model.KlondikePackFactory;

import java.io.*;

/**
 * Klondike board.
 * Implements main logic and rules of the game.
 * @author xivanc03
 */
public class KlondikeBoard {
	private String board_id;
	private PackFactory factory;
	public Pack deck;
	public Pack sourcePack;
	public Pack[] targetPacks;
	public Pack[] workingPacks;
	private Card activeCard;
	private Pack activePack;

	/**
	 * Constructor.
	 * @param id the id of the board used for autosave
	 * @param load the name of the save file to load from. If null, a new game is created.
	 */
	public KlondikeBoard(String id, String load) {
		board_id = id;

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
		// Clear autosave file
		try {
			FileWriter writer = new FileWriter(new File("game" + File.separator + board_id + ".asv"));
			writer.write("");
			writer.close();
		}
		catch (Exception e) {
			// If file is not found, no worries
		}

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
			workingPacks[i].move(deck, deck.get(deck.size() - i - 1));
			workingPacks[i].get(i).flip(true);
		}

		// Set activity to null
		activeCard = null;
		activePack = null;
	}

	/**
	 * Check if save file exists.
	 * @param name name of the save file without extension
	 * @return true if file exists, false if it does not
	 */
	public boolean saveFileExists(String name) {
		return (new File("saved" + File.separator + name + ".sv")).exists();
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
			folder.mkdir();
		}

		// Write to file
		File file = new File("saved" + File.separator + name + ".sv");
		FileWriter writer;
		String saveStr = boardToString();
		try {
			writer = new FileWriter(file);
			writer.write(saveStr);
			writer.close();
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
		// Create packs from file
		File file = new File("saved" + File.separator + name + ".sv");
		FileReader reader;
		StringBuilder str = new StringBuilder();
		factory = new KlondikePackFactory();
		try {
			// Get file to string
			reader = new FileReader(file);
			int c = 0;
			while ((c = reader.read()) != -1) str.append((char) c);

			// Load packs
			boardFromString(str.toString());
			reader.close();

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
	 * Auto save function for undo.
	 * All packs are saved to autosave file. Only last 10 configurations are kept.
	 */
	protected void autoSave() {
		// Check game folder
		File folder = new File("game");
		if (!folder.exists()) {
			folder.mkdir();
		}

		// Needed variables
		File file = new File("game" + File.separator + board_id + ".asv");
		BufferedReader reader;
		String ls = System.getProperty("line.separator");
		String line;
		StringBuilder whole = new StringBuilder();
		int i = 0;
		FileWriter writer;

		try {
			// Get file and reader
			if (!file.exists()) {
				file.createNewFile();
			}
			reader = new BufferedReader(new FileReader(file));

			// Build the content
			while((i < 9 * 13) && ((line = reader.readLine()) != null)) {
				whole.append(line);
				whole.append(ls);
				i += 1;
			}
			whole.insert(0, boardToString());
			reader.close();

			// Write to file
			writer = new FileWriter(file);
			writer.write(whole.toString());
			writer.close();
		}
		catch (IOException e) {
			// TODO -> GUI MESSAGE
			e.printStackTrace();
		}
	}

	/**
	 * Undo last move.
	 * Maximum of 10 undo moves are available.
	 */
	public void undo() {
		File file = new File("game" + File.separator + board_id + ".asv");
		BufferedReader reader;
		String ls = System.getProperty("line.separator");
		String line;
		StringBuilder str = new StringBuilder();
		FileWriter writer;
		int i = 0;

		try {
			// Get file and reader
			if (!file.exists()) {
				return;
			}
			reader = new BufferedReader(new FileReader(file));

			// Get last state
			while((i < 13) && ((line = reader.readLine()) != null)) {
				str.append(line);
				str.append(ls);
				i += 1;
			}

			// Load it
			if (str.toString().equals("")) {
				return;
			}
			boardFromString(str.toString());

			// Get the rest of the file
			str.setLength(0);
			while((line = reader.readLine()) != null) {
				str.append(line);
				str.append(ls);
			}
			reader.close();

			// Rewrite to file
			writer = new FileWriter(file);
			writer.write(str.toString());
			writer.close();
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
			autoSave();
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
			// If an empty pack was clicked, it will not get selected
			if (clickedCard == null)
				return;
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
			autoSave();
			// Card move
			clickedPack.move(activePack, activeCard);

			activeCard = null;
			activePack = null;
		}
	}

	public String getActivePack() {
		if (activePack == sourcePack) {
			return "S";
		}
		for (int i = 0; i < 4; i++) {
			if (activePack == targetPacks[i])
				return "T" + i;
		}
		for (int i = 0; i < 7; i++) {
			if (activePack == workingPacks[i])
				return "W" + i;
		}
		return "null";
	}

	public int getActiveCard() {
		for (int i = 0; i < activePack.size(); i++) {
			if (activePack.get(i) == activeCard) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Create string representing board configuration.
	 * Each pack is on new line.
	 * @return string representing all packs on board
	 */
	private String boardToString() {
		StringBuilder buf = new StringBuilder();
		String ls = System.getProperty("line.separator");

		buf.append(deck); buf.append(ls);
		buf.append(sourcePack); buf.append(ls);
		for (int i = 0; i < 4; i++) {
			buf.append(targetPacks[i]); buf.append(ls);
		}
		for (int i = 0; i < 7; i++) {
			buf.append(workingPacks[i]); buf.append(ls);
		}

		return buf.toString();
	}

	/**
	 * Load board configuration from string.
	 * Ech pack is filled accordingly.
	 * @param string a string representing all packs on board
	 */
	private void boardFromString(String string) {
		// Make reader
		BufferedReader reader = new BufferedReader(new StringReader(string));

		try {
			// Load deck and source pack
			deck = factory.createDeck(reader.readLine());
			sourcePack = factory.createSourcePack(reader.readLine());

			// Load target packs
			targetPacks = new Pack[4];
			int i = 0;
			for (Card.Color color: Card.Color.values()) {
				targetPacks[i] = factory.createTargetPack(color, reader.readLine());
				i += 1;
			}

			// Load working packs
			workingPacks = new Pack[7];
			for (i = 0; i < 7; i++) {
				workingPacks[i] = factory.createWorkingPack(reader.readLine());
			}
		}
		catch (IOException e) {
			// TODO -> GUI MESSAGE
			e.printStackTrace();
		}
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
	 *     U undos last move
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
				case 'U':
					undo();
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
