import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Player {

	String playerName;
	int id;
	String kingColor;
	int kingNum;
	Area[][] land;
	String playerType;
	int score;
	private List<King> kings = new ArrayList<>();
	int[][][] scoreList;
	public static final int LAND_DIMENSION = 9;
	public static final int MAX_DIMENSION = 5;

	public static final float RENDER_START_PT_X = Game.height * 0.15f;
	public static final float RENDER_START_PT_Y = Game.height * 0.1f;

	static int PLAYER_ID = 1;

	// print in csv
	boolean castleCentered;
	int emptyAreaNum = 0;
	int singleEmptyAreaNum = 0;
	int totalCrownNum = 0;
	int finalScore;

	public Player() {

	}

	public Player(String name, String kingColor, int kingNum) {
		this.playerName = name;
		this.id = PLAYER_ID;
		this.land = intialiseLand();
		this.kingColor = kingColor;
		this.kingNum = kingNum;
		this.playerType = "Person";
		PLAYER_ID++;

	}

	public String getPlayerName() {
		return playerName;
	}

	public int getId() {
		return id;
	}

	public Area[][] getLand() {
		return land;
	}

	public String getKingcolor() {
		return kingColor;
	}

	public int getKingNum() {
		return kingNum;
	}

	public List<King> getKings() {
		return kings;
	}

	public void setName(String name) {
		playerName = name;
	}

	public static Area[][] intialiseLand() {
		Area[][] area = new Area[LAND_DIMENSION][LAND_DIMENSION];
		for (int i = 0; i != area.length; i++) {
			for (int j = 0; j != area[i].length; j++) {
				area[i][j] = (i == LAND_DIMENSION / 2 && j == LAND_DIMENSION / 2) ? new Area(Area.CASTLE) : new Area();
			}
		}
		return area;
	}

	public static int colorToId(String kingColor) {
		switch (kingColor) {
		case "red":
			return 1;
		case "yellow":
			return 3;
		case "blue":
			return 5;
		case "pink":
			return 7;
		default:
			return -1;
		}
	}

	/*
	 * public void setKings(int playerNum) { switch(playerNum) { case 2: this.king1
	 * = new King(colorToId(kingColor), kingColor); this.king2 = new
	 * King(colorToId(kingColor), kingColor); Main.kingList.add(this.king1);
	 * Main.kingList.add(this.king2); break; case 3: this.king1 = new
	 * King(colorToId(kingColor), kingColor); Main.kingList.add(this.king1); break;
	 * case 4: this.king1 = new King(colorToId(kingColor), kingColor);
	 * Main.kingList.add(this.king1); break; default : System.out.println("Error");
	 * break; } }
	 */

	public void printLand() {
		for (int i = 0; i != land.length; i++) {
			for (int j = 0; j != land[i].length; j++) {
				System.out.print(Area.typeToString(land[i][j].getType()) + " ");
			}
			System.out.println();
		}
	}

	public static boolean lengthValid(List l) {
		return l.size() <= 5;
	}

	public boolean landValid() {
		for (int i = 0; i != land.length; i++) {
			for (int j = 0; j != land[i].length; j++) {
				if (land[i][j].isOccupied())
					;
			}
			System.out.println();

		}
		return false;
	}

	public boolean isLandOccupied(Domino d, int x, int y) {
		switch (d.getDirection()) {
		case 1:
			// if : detect if out of border
			if (y < 8) {
				return (this.land[x][y].isOccupied() || this.land[x][y + 1].isOccupied());
			}
			return true;
		case 2:
			if (x < 8) {
				return (this.land[x][y].isOccupied() || this.land[x + 1][y].isOccupied());
			}
			return true;
		case 3:
			if (y > 0) {
				return (this.land[x][y].isOccupied() || this.land[x][y - 1].isOccupied());
			}
			return true;
		case 4:
			if (x > 0) {
				return (this.land[x][y].isOccupied() || this.land[x - 1][y].isOccupied());
			}
			return true;
		default:
			return true;
		}
	}

	public boolean isPlaceOk(Domino domino, int x, int y) {

		int type1 = domino.getType1();
		int type2 = domino.getType2();
		boolean piece1up = false;
		boolean piece1down = false;
		boolean piece1left = false;
		boolean piece1right = false;

		boolean piece2up = false;
		boolean piece2down = false;
		boolean piece2left = false;
		boolean piece2right = false;

		switch (domino.getDirection()) {
		// if in the corner, only detect some sides to avoid out of bounds
		case 1:
			if (x > 0) {
				piece1up = (land[x - 1][y].getType() == type1) || (land[x - 1][y].getType() == Area.CASTLE);
				piece2up = (land[x - 1][y + 1].getType() == type2) || (land[x - 1][y + 1].getType() == Area.CASTLE);
			}
			if (x < 8) {
				piece1down = (land[x + 1][y].getType() == type1) || (land[x + 1][y].getType() == Area.CASTLE);
				piece2down = (land[x + 1][y + 1].getType() == type2) || (land[x + 1][y + 1].getType() == Area.CASTLE);
			}
			if (y > 0) {
				piece1left = (land[x][y - 1].getType() == type1) || (land[x][y - 1].getType() == Area.CASTLE);
			}
			if (y < 7) {
				piece2right = (land[x][y + 2].getType() == type2) || (land[x][y + 2].getType() == Area.CASTLE);
			}
			return (piece1up || piece1down || piece1left || piece2up || piece2down || piece2right);

		case 2:
			if (x > 0) {
				piece1up = (land[x - 1][y].getType() == type1) || (land[x - 1][y].getType() == Area.CASTLE);
			}
			if (y > 0) {
				piece1left = (land[x][y - 1].getType() == type1) || (land[x][y - 1].getType() == Area.CASTLE);
				piece2left = (land[x + 1][y - 1].getType() == type2) || (land[x + 1][y - 1].getType() == Area.CASTLE);
			}
			if (y < 8) {
				piece1right = (land[x][y + 1].getType() == type1) || (land[x][y + 1].getType() == Area.CASTLE);
				piece2right = (land[x + 1][y + 1].getType() == type2) || (land[x + 1][y + 1].getType() == Area.CASTLE);
			}
			if (x < 7) {
				piece2down = (land[x + 2][y].getType() == type2) || (land[x + 2][y].getType() == Area.CASTLE);
			}
			return (piece1up || piece1left || piece1right || piece2down || piece2left || piece2right);
		case 3:
			if (x > 0) {
				piece1up = (land[x - 1][y].getType() == type1) || (land[x - 1][y].getType() == Area.CASTLE);
				piece2up = (land[x - 1][y - 1].getType() == type2) || (land[x - 1][y - 1].getType() == Area.CASTLE);
			}
			if (x < 8) {
				piece1down = (land[x + 1][y].getType() == type1) || (land[x + 1][y].getType() == Area.CASTLE);
				piece2down = (land[x + 1][y - 1].getType() == type2) || (land[x + 1][y - 1].getType() == Area.CASTLE);
			}
			if (y < 8) {
				piece1right = (land[x][y + 1].getType() == type1) || (land[x][y + 1].getType() == Area.CASTLE);
			}
			if (y > 1) {
				piece2left = (land[x][y - 2].getType() == type2) || (land[x][y - 2].getType() == Area.CASTLE);
			}
			return (piece1up || piece1down || piece1right || piece2up || piece2down || piece2left);

		case 4:
			if (x < 8) {
				piece1down = (land[x + 1][y].getType() == type1) || (land[x + 1][y].getType() == Area.CASTLE);
			}
			if (y > 0) {
				piece1left = (land[x][y - 1].getType() == type1) || (land[x][y - 1].getType() == Area.CASTLE);
				piece2left = (land[x - 1][y - 1].getType() == type2) || (land[x - 1][y - 1].getType() == Area.CASTLE);
			}
			if (y < 8) {
				piece1right = (land[x][y + 1].getType() == type1) || (land[x][y + 1].getType() == Area.CASTLE);
				piece2right = (land[x - 1][y + 1].getType() == type2) || (land[x - 1][y + 1].getType() == Area.CASTLE);
			}
			if (x > 1) {
				piece2up = (land[x - 2][y].getType() == type2) || (land[x - 2][y].getType() == Area.CASTLE);
			}
			return (piece1down || piece1left || piece1right || piece2up || piece2left || piece2right);
		}
		return true;
	}

	public boolean isDimensionOk() {
		// for rows and columns
		int rowDimension = 0;
		int columnDimension = 0;
		int xDimension, yDimension;
		for (int i = 0; i != land.length; i++) {
			xDimension = 0;
			yDimension = 0;
			for (int j = 0; j != land[i].length; j++) {
				if (land[i][j].isOccupied()) {
					xDimension = 1;
				}
				if (land[j][i].isOccupied()) {
					yDimension = 1;
				}
				if (j == land[i].length - 1) {
					rowDimension += xDimension;
					columnDimension += yDimension;
				}
			}
		}
		if (rowDimension > 5 || columnDimension > 5) {
			return false;
		}
		return true;
	}

	public void removeDominoFromLand(Domino domino, int x, int y) {
		switch (domino.getDirection()) {
		case Domino.D1:
			this.land[x][y] = new Area();
			this.land[x][y + 1] = new Area();
			break;
		case Domino.D2:
			this.land[x][y] = new Area();
			this.land[x + 1][y] = new Area();
			break;
		case Domino.D3:
			this.land[x][y] = new Area();
			this.land[x][y - 1] = new Area();
			break;
		case Domino.D4:
			this.land[x][y] = new Area();
			this.land[x - 1][y] = new Area();
			break;
		default:

			break;
		}
	}

	public class Location {
		int x, y;

		public Location(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public int[] get1stDominoPostion() {
		int[] position = new int[2];
		for (int i = 0; i != land.length; i++) {
			for (int j = 0; j != land[i].length; j++) {
				if (land[i][j].isOccupied()) {
					position[1] = i;
					position[2] = j;
				}
			}
		}
		return position;
	}

	public Area[][] land5x5() {
		Area[][] tempArea = new Area[MAX_DIMENSION][MAX_DIMENSION];
		int[] position = get1stDominoPostion();
		for (int i = position[0]; i < MAX_DIMENSION; i++) {
			for (int j = position[1]; j < MAX_DIMENSION; j++) {
				tempArea[i - position[0]][j - position[0]] = land[i][j];
			}
		}
		return tempArea;
	}

	public void renderLand(Graphics graphics) {
		graphics.setColor(Color.white);
		float y = RENDER_START_PT_Y;
		float width, height;
		width = height = y + (LAND_DIMENSION - 1) * Game.dominoWidth;
		graphics.fillRect(y, y, width, height);
		renderAreaInLand(graphics);
	}

	public void renderAreaInLand(Graphics graphics) {
		float x = RENDER_START_PT_X;
		float y = RENDER_START_PT_Y;

		for (int i = 0; i < LAND_DIMENSION; i++) {
			for (int j = 0; j < LAND_DIMENSION; j++) {

				int xij = (int) x + j * Game.dominoWidth;
				int yij = (int) y + i * Game.dominoWidth;

				land[i][j].render(graphics, xij, yij);

			}
		}
	}

	public void renderInfo(Graphics graphics, Color color) {
		float x = Game.width * 0.75f;
		float y = Game.height * 0.2f;
		float d = Game.width * 0.015f;
		// String str = "Player No." + id + "\r\n" + "Player name : " + playerName +
		// "\r\n" + "King color : " + kingColor;

		graphics.setColor(color);
		// graphics.drawString("Round : " + Game.ROUND, x, y);
		graphics.drawString("Player No." + id, x, y + d);
		graphics.drawString("Player name : " + playerName, x, y + 2 * d);
		graphics.drawString("King color : " + kingColor, x, y + 3 * d);
		renderKings(graphics, x, y + 10 * d);

	}

	public void renderKings(Graphics graphics, float x, float y) {
		float d = Game.width * 0.1f;
		int i = 0;
		for (King k : kings) {
			k.render(graphics, x + i * d, y);
			i++;
		}
	}

	public void render(Graphics graphics) {
		this.renderAreaInLand(graphics);
		this.renderInfo(graphics, Game.ALTERNATIVE_MESSAGE);
	}

	public void renderLandMini(Graphics graphics, float x, float y) {
		for (int i = 0; i < LAND_DIMENSION; i++) {
			for (int j = 0; j < LAND_DIMENSION; j++) {

				int xij = (int) (x + j * Game.dominoWidth * 0.2);
				int yij = (int) (y + i * Game.dominoWidth * 0.2);

				land[i][j].renderMini(graphics, xij, yij);

			}
		}
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public static void main(String[] args) {

	}

	public void placeDomino(Domino domino, int x, int y) {
		int type1 = domino.getType1();
		int crownNum1 = domino.getCrownNum1();
		int type2 = domino.getType2();
		int crownNum2 = domino.getCrownNum2();
		switch (domino.getDirection()) {
		case Domino.D1:
			this.land[x][y].setType(type1);
			this.land[x][y].setCrownNum(crownNum1);
			this.land[x][y].setStat(true);

			this.land[x][y + 1].setType(type2);
			this.land[x][y + 1].setCrownNum(crownNum2);
			this.land[x][y + 1].setStat(true);
			break;
		case Domino.D2:
			this.land[x][y].setType(type1);
			this.land[x][y].setCrownNum(crownNum1);
			this.land[x][y].setStat(true);

			this.land[x + 1][y].setType(type2);
			this.land[x + 1][y].setCrownNum(crownNum2);
			this.land[x + 1][y].setStat(true);
			break;
		case Domino.D3:
			this.land[x][y].setType(type1);
			this.land[x][y].setCrownNum(crownNum1);
			this.land[x][y].setStat(true);

			this.land[x][y - 1].setType(type2);
			this.land[x][y - 1].setCrownNum(crownNum2);
			this.land[x][y - 1].setStat(true);
			break;
		case Domino.D4:
			this.land[x][y].setType(type1);
			this.land[x][y].setCrownNum(crownNum1);
			this.land[x][y].setStat(true);

			this.land[x - 1][y].setType(type2);
			this.land[x - 1][y].setCrownNum(crownNum2);
			this.land[x - 1][y].setStat(true);
			break;
		default:

			break;
		}
	}

	public boolean detectPositions(Domino domino) {
		int availablePositions = 0;
		for (int i = 0; i < 9; i++) { // rows
			for (int j = 0; j < 9; j++) { // columns
				for (int k = 0; k < 4; k++) { // for direction 1 to 4
					domino.setDirection(k + 1);
					if (!isLandOccupied(domino, i, j) && isPlaceOk(domino, i, j)) {
						placeDomino(domino, i, j);
						if (isDimensionOk()) {
							availablePositions = availablePositions + 1;
						}
						removeDominoFromLand(domino, i, j); // only detection, so remove

					}
				}
			}
		}

		domino.setDirection(Domino.D1);
		if (availablePositions == 0) {
			System.out.println("No position available! You have to discard this domino");
			// discard domino code
			return false;
		} else {
			System.out.println("You have " + availablePositions + " positions available to put");
			Game.displayedString1 = "Vous avez " + availablePositions + " position valable";
			return true;
		}
	}

	public int calculateScore() {
		score = 0;
		int[][] crownNum = new int[9][9];
		for (int i = 0; i < 9; i++) { // rows
			for (int j = 0; j < 9; j++) { // columns
				crownNum[i][j] = -1; // initialisation
			}
		}

		for (int i = 0; i < 9; i++) { // rows
			for (int j = 0; j < 9; j++) { // columns
				if (crownNum[i][j] == -1 && land[i][j].isOccupied()) {
					Location location = new Location(i, j);
					findArea(location, crownNum);
				}
			}
		}

		for (int i = 0; i < 9; i++) { // rows
			for (int j = 0; j < 9; j++) { // columns
				if (crownNum[i][j] != -1)
					score += crownNum[i][j];
			}
		}
		return score;
	}

	public void findArea(Location location, int[][] crownNum) {
		Deque<Location> stackCrown = new LinkedList<Location>();
		stackCrown.push(location); // put the location in the stack
		int x = location.x;
		int y = location.y;
		int counter = land[x][y].getCrownNum();
		crownNum[x][y] = -2;

		// Depth-First-Search
		while (!stackCrown.isEmpty()) {
			x = stackCrown.peek().x;
			y = stackCrown.peek().y;

			// up
			if (x > 0 && crownNum[x - 1][y] == -1 && land[x][y].getType() == land[x - 1][y].getType()) {
				crownNum[x - 1][y] = -2;
				stackCrown.push(new Location(x - 1, y));
				counter += land[x - 1][y].getCrownNum();
				continue;
			}

			// right
			if (y < 8 && crownNum[x][y + 1] == -1 && land[x][y].getType() == land[x][y + 1].getType()) {
				crownNum[x][y + 1] = -2;
				stackCrown.push(new Location(x, y + 1));
				counter += land[x][y + 1].getCrownNum();
				continue;
			}

			// down
			if (x < 8 && crownNum[x + 1][y] == -1 && land[x][y].getType() == land[x + 1][y].getType()) {
				crownNum[x + 1][y] = -2;
				stackCrown.push(new Location(x + 1, y));
				counter += land[x + 1][y].getCrownNum();
				continue;
			}

			// right
			if (y > 0 && crownNum[x][y - 1] == -1 && land[x][y].getType() == land[x][y - 1].getType()) {
				crownNum[x][y - 1] = -2;
				stackCrown.push(new Location(x, y - 1));
				counter += land[x][y - 1].getCrownNum();
				continue;
			}
			stackCrown.pop(); // remove the top location in the stack
		}

		for (int i = 0; i < 9; i++) { // rows
			for (int j = 0; j < 9; j++) { // columns
				if (crownNum[i][j] == -2)
					crownNum[i][j] = counter;
			}
		}
		// Location elem = stackCrown.peek();
	}

	public int finalScore() {
		int finalScore = calculateScore();
		int landNum = 0;
		// if land is complet
		for (int i = 0; i < 9; i++) { // rows
			for (int j = 0; j < 9; j++) { // columns
				if (land[i][j].isOccupied()) {
					landNum++;
				}
			}
		}
		if (landNum == 25) {
			finalScore += 5;
			System.out.println("Land complet");
		}

		// if castle is in the middle
		int[] border = new int[2];
		boolean top = false;
		boolean left = false;

		for (int i = 0; i != land.length; i++) {
			for (int j = 0; j != land[i].length; j++) {
				if (land[i][j].isOccupied()) {
					border[0] = i; // top border
					top = true;
					break;
				}
			}
			if (top) {
				break;
			}
		}
		for (int i = 0; i != land.length; i++) {
			for (int j = 0; j != land[i].length; j++) {

				if (land[j][i].isOccupied()) {
					border[1] = i; // left border
					left = true;
					break;
				}
			}
			if (left) {
				break;
			}
		}

		if (land[border[0] + 2][border[1] + 2].getType() == Area.CASTLE) {
			finalScore += 10;
			System.out.println("Castle in the middle");
			this.castleCentered = true;

		}

		this.finalScore = finalScore;

		checkLand(border[0], border[1]);

		return finalScore;

	}
	
	public void checkLand(int borderTop, int borderLeft) {
		//System.out.println((borderTop + MAX_DIMENSION) + " " + (borderLeft + MAX_DIMENSION));
		int tmpEmptyAreaNum = 0;
		int tmpSingleEmptyAreaNum = 0;
		
		for (int i = borderTop; i != borderTop + MAX_DIMENSION; i++) {
			for (int j = borderLeft; j != borderLeft + MAX_DIMENSION; j++) {
				if (!land[i][j].isOccupied()) {
					tmpEmptyAreaNum++;

					if (isEmptyAreaSingle(i, j, borderTop, borderLeft)) {
						tmpSingleEmptyAreaNum++;
						//System.out.println(i + " " + j);
					}

				} else {
					totalCrownNum += land[i][j].getCrownNum();
				}
			}
		}
		emptyAreaNum = tmpEmptyAreaNum;
		singleEmptyAreaNum = tmpSingleEmptyAreaNum;
	}
	public boolean isEmptyAreaSingle(int i, int j, int borderTop, int borderLeft) {
		if (i == borderTop && j == borderLeft) {
			return land[i + 1][j].isOccupied() && land[i][j + 1].isOccupied();
		} else if (i == borderTop && j == borderLeft + MAX_DIMENSION - 1) {
			return land[i][j - 1].isOccupied() && land[i + 1][j].isOccupied();
		} else if (i == borderTop + MAX_DIMENSION - 1 && j == borderLeft) {
			return land[i - 1][j].isOccupied() && land[i][j + 1].isOccupied();
		} else if (i == borderTop + MAX_DIMENSION - 1 && j == borderLeft + MAX_DIMENSION - 1) {
			return land[i - 1][j].isOccupied() && land[i][j - 1].isOccupied();
		} else if (i == borderTop) {
			return land[i][j - 1].isOccupied() && land[i + 1][j].isOccupied() && land[i][j + 1].isOccupied();
		} else if (j == borderLeft) {
			return land[i - 1][j].isOccupied() && land[i + 1][j].isOccupied() && land[i][j + 1].isOccupied();
		} else if (i == borderTop + MAX_DIMENSION - 1) {
			return land[i - 1][j].isOccupied() && land[i][j - 1].isOccupied() && land[i][j + 1].isOccupied();
		} else if (j == borderLeft + MAX_DIMENSION - 1) {
			return land[i - 1][j].isOccupied() && land[i][j - 1].isOccupied() && land[i + 1][j].isOccupied();
		} else {
			return land[i - 1][j].isOccupied() && land[i][j - 1].isOccupied() && land[i + 1][j].isOccupied()
					&& land[i][j + 1].isOccupied();
		}

	}

	public void place(Domino domino) {

	}

	public int chooseBestDominoTurn1(List<Domino> dominoList) {
		return 0;
	}

	public int chooseBestDomino(Domino preDomino, int preX, int preY, int preDirection, List<Domino> dominoList) {
		return 0;
	}

	public int[] bestPosition(Domino domino) {
		int[] returnArray = new int[4];

		return returnArray;
	}

	public void printResultInCsv() throws IOException {

		FileWriter fw = null;

		try {
			fw = new FileWriter(new File("results.csv"), true);
			fw.append(finalScore + "," + castleCentered + "," + emptyAreaNum + "," + singleEmptyAreaNum + ","
					+ totalCrownNum + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			fw.close();
		}

	}
}
