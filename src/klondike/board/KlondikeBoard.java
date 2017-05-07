package klondike.board;

import klondike.klondikeInterface.*;
import klondike.model.KlondikePackFactory;

import java.io.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/**
 * Klondike board.
 *
 * Implements main logic and the rules of the game.
 *
 * @author xivanc03
 * @author xandri03
 */
public class KlondikeBoard {
    //******************************************************************
    // Attributes
    
    // Card packs
    public Pack deck;
    public Pack sourcePack;
    public Pack[] targetPacks;
    public Pack[] workingPacks;
    
    // Active elements.
    private Card activeCard;
    private Pack activePack;

    /**
     * A stack of autosaves.
     */
    private final Stack<String> autosaves;
    
    /**
     * An array (a circular buffer) of hints.
     */
    private final List<Hint> hints;
    
    /**
     * Read head for {@code hints}.
     */
    private int hintPtr;
    
    //******************************************************************
    // Constructors
    
    /**
     * Default constructor.
     * @param str The name of the save file to load from. If null, a new game
     * is created.
     */
    public KlondikeBoard(String str) {
        autosaves = new Stack<>();
        hints = new ArrayList<>();
        
        if (str == null)
            newGame();
        else
            loadGame(str);
    }
    
    @Deprecated
    public KlondikeBoard(String id, String load) {
        this(load);
    }

    //******************************************************************
    // Public

    /**
     * Create a new game.
     */
    public void newGame() {
        // Create factory, deck and source pack
        PackFactory factory = new KlondikePackFactory();
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
            workingPacks[i].get().flip(true);
        }

        // Create initial state
        resetActivity();
        updateHints();
        autosaves.clear();
        autosave();
    }
    
    /**
     * Click the card. All necessary actions are carried out.
     * @param card The clicked card.
     * @param pack The pack of a clicked card.
     */
    public void clickCard(Card card, Pack pack) {
        boolean moved = false;  // true when a move was performed
        
        // Check if the deck was clicked
        if (pack == deck) {
            
            // Reset activity
            activeCard = null;
            activePack = null;

            // Move top card to the source pack or return all back
            if(!deck.empty()) {
                sourcePack.move(deck, deck.get());
            }
            else {
                while(!sourcePack.empty()) {
                    deck.move(sourcePack, sourcePack.get());
                }
            }
            moved = true;
        }
        else {
            // Regular pack: check activity
            if (activeCard != null) {
                // Move to working pack or target pack is allowed
                if(pack != sourcePack)
                    moved = pack.move(activePack, activeCard);

                resetActivity();
            }
            else {
                // An empty pack may not be selected
                if (card == null)
                    return;

                // A card that is faced down may not be selected
                if(!card.isFacedUp())
                    return;

                // Only the top card from a source pack can be selected
                if (pack == sourcePack && pack.get() != card)
                    return;

                // Set activity
                activeCard = card;
                activePack = pack;
            }
        }
        
        // If a changed occured, update hints and save new state
        if(moved) {
            updateHints();
            autosave();
        }
    }
      
    /**
     * Reset card/pack activity.
     */
    public void resetActivity() {
        activeCard = null;
        activePack = null;
    }
    
    /**
     * Get active pack.
     * @return active pack
     */
    public Pack getActivePack() {
        return activePack;
    }

    /**
     * Get active card.
     * @return active card
     */
    public Card getActiveCard() {
        return activeCard;
    }

    /**
     * Check if the save file exists.
     * @param name name of the save file without extension
     * @return true if file exists, false if it does not
     */
    public boolean saveFileExists(String name) {
        return (new File("saved" + File.separator + name + ".sv")).exists();
    }

    /**
     * Save current game. The game is saved in a text form.
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
        String saveStr = toString();
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
     * Load the game. All packs are initialized appropriately.
     * @param name Filename to load from.
     */
    public void loadGame(String name) {
        // Read board specification
        File file = new File("saved" + File.separator + name + ".sv");
        FileReader reader;
        StringBuilder str = new StringBuilder();
        try {
            // Get file to string
            reader = new FileReader(file);
            int c;
            while ((c = reader.read()) != -1) str.append((char) c);

            // Load packs
            reader.close();

            // Set activity to null
            activeCard = null;
            activePack = null;
        }
        catch (IOException e) {
            // TODO -> GUI MESSAGE
            e.printStackTrace();
        }
        
        // Load from string
        fromString(str.toString());
       
        // Create initial state
        resetActivity();
        updateHints();
        autosaves.clear();
        autosave();
    }

    /**
     * Undo last move. Maximum of 20 undo moves are available.
     */
    public void undo() {
        autosaves.pop();
        if(!autosaves.empty())
            fromString(autosaves.peek());
    }

    /**
     * Find a valid move.
     * @return A valid move or null if none is possible.
     */
    public Hint hint() {
        if(hints.isEmpty())
            return null;
        
        Hint h = hints.get(hintPtr);
        hintPtr = (hintPtr+1) % hints.size();
        return h;
    }
    
    /**
     * Create a string representation of a board. Each pack is on the new line.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        String ls = System.getProperty("line.separator");

        buf.append(deck); buf.append(ls);
        buf.append(sourcePack); buf.append(ls);
        for (int i = 0; i < 4; i++) {
            buf.append(targetPacks[i]);
            buf.append(ls);
        }
        for (int i = 0; i < 7; i++) {
            buf.append(workingPacks[i]);
            buf.append(ls);
        }

        return buf.toString();
    }

    /**
     * Load board configuration from {@code str}.
     * Each pack is constructed separately.
     * @param str A string representing all packs on board
     */
    public void fromString(String str) {
        // Check input string
        if(str == null)
            return;
        
        // Create reader
        BufferedReader reader = new BufferedReader(new StringReader(str));

        // Load specification
        PackFactory factory = new KlondikePackFactory();
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
     * Check if the game is finished, that is, all 4 kings are at target packs.
     * @return True if all 4 kings
     */
    public boolean isFinished() {
        for(Pack pack: targetPacks) {
            if((pack.get() == null) || (pack.get().value() != 13))
                return false;
        }
        
        return true;
    }
            
    //******************************************************************
    // Auxiliary
    
    /**
     * Autosave function for undo. The board specification is stored via its
     * string representation in a stack. 20 of such saves are stored.
     */
    protected void autosave() {
        autosaves.push(toString());
        if(autosaves.size() > 20)
            autosaves.removeElementAt(0);
    }

    /**
     * Update hints buffer.
     */
    protected void updateHints() {
        // Clear hints
        hints.clear();
        hintPtr = 0;
        
        // Try to move a card from the source
        for(Pack target: targetPacks) {
            if(target.check(sourcePack, sourcePack.get()))
                hints.add(new Hint(sourcePack, sourcePack.get(), target));
        }
        for(Pack target: workingPacks) {
            if(target.check(sourcePack, sourcePack.get()))
                hints.add(new Hint(sourcePack, sourcePack.get(), target));
        }
        
        // Try to move a sequence of cards from the working pack
        for(Pack source: workingPacks) {
            for(int i = source.size()-1; i >= 0; i--) {
                // Cannot move a faced down card
                Card card = source.get(i);
                if(!card.isFacedUp())
                    break;
                
                // Try target packs
                for(Pack target: targetPacks) {
                    if(target.check(source, card))
                        hints.add(new Hint(source, card, target));
                }
                
                // Try other working packs
                for(Pack target: workingPacks) {
                    if(target.check(source, card))
                        hints.add(new Hint(source, card, target));
                }
            }
        }
        
        // Finally, try to move a card from a target pack
        for(Pack source: targetPacks) {
            for(Pack target: workingPacks) {
                if(target.check(source, source.get()))
                    hints.add(new Hint(source, source.get(), target));
            }
        }
    }

    //******************************************************************
    // Deprecated
    
    /**
     * Print packs to standard output.
     * Auxiliary function.
     */
    @Deprecated
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
    @Deprecated
    public void textCommand() {
        Card clickedCard;
        Pack clickedPack;
        String command;

        while (true) {
            printPacks();

            command = getUserInput();
            if (command == null)
                return;
            
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

            if (command.length() > 2)
                clickedCard = clickedPack.get(Integer.parseInt(command.substring(2, command.length())));
            else
                clickedCard = null;
            
            clickCard(clickedCard, clickedPack);
        }
    }

    /**
     * Reads line from standard input.
     * Auxiliary function.
     * @return user input
     */
    @Deprecated
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
