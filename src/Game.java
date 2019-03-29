import java.awt.Font;
import java.io.*;
import java.util.*;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends BasicGame {

	public Game(String title) {
		super(title);
	}

	private final static int ID = 1;

	public static int width, height, dominoWidth;

	public static int playerNum;
	public static int kingNum;
	public static int round = 1;
	public static List<Player> playerList = new ArrayList<>();

	public static List<Integer> playerOrder;

	public static List<Domino> dominoList = new ArrayList<>();;
	static List<Domino> dominoListDraw = new ArrayList<>();;
	static List<Domino> dominoTempList;
	static List<King> kingList = new ArrayList<King>();
	static Map<King, Domino> kingToDomino = new LinkedHashMap<King, Domino>();
	static Map<King, Domino> tempKingToDomino = new LinkedHashMap<King, Domino>();

	public final static int UP = Input.KEY_UP;
	public final static int DOWN = Input.KEY_DOWN;
	public final static int LEFT = Input.KEY_LEFT;
	public final static int RIGHT = Input.KEY_RIGHT;
	public final static int CONFIRM = Input.KEY_ENTER;

	public static int GAME_PHASE = 0;
	public static Player currentPlayer;
	public static King currentKing;
	public static Domino currentDomino;

	static boolean kingsShuffled = false;
	static boolean tempKingToDominoInitialised = false;
	static boolean scoreCalculated = false;
	static boolean lastRoundFlag = false;

	static int createdPlayer = 0;
	static int kingChoseDomino = 0;
	static int playedKing = 0;
	static int totalRound;

	public final static int MAIN_SCREEN = 0;
	public static final int INPUT_PLAYER_NUMBER = 1;
	public final static int CREAT_PLAYERS = 2;
	public final static int SHUFFLE_KINGS = 3;
	public final static int DRAW_DOMINOS = 4;
	public final static int CHOSE_DOMINO = 5;
	public final static int PLACE_DOMINO = 6;
	public final static int GAME_OVER = 100;

	public final static Color MESSAGE_COLOR = Color.white;
	public final static Color ALTERNATIVE_MESSAGE = Color.black;
	public final static Color WARNING_COLOR = Color.red;

	static TextField textField;
	static Domino displayedDomino;
	static String displayedString1 = "";
	static String displayedString2 = "";

	static Player winner;
	static int winnerScore;
	TrueTypeFont font;

	static float infoX, infoY, d;

	public static Image CHAMPS_IMAGE;
	public static Image FORET_IMAGE;
	public static Image MER_IMAGE;
	public static Image PRAIRIE_IMAGE;
	public static Image MINE_IMAGE;
	public static Image MONTAGNE_IMAGE;
	public static Image CASTLE_IMAGE;
	public static Image EMPTY_IMAGE;

	Image background;
	Image mainScreen;
	Image panel;

	// Getters
	public int getPlayerNum() {
		return playerNum;
	}

	public int getKingNum() {
		return kingNum;
	}

	// Valid check
	public static boolean playerNumValid(int playerN) {
		return 2 <= playerN && playerN <= 4;
	}

	public static boolean isPositionValid(int x) {
		return 1 <= x && x <= 9;
	}

	public static int inputInt() {
		int n = 0;
		Scanner scan = new Scanner(System.in);
		boolean valeurOK = false;

		do {
			try {
				n = scan.nextInt();
				scan.nextLine();
				valeurOK = true;
			} catch (Exception e) {
				System.out.println("Erreur : Veuillez saisir un nombre entier.");
				scan.nextLine();
			}
		} while (!valeurOK);

		return n;
	}

	public static int inputPosition() {
		int x = 0;
		do {
			x = inputInt();
			if (!isPositionValid(x)) {
				System.out.println("Saisissez un nombre entre 1 et 9");
			}
		} while (!isPositionValid(x));
		return x;

	}

	public static String inputString() {
		String str = null;
		boolean valeurOK = false;
		Scanner scan = new Scanner(System.in);
		do {
			try {
				str = scan.nextLine();
				valeurOK = true;
			} catch (Exception e) {
				System.out.println("Erreur : Veuillez entrer une chaine de caractere valide.");
				scan.nextLine();
			}
		} while (!valeurOK);
		return str;
	}

	public static int inputPlayerNum() {
		int n;
		// System.out.println("Saisissez le nombre de joueur (chiffre entier 2 a 4) :
		// ");
		displayedString1 = "Saisissez le nombre de joueur (chiffre entier 2 a 4) : ";
		do {
			n = inputInt();
			if (!playerNumValid(n)) {
				System.out.println("Erreur : Veuillez entrer un nombre entier entre 2 et 4.");
			}
		} while (!playerNumValid(n));
		return n;

	}

	public static List loadDominos(String filePath) {
		List dominoList = new ArrayList<Domino>();
		Scanner scanner;
		int crownNum1, type1, crownNum2, type2, dominoNum;
		boolean loadSuccessful = false;
		do {
			try {
				File file = new File(filePath);
				scanner = new Scanner(file);
				scanner.useDelimiter(",|\n|\r");

				for (int i = 0; i <= 4; i++) { // header
					scanner.next();
				}

				// load dominos in dominoList
				for (int i = 0; i < 48; i++) {
					System.out.println(scanner.next());

					crownNum1 = Integer.parseInt(scanner.next());
					type1 = Domino.typeToInt(scanner.next());
					crownNum2 = Integer.parseInt(scanner.next());
					type2 = Domino.typeToInt(scanner.next());
					dominoNum = Integer.parseInt(scanner.next());

					Domino d = new Domino(crownNum1, type1, crownNum2, type2, dominoNum);

					if (d.dominoValid())
						dominoList.add(d);

				}
				loadSuccessful = true;
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Erreur lors du chargement du fichier dominos.csv");
				System.out.println("Verifiez que le fichier est bien a l'enplacement : " + filePath);
				System.out.println("Ou saisissez l'emplacement manuelment :");
				Scanner scan = new Scanner(System.in);
				String path = scan.nextLine();
				filePath = path.isEmpty() ? filePath : path;
			}
		} while (!loadSuccessful);
		return dominoList;
	}

	public static List configDominoList() {
		List<Domino> tempList = new ArrayList<Domino>();
		tempList = dominoList.subList(0, playerNum * 12);
		return tempList;
	}

	public static List<Domino> drawDominos() {
		List<Domino> tempList = new ArrayList<Domino>();
		for (int i = 0; i < kingNum; i++) {
			tempList.add(dominoList.get(0));
			dominoList.remove(0);
		}
		// tempList = dominoList.subList(0, kingNum);
		return tempList;
	}

	public static Domino dominoNumMin(List<Domino> liste) {
		Domino min = liste.get(0);
		for (int i = 0; i < liste.size(); i++)
			if (liste.get(i).getDominoNum() <= min.getDominoNum()) {
				min = liste.get(i);
			}
		return min;
	}

	public static List<Domino> sortDominoByNum(List l) {
		List<Domino> tempList = new ArrayList<Domino>();
		do {
			tempList.add(dominoNumMin(l));
			l.remove(dominoNumMin(l));
		} while (l.size() != 0);
		return tempList;
	}

	public static void printDominoInfo2(Domino d) {
		System.out.println("Domino No." + d.getDominoNum());
		System.out.println("Type 1 : " + Domino.typeToString(d.getType1()));
		System.out.println("crown number 1 : " + d.getCrownNum1());
		System.out.println("Type 2 : " + Domino.typeToString(d.getType2()));
		System.out.println("crown number 2 : " + d.getCrownNum2());
		System.out.println();
	}

	public static void printDominoList(List l) {
		int i = 0;
		Iterator<Domino> iterator = l.iterator();
		while (iterator.hasNext()) {
			System.out.println("No." + i);
			printDominoInfo2(iterator.next());
			i++;
		}
		System.out.println("logueuer de la list : " + l.size());
	}

	public static void creatPlayers() {
		ArrayList<String> listColor = new ArrayList<String>(Arrays.asList("red", "yellow", "green", "pink"));
		// Collections.shuffle(listColor);
		// System.out.println(listColor);
		int kingPerPlayer = playerNum == 2 ? 2 : 1;
		System.out.println("Nombre de joueur : " + playerNum);
		for (int i = 1; i <= playerNum; i++) {
			System.out.println("Saisissez le nom du joueur No." + i);
			String name = inputString();
			name = name.isEmpty() ? "Player" + Integer.toString(i) : name;
			Player p = new Player(name, listColor.get(i - 1), kingPerPlayer);
			// p.setKings(playerNum);
			for (int j = 0; j < kingPerPlayer; j++) {
				King k = new King(listColor.get(i - 1));
				kingList.add(k);
				p.getKings().add(k);
			}
			playerList.add(p);
		}
	}

	public static List configPlayerOrder() {
		List tempList = new ArrayList<Player>();
		for (King k : kingList) {
			for (Player p : playerList) {
				if (k.getColor() == p.getKingcolor()) {
					tempList.add(p.getId());
				}
			}
		}
		return tempList;
	}

	public static Player getPlayerByKing(King k) {
		for (Player p : playerList) {
			if (p.getKings().contains(k)) {
				return p;
			}
		}
		return null;
	}

	public static void printPlayerInfo(Player p) {
		System.out.println("Player No." + p.getId());
		System.out.println("Player name : " + p.getPlayerName());
		System.out.println("King number : " + p.getKingNum());
		System.out.println("King color : " + p.getKingcolor());
		printKingList(p.getKings());
		p.printLand();
		System.out.println();
	}

	public static void printPlayerList(List l) {
		Iterator<Player> iterator = l.iterator();
		while (iterator.hasNext()) {
			printPlayerInfo(iterator.next());
		}
	}

	public static void printKingInfo(King k) {
		System.out.println("-------------------");
		System.out.println(k.getColor() + " king");
		System.out.println("king id : " + k.getId());
		System.out.println("-------------------");
	}

	public static void printKingList(List<King> l) {
		Iterator<King> iterator = l.iterator();
		while (iterator.hasNext()) {
			printKingInfo(iterator.next());
		}
		System.out.println("Nombre de rois : " + l.size());
	}

	public static void printKingToDomino() {
		Set keys = kingToDomino.keySet();
		Iterator<King> kings = keys.iterator();
		while (kings.hasNext()) {
			King king = kings.next();
			Domino d = kingToDomino.get(king);
			printKingInfo(king);
			printDominoInfo2(d);

		}
	}

	public static List dupliqueDominoList(List l) {
		List<Domino> dominoTempList = new ArrayList<Domino>();
		Iterator<Domino> iter = l.iterator();
		while (iter.hasNext()) {
			dominoTempList.add(iter.next());
		}
		return dominoTempList;
	}

	public static Map dupliqueTempKingToDomino() {
		Map kingToDomino = new LinkedHashMap<King, Domino>();
		Set keys = tempKingToDomino.keySet();
		Iterator<King> kings = keys.iterator();
		while (kings.hasNext()) {
			King king = kings.next();
			Domino d = tempKingToDomino.get(king);
			kingToDomino.put(king, d);
		}
		return kingToDomino;
	}

	public static King getKingFromDomino(Domino d) {
		List tempList = new ArrayList<>();
		King king = null;
		for (King k : kingToDomino.keySet()) {
			if (kingToDomino.get(k).equals(d)) {
				// tempList.add(k);
				king = k;
			}
		}
		return king;
	}

	public static List configKingListForNextTurn(List dominoTempList) {
		List<King> tempKingList = new ArrayList<>();
		Iterator<Domino> iter = dominoTempList.iterator();
		while (iter.hasNext()) {
			King k = getKingFromDomino(iter.next());
			tempKingList.add(k);
		}
		return tempKingList;

	}

	public static void inisialisation() {
		// initialiser
		dominoList = loadDominos("dominos.csv"); // dominos.csv
		Collections.shuffle(dominoList);
		printDominoList(dominoList);
	}

	public static void playersInit() {
		// inisialisation des joueurs
		playerNum = inputPlayerNum();
		creatPlayers();
		printPlayerList(playerList);
		kingNum = (playerNum == 2 || playerNum == 4) ? 4 : 3;
		System.out.println("nombre de rois : " + kingNum);
		System.out.println("___________________________________________________");
		dominoList = configDominoList();
		printDominoList(dominoList);
	}

	public static void round() {

		dominoListDraw = drawDominos();
		printDominoList(dominoListDraw);
		System.out.println("___________________________________________________");
		dominoListDraw = sortDominoByNum(dominoListDraw);
		printDominoList(dominoListDraw);
		System.out.println("___________________________________________________");
		List dominoTempList = dupliqueDominoList(dominoListDraw);
		printDominoList(dominoTempList);
		if (round == 1) {
			Collections.shuffle(kingList);
		}
		System.out.println("___________________________________________________");
		System.out.println("L'ordre de ce tour : ");
		printKingList(kingList);
		System.out.println("___________________________________________________");

		System.out.println("___________________________________________________");
		Map tempKingToDomino = new LinkedHashMap<King, Domino>();

		for (King k : kingList) {
			Player p = getPlayerByKing(k);
			// printPlayerInfo(p);
			if (round != 1) {
				// place domino
				int x, y;
				Domino domino = kingToDomino.get(k);
				do {
					domino.turnDomino();
					x = inputPosition() - 1;
					y = inputPosition() - 1;
				} while (!p.isPlaceOk(domino, x, y) && !p.isLandOccupied(domino, x, y));
				p.placeDomino(domino, x, y);
			}
			tempKingToDomino.put(k, k.draw());
		}

		kingToDomino = tempKingToDomino;

		System.out.println("___________________________________________________");
		printKingToDomino();
		System.out.println("___________________________________________________");
		printDominoList(dominoTempList);
		kingList = configKingListForNextTurn(dominoTempList);
		printKingList(kingList);

		// dominoTempList.clear();
		round++;
	}

	public void gameOver() {
		if (dominoList.size() == 0)
			GAME_PHASE = 100;
	}

	public static String inputPlayerName(Input input) {
		String name = "";
		if (input.isKeyPressed(input.KEY_ENTER)) {
			name = textField.getText();
		}
		return name;
	}

	public static void finalScore() {
		winner = playerList.get(0);
		winner.calculateScore();
		winnerScore = winner.score;
		for (Player p : playerList) {
			int score = p.finalScore();
			if (score > winnerScore) {
				winner = p;
				winnerScore = score;
			}
		}
		System.out.println("The winner is : " + winner.getPlayerName());
		scoreCalculated = true;
	}

	public static void mousePressedPosition(Input input) {
		if (input.isMousePressed(input.MOUSE_LEFT_BUTTON)) {
			int posX = input.getMouseX();
			int posY = input.getMouseY();

			System.out.println(posX + " " + posY);
			System.out.println(width + " " + height);
			System.out.println();
		}
	}

	public static void renderMiniLand(Graphics graphics) {
		float x = width * 0.15f;
		float y = height * 0.83f;
		int i = 0;
		for (Player p : playerList) {
			p.renderLandMini(graphics, x + i * 10 * dominoWidth * 0.2f, y);
			i++;
		}
	}

	@Override
	public void init(GameContainer gameContainer) throws SlickException {
		width = gameContainer.getWidth();
		height = gameContainer.getHeight();
		dominoWidth = (int) (width * 0.05);
		dominoList = loadDominos("dominos.csv"); // dominos.csv
		Collections.shuffle(dominoList);
		// printDominoList(dominoList);
		King.king = new Image("king.png");

		Domino.crown = new Image("Crown2.png");

		CHAMPS_IMAGE = new Image("champs.png");
		FORET_IMAGE = new Image("foret.png");
		MER_IMAGE = new Image("mer.png");
		MINE_IMAGE = new Image("mines.png");
		MONTAGNE_IMAGE = new Image("montagne.png");
		PRAIRIE_IMAGE = new Image("prairie.png");
		CASTLE_IMAGE = new Image("castle.jpg");
		EMPTY_IMAGE = new Image("empty.jpg");

		background = new Image("board2.png");
		panel = new Image("yangpizhi.png");
		mainScreen = new Image("main.jpg");

		font = new TrueTypeFont(new java.awt.Font(java.awt.Font.SERIF, java.awt.Font.BOLD, (int) (height * 0.025)),
				false);
		textField = new TextField(gameContainer, font, (int) (width * 0.5 - width * 0.25),
				(int) (height * 0.5 - height * 0.1), (int) (width * 0.5), (int) (height * 0.05));
		textField.setBackgroundColor(Color.gray);
		textField.setBorderColor(Color.white);

	}

	@Override
	public void update(GameContainer gameContainer, int delta) throws SlickException {
		// System.out.println("current phase" + GAME_PHASE);
		Input input = gameContainer.getInput();

		float x, y;
		int i1, i2;
		switch (GAME_PHASE) {

		case MAIN_SCREEN:
			if (input.isKeyPressed(CONFIRM)) {
				GAME_PHASE++;
			}
			break;

		case INPUT_PLAYER_NUMBER:
			displayedString1 = "Saisissez le nombre de joueur (chiffre entier 2 a 4) : ";
			int n = 0;
			if (input.isKeyPressed(CONFIRM)) {
				try {
					n = Integer.parseInt(textField.getText());
				} catch (Exception e) {
					displayedString2 = "Erreur : Veuillez entrer un nombre entier entre 2 et 4.";
					textField.setText("");
				}

				if (!playerNumValid(n)) {
					displayedString2 = "Erreur : Veuillez entrer un nombre entier entre 2 et 4.";
					textField.setText("");
				} else {
					playerNum = n;
					kingNum = (playerNum == 2 || playerNum == 4) ? 4 : 3;
					textField.setText("");
					totalRound = (playerNum == 2) ? 6 : 12;
					GAME_PHASE++;
				}
			}
			break;

		case CREAT_PLAYERS:

			displayedString1 = "";
			displayedString2 = "";

			displayedString1 = "Nombre de joueur : " + String.valueOf(playerNum);

			ArrayList<String> listColor = new ArrayList<String>(Arrays.asList("red", "yellow", "green", "blue"));
			int kingPerPlayer = playerNum == 2 ? 2 : 1;

			if (createdPlayer < playerNum) {

				displayedString2 = "Saisissez le nom du joueur No." + String.valueOf(createdPlayer + 1);
				String name = "";
				if (input.isKeyPressed(input.KEY_ENTER)) {
					name = textField.getText();
					name = name.isEmpty() ? "Player" + Integer.toString(createdPlayer + 1) : name;
					Player p;
					// = new Player(name, listColor.get(createdPlayer), kingPerPlayer);

					if (name.equals("AI")) {
						p = new AI(name, listColor.get(createdPlayer), kingPerPlayer);
					} else {
						p = new Player(name, listColor.get(createdPlayer), kingPerPlayer);
					}
					for (int j = 0; j < kingPerPlayer; j++) {
						King k = new King(listColor.get(createdPlayer));
						kingList.add(k);
						p.getKings().add(k);
					}
					playerList.add(p);
					createdPlayer++;
					System.out.println(playerList);
					System.out.println(kingList);
					textField.setText("");

				}

			}

			else {
				displayedString1 = "";
				displayedString2 = "";
				dominoList = configDominoList();

				GAME_PHASE = round == 1 ? SHUFFLE_KINGS : DRAW_DOMINOS;
			}
			break;

		case SHUFFLE_KINGS:

			if (!kingsShuffled) {
				Collections.shuffle(kingList);
				if (input.isKeyPressed(CONFIRM)) {
					kingsShuffled = true;

				}
			}

			if (input.isKeyPressed(CONFIRM)) {
				GAME_PHASE = DRAW_DOMINOS;
				// kingsShuffled = false;
				displayedString1 = "";
				displayedString2 = "";
			}
			break;

		case DRAW_DOMINOS:

			dominoListDraw = drawDominos();
			dominoListDraw = sortDominoByNum(dominoListDraw);
			dominoTempList = dupliqueDominoList(dominoListDraw);
			GAME_PHASE = CHOSE_DOMINO;
			System.out.println(dominoListDraw);
			displayedString1 = "";
			displayedString2 = "";

			break;

		case CHOSE_DOMINO:

			x = width * 0.125f;
			y = height * 0.3f;

			if (!tempKingToDominoInitialised) {

				tempKingToDominoInitialised = true;
				System.out.println(tempKingToDomino);
			}

			if (kingChoseDomino < kingList.size()) {
				currentKing = kingList.get(kingChoseDomino);
				currentPlayer = getPlayerByKing(currentKing);

			}
			// currentKing.choseDomino(input);
			if (currentPlayer.playerType.equals("Person")) {
				if (input.isMousePressed(input.MOUSE_LEFT_BUTTON)) {
					int posX = input.getMouseX();
					int posY = input.getMouseY();

					// System.out.println(x + " " + y);
					// System.out.println(x * 3 * Game.dominoWidth + " " + (x + Game.dominoWidth *
					// (2 + 3)));
					// System.out.println(x * 3 * Game.dominoWidth + " " + (x + Game.dominoWidth *
					// (2 + 3 * 2)));
					// System.out.println();
					// System.out.println(posX + " " + posY);

					for (int i4 = 0; i4 < dominoListDraw.size(); i4++) {
						if ((x + i4 * 3 * dominoWidth < posX) && (posX < (x + dominoWidth * (2 + 3 * i4)))
								&& ((y < posY) && (posY < (y + Game.dominoWidth * 2)))) {
							Domino d = dominoListDraw.get(i4);
							dominoListDraw.remove(d);
							tempKingToDomino.put(currentKing, d);
							kingChoseDomino++;
							System.out.println(kingChoseDomino);
							System.out.println(currentKing.getId());

						}
					}

				}
			} else {
				if (!dominoListDraw.isEmpty()) {
					if (Game.round == 1) {

						int index = currentPlayer.chooseBestDominoTurn1(dominoListDraw);
						Domino d = dominoListDraw.get(index);
						tempKingToDomino.put(currentKing, d);
						dominoListDraw.remove(index);
						kingChoseDomino++;

					} else {

						Domino pre = kingToDomino.get(currentKing);
						int bestX = currentPlayer.bestPosition(pre)[1];
						int bestY = currentPlayer.bestPosition(pre)[2];
						int bestDirection = currentPlayer.bestPosition(pre)[3];
						int index = currentPlayer.chooseBestDomino(pre, bestX, bestY, bestDirection, dominoListDraw);
						Domino d = dominoListDraw.get(index);
						tempKingToDomino.put(currentKing, d);
						dominoListDraw.remove(d);
						kingChoseDomino++;

					}
				}
			}

			if (kingChoseDomino >= kingNum && input.isKeyPressed(CONFIRM)) {
				kingChoseDomino = 0;
				tempKingToDominoInitialised = false;
				displayedString1 = "";
				displayedString2 = "";
				if (round == 1) {
					GAME_PHASE = DRAW_DOMINOS;
					kingToDomino = dupliqueTempKingToDomino();
					kingList = configKingListForNextTurn(dominoTempList);
					round++;

				} else {
					GAME_PHASE = PLACE_DOMINO;
					printKingToDomino();

				}

			}

			break;

		case PLACE_DOMINO:

			if (playedKing < kingList.size()) {
				currentKing = kingList.get(playedKing);
				currentPlayer = getPlayerByKing(currentKing);
				currentDomino = kingToDomino.get(currentKing);
				currentDomino.update(input);
			}

			if (playedKing < kingNum) {
				if (currentPlayer.playerType.equals("Person")) {

					if (input.isMousePressed(input.MOUSE_LEFT_BUTTON)) {
						int posX = input.getMouseX();
						int posY = input.getMouseY();

						float x1 = Player.RENDER_START_PT_X;
						float y1 = Player.RENDER_START_PT_Y;

						boolean putOk = false;
						for (int i = 0; i < Player.LAND_DIMENSION; i++) {
							for (int j = 0; j < Player.LAND_DIMENSION; j++) {
								float xij = x1 + j * Game.dominoWidth;
								float yij = y1 + i * Game.dominoWidth;
								if ((xij < posX && posX < xij + dominoWidth) && (yij < posY && posY < yij + dominoWidth)
										&& (!currentPlayer.isLandOccupied(currentDomino, i, j)
												&& currentPlayer.isPlaceOk(currentDomino, i, j))) {
									currentPlayer.placeDomino(currentDomino, i, j);
									if (!currentPlayer.isDimensionOk()) {
										currentPlayer.removeDominoFromLand(currentDomino, i, j);
										System.out.println("Dimension out of bounds !");
										displayedString2 = "La dimension limite est 5 X 5";
									} else if (currentPlayer.isDimensionOk()) {
										putOk = true;
										currentDomino = null;
										currentPlayer.printLand();
										printPlayerInfo(currentPlayer);
										currentPlayer.calculateScore();
									}

								}

							}
						}
						if (putOk || !currentPlayer.detectPositions(currentDomino)) {

							playedKing++;
						}

					}
				} else {
					if (currentDomino != null) {
						currentPlayer.place(currentDomino);
						currentDomino = null;
						playedKing++;
					}

				}
			}

			if (playedKing >= kingNum && input.isKeyPressed(CONFIRM)) {

				if (round < totalRound) {
					GAME_PHASE = DRAW_DOMINOS;
				} else if (round == totalRound) {
					GAME_PHASE = PLACE_DOMINO;
				} else {
					GAME_PHASE = GAME_OVER;

				}
				displayedString1 = "";
				displayedString2 = "";

				playedKing = 0;
				currentPlayer = null;
				currentDomino = null;
				kingToDomino = dupliqueTempKingToDomino();
				for (Player p : playerList) {
					printPlayerInfo(p);
				}
				kingList = configKingListForNextTurn(dominoTempList);
				dominoTempList.clear();
				round++;
				if (dominoList.isEmpty()) {
					lastRoundFlag = true;

				}

			}

			break;

		case GAME_OVER:

			if (!scoreCalculated) {
				finalScore();
			}
			displayedString1 = "GAME OVER   THE WINNER IS : " + winner.getPlayerName();
			displayedString2 = "SCORE : " + winnerScore;

			break;

		}

		// mousePressedPosition(input);

	}

	@Override
	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		// TrueTypeFont font = new TrueTypeFont(new java.awt.Font(java.awt.Font.SERIF,
		// java.awt.Font.BOLD, 50),false);
		graphics.setFont(font);
		textField.setFocus(true);

		if (GAME_PHASE == PLACE_DOMINO || GAME_PHASE == DRAW_DOMINOS || GAME_PHASE == CHOSE_DOMINO
				|| GAME_PHASE == SHUFFLE_KINGS || GAME_PHASE == GAME_OVER) {
			background.draw(0, 0, width, height);
			panel.draw(width * 0.7f, 0, panel.getWidth() * 1.3f, height);
		}

		switch (GAME_PHASE) {

		case MAIN_SCREEN:
			graphics.setColor(MESSAGE_COLOR);
			mainScreen.draw(0, 0, width, height);
			graphics.drawString("Appuyez sur la touche ENTREE pour continuer", width * 0.5f - width * 0.15f,
					height * 0.5f);

			break;

		case INPUT_PLAYER_NUMBER:
			graphics.setColor(MESSAGE_COLOR);
			textField.render(gameContainer, graphics);
			graphics.drawString(displayedString1, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.25f);
			graphics.setColor(WARNING_COLOR);
			graphics.drawString(displayedString2, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.2f);

			break;

		case CREAT_PLAYERS:
			graphics.setColor(MESSAGE_COLOR);
			graphics.drawString(displayedString1, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.25f);
			graphics.drawString(displayedString2, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.2f);
			textField.render(gameContainer, graphics);

			break;

		case SHUFFLE_KINGS:
			float x = width * 0.125f;
			float y = height * 0.3f;
			int i = 1;
			Iterator<King> iter = kingList.iterator();
			while (iter.hasNext()) {
				iter.next().render(graphics, i * x, y);
				i++;
			}
			break;

		case DRAW_DOMINOS:
			graphics.drawString("Round : " + Game.round, Game.width * 0.75f, Game.height * 0.2f);
			x = width * 0.125f;
			y = height * 0.3f;

			int i1 = 1;
			if (!dominoListDraw.isEmpty()) {
				Iterator<Domino> iterDomino = dominoListDraw.iterator();
				while (iterDomino.hasNext()) {
					Domino domino = iterDomino.next();
					domino.renderFixed(graphics, x + 3 * dominoWidth * i1, y);
					i1++;
				}
			}
			break;

		case CHOSE_DOMINO:
			graphics.setColor(Game.ALTERNATIVE_MESSAGE);
			// graphics.drawString("Round : " + Game.ROUND, Game.width * 0.75f, Game.height
			// * 0.2f);
			i = 0;
			x = width * 0.125f;
			y = height * 0.3f;

			infoX = Game.width * 0.75f;
			infoY = Game.height * 0.2f;
			d = Game.width * 0.015f;

			graphics.setColor(Game.ALTERNATIVE_MESSAGE);
			graphics.drawString("Round : " + Game.round, infoX, infoY);
			if (currentPlayer != null) {
				graphics.drawString(currentPlayer.getPlayerName() + " choisissez un domino", infoX, infoY + d);
			}
			if (currentKing != null) {
				currentKing.render(graphics, infoX + 4 * d, infoY + 10 * d);
			}
			if (!dominoListDraw.isEmpty()) {
				Iterator<Domino> iterDomino = dominoListDraw.iterator();
				while (iterDomino.hasNext()) {
					Domino domino = iterDomino.next();
					if (!domino.chosed) {
						domino.renderFixed(graphics, x + 3 * dominoWidth * i, y);
						i++;
					}
				}
			}
			renderMiniLand(graphics);

			break;

		case PLACE_DOMINO:
			infoX = Game.width * 0.75f;
			infoY = Game.height * 0.2f;
			d = Game.width * 0.015f;
			graphics.setColor(Game.ALTERNATIVE_MESSAGE);
			graphics.drawString("Round : " + Game.round, infoX, infoY);

			if (currentPlayer != null) {
				currentPlayer.render(graphics);
			}
			if (currentDomino != null) {
				currentDomino.render(graphics);
			}
			renderMiniLand(graphics);
			graphics.drawString(displayedString1, width * 0.5f - width * 0.25f, height * 0.09f);
			graphics.setColor(WARNING_COLOR);
			graphics.drawString(displayedString2, width * 0.5f - width * 0.25f, height * 0.095f);

			break;

		case GAME_OVER:
			if (winner != null) {
				graphics.setColor(ALTERNATIVE_MESSAGE);
				graphics.drawString(displayedString1, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.25f);
				graphics.drawString(displayedString2, width * 0.5f - width * 0.25f, height * 0.5f + height * 0.015f);
				renderMiniLand(graphics);

				winner.renderInfo(graphics, ALTERNATIVE_MESSAGE);
			}

		}

	}

}
