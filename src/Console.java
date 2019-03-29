import java.io.*;
import java.util.*;

public class Console {

	// game info
	public static int playerNum;
	public static int kingNum;
	public static int round = 1;
	public static List<Player> playerList = new ArrayList<>();
	public static int maxRound;

	public static List<Integer> playerOrder;

	public static List<Domino> dominoList;
	static List<Domino> dominoListDraw;
	// static List<Domino> dominoTempList;
	static List<King> kingList = new ArrayList<>();
	static Map<King, Domino> kingToDomino = new LinkedHashMap<King, Domino>();

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
		return 0 <= x && x <= 8;
	}

	public static boolean gameOver() {

		return (round <= maxRound) ? false : true;

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
				System.out.println("Saisissez un nombre entre 0 et 8");
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
		System.out.println("Saisissez le nombre de joueur (chiffre entier 2 a 4) : ");
		do {
			n = inputInt();
			if (!playerNumValid(n)) {
				System.out.println("Erreur : Veuillez entrer un nombre entier entre 2 et 4.");
			}
		} while (!playerNumValid(n));
		return n;

	}

	public static List<Domino> loadDominos(String filePath) {
		List<Domino> dominoList = new ArrayList<Domino>();
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
		List<Domino> tempList;
		tempList = dominoList.subList(0, kingNum);
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
		Player p;
		// Collections.shuffle(listColor);
		System.out.println(listColor);
		int kingPerPlayer = playerNum == 2 ? 2 : 1;
		System.out.println("Nombre de joueur : " + playerNum);
		for (int i = 1; i <= playerNum; i++) {
			System.out.println("Saisissez le nom du joueur No." + i);
			String name = inputString();
			name = name.isEmpty() ? "Player" + Integer.toString(i) : name;
			if (name.equals("AI")) {
				p = new AI(name, listColor.get(i - 1), kingPerPlayer);
			} else {
				p = new Player(name, listColor.get(i - 1), kingPerPlayer);
			}

			for (int j = 0; j < kingPerPlayer; j++) {
				King k = new King(listColor.get(i - 1));
				kingList.add(k);
				p.getKings().add(k);
			}
			playerList.add(p);
		}
	}

	public static void creatPlayers2() {
		ArrayList<String> listColor = new ArrayList<String>(Arrays.asList("red", "yellow", "green", "pink"));
		Player p;
		// Collections.shuffle(listColor);
		System.out.println(listColor);
		int kingPerPlayer = playerNum == 2 ? 2 : 1;
		System.out.println("Nombre de joueur : " + playerNum);
		for (int i = 1; i <= playerNum; i++) {
			p = new AI("AI", listColor.get(i - 1), kingPerPlayer);

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
		System.out.println("Player type : " + p.getPlayerType());

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

	}

	public static void playersInit() {
		// player inisialisation
		// playerNum = inputPlayerNum();
		playerNum = 2;
		creatPlayers2();
		printPlayerList(playerList);
		kingNum = (playerNum == 2 || playerNum == 4) ? 4 : 3;
		maxRound = (playerNum == 2) ? 7 : 13;

		System.out.println("nombre de rois : " + kingNum);
		System.out.println("___________________________________________________");
		dominoList = configDominoList();

	}

	public static void round() {

		System.out.println("Round : " + round);

		if (round != maxRound) {
			dominoListDraw = drawDominos();
			// printDominoList(dominoListDraw);
			System.out.println("___________________________________________________");
			dominoListDraw = sortDominoByNum(dominoListDraw);
			// printDominoList(dominoListDraw);
		}
		System.out.println("___________________________________________________");
		List dominoTempList = dupliqueDominoList(dominoListDraw);

		Map tempKingToDomino = new LinkedHashMap<King, Domino>();

		if (round == 1) {
			Collections.shuffle(kingList);
			for (King k : kingList) {
				Player p = getPlayerByKing(k);
				tempKingToDomino.put(k, k.draw()); // choose domino
			}
		}

		System.out.println("___________________________________________________");
		System.out.println("L'ordre de ce tour : ");
		printKingList(kingList);
		System.out.println("___________________________________________________");

		System.out.println("___________________________________________________");

		for (King k : kingList) {
			Player p = getPlayerByKing(k);
			// printPlayerInfo(p);
			if (round != 1) {

				if (round < maxRound) {
					tempKingToDomino.put(k, k.draw()); // choose domino
				}

				// place domino
				Domino domino = kingToDomino.get(k);

				if (p.playerType.equals("Person")) {
					int x, y;
					boolean putOk = false;
					do {
						do {
							p.printLand();
							domino.printDominoInfo();
							domino.setDirection(Domino.D1);
							p.detectPositions(domino);
							domino.setDirection(Domino.D1);
							domino.turnDomino();
							x = inputPosition();
							y = inputPosition();
						} while (p.isLandOccupied(domino, x, y) || !p.isPlaceOk(domino, x, y));
						p.placeDomino(domino, x, y);
						if (!p.isDimensionOk()) {
							p.removeDominoFromLand(domino, x, y);
							System.out.println("Dimension out of bounds !");
						} else {
							putOk = true;
							p.printLand();
							System.out.println("Current score of player " + p.getId() + " is " + p.calculateScore());

						}
					} while (!putOk);

				} else {
					System.out.println("AI's turn !");
					p.place(domino);
				}

			}
		}

		kingToDomino = tempKingToDomino;

		System.out.println("___________________________________________________");
		printKingToDomino();
		System.out.println("___________________________________________________");
		kingList = configKingListForNextTurn(dominoTempList);

		round++;
	}

	public static void main(String[] args) {

		// initialisation
		for (int i = 0; i < 2000; i++) {
			inisialisation();
			System.out.println("___________________________________________________");
			System.out.println("___________________________________________________");
			// creat players
			playersInit();
			System.out.println("___________________________________________________");
			System.out.println("___________________________________________________");
			// round
			while (!gameOver()) {
				round();
			}

			System.out.println("GAME OVER");
			for (Player p : playerList) {
				int finalScore = p.finalScore();
				p.printLand();
				System.out.println("Final score of player " + p.getId() + " is " + finalScore);
				System.out.println(p.finalScore + "," + p.castleCentered + "," + p.emptyAreaNum + ","
						+ p.singleEmptyAreaNum + "," + p.totalCrownNum + "\n");
				try {
					p.printResultInCsv();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			System.out.println("___________________________________________________");
			round = 1;
			playerList.removeAll(playerList);
			dominoList.removeAll(dominoList);
			dominoListDraw.removeAll(dominoListDraw);
			kingList.removeAll(kingList);
			kingToDomino.clear();
		}
	}

}
