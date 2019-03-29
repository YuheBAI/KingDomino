import java.util.*;
import java.io.*;
import org.newdawn.slick.*;
//Load csv : https://stackoverflow.com/questions/14274259/read-csv-with-scanner
//https://stackoverflow.com/questions/6453723/how-to-read-csv-file-without-knowing-header-using-java

public class Domino {

	private int x;
	private int y;

	private int crownNum1;
	private int type1;
	private int crownNum2;
	private int type2;
	private int dominoNum;

	// KEYS
	private final static int UP = Game.UP;
	private final static int DOWN = Game.DOWN;
	private final static int TURN_LEFT = Game.LEFT;
	private final static int TURN_RIGHT = Game.RIGHT;
	// public final static int CONFIRM = Game.CONFIRM;

	public int direction;

	public static Image crown = Area.crown;

	// types
	public final static int CHAMPS = Area.CHAMPS;
	public final static int FORET = Area.FORET;
	public final static int MER = Area.MER;
	public final static int PRAIRIE = Area.PRAIRIE;
	public final static int MINE = Area.MINE;
	public final static int MONTAGNE = Area.MONTAGNE;

	// colors
	public final static Color CHAMPS_COLOR = Area.CHAMPS_COLOR;
	public final static Color FORET_COLOR = Area.FORET_COLOR;
	public final static Color MER_COLOR = Area.MER_COLOR;
	public final static Color PRAIRIE_COLOR = Area.PRAIRIE_COLOR;
	public final static Color MINE_COLOR = Area.MINE_COLOR;
	public final static Color MONTAGNE_COLOR = Area.MONTAGNE_COLOR;

	// directions
	public final static int D1 = 1;
	public final static int D2 = 2;
	public final static int D3 = 3;
	public final static int D4 = 4;

	public boolean chosed;

	public Domino() {

	}

	public Domino(int crownNum1, int type1, int crownNum2, int type2, int dominoNum) {
		this.crownNum1 = crownNum1;
		this.type1 = type1;
		this.crownNum2 = crownNum2;
		this.type2 = type2;
		this.dominoNum = dominoNum;
		this.direction = D1;
		this.chosed = false;
		this.x = Game.width / 2;
		this.y = Game.height / 2;

	}

	// getters
	public int getType1() {
		return type1;
	}

	public int getType2() {
		return type2;
	}

	public int getCrownNum1() {
		return crownNum1;
	}

	public int getCrownNum2() {
		return crownNum2;
	}

	public int getDominoNum() {
		return dominoNum;
	}

	public int getDirection() {
		return direction;
	}

	// setter
	public void setDirection(int d) {
		this.direction = d;
	}

	// valid check
	public static boolean crownNumValid(int crownNum) {
		return 0 <= crownNum && crownNum <= 3;
	}

	public static boolean typeValid(int type) {
		return CHAMPS <= type && type <= MONTAGNE;
	}

	public static boolean dominoNumValid(int dominoNum) {
		return 1 <= dominoNum && dominoNum <= 48;
	}

	public boolean dominoValid() {
		return typeValid(type1) || typeValid(type2) || crownNumValid(crownNum1) || crownNumValid(crownNum2)
				|| dominoNumValid(dominoNum);
	}

	public static boolean directionValid(int dir) {
		return D1 <= dir && dir <= D4;
	}

	public static String typeToString(int type) {
		switch (type) {
		case CHAMPS:
			return "Champs";
		case FORET:
			return "Foret";
		case MER:
			return "Mer";
		case PRAIRIE:
			return "Prairie";
		case MINE:
			return "Mine";
		case MONTAGNE:
			return "Montagne";
		default:
			return null;

		}
	}

	public static int typeToInt(String type) {
		switch (type) {
		case "Champs":
			return CHAMPS;
		case "Foret":
			return FORET;
		case "Mer":
			return MER;
		case "Prairie":
			return PRAIRIE;
		case "Mine":
			return MINE;
		case "Montagne":
			return MONTAGNE;
		default:
			return 0;

		}
	}

	public void turnDomino() {
		/*
		 * if (D1 <= direction && direction < D4) { direction++; } else { direction =
		 * D1; }
		 */
		boolean over = false;
		do {
			System.out.println("Turn Domino ? Y/N");

			switch (Console.inputString()) {
			case "Y":
				direction = direction == D4 ? D1 : ++direction;
				break;
			case "N":
				over = true;
				break;
			default:
				System.out.println("Error !");
				break;

			}
		} while (!over);

	}

	public void turnDminoRight() {
		direction = direction == D4 ? D1 : ++direction;
	}

	public void turnDominoLeft() {
		direction = direction == D1 ? D4 : --direction;
	}

	public void printDominoInfo() {
		System.out.println("Domino No." + dominoNum);
		System.out.println("Type 1 : " + typeToString(type1));
		System.out.println("crown number 1 : " + crownNum1);
		System.out.println("Type 2 : " + typeToString(type2));
		System.out.println("crown number 2 : " + crownNum2);
		System.out.println();
	}

	public static Color typeToColor(int type) {
		switch (type) {
		case CHAMPS:
			return CHAMPS_COLOR;
		case FORET:
			return FORET_COLOR;
		case MER:
			return MER_COLOR;
		case PRAIRIE:
			return PRAIRIE_COLOR;
		case MINE:
			return MINE_COLOR;
		case MONTAGNE:
			return MONTAGNE_COLOR;
		default:
			return null;

		}
	}

	public static Image typeToImage(int type) throws SlickException {
		switch (type) {
		case CHAMPS:
			return Game.CHAMPS_IMAGE;
		case FORET:
			return Game.FORET_IMAGE;
		case MER:
			return Game.MER_IMAGE;
		case PRAIRIE:
			return Game.PRAIRIE_IMAGE;
		case MINE:
			return Game.MINE_IMAGE;
		case MONTAGNE:
			return Game.MONTAGNE_IMAGE;
		default:
			return new Image(Game.dominoWidth, Game.dominoWidth);

		}
	}

	public void update(Input input) {
		
		this.x = input.getMouseX();
		this.y = input.getMouseY();

		if (input.isKeyPressed(TURN_LEFT)) {
			this.turnDominoLeft();
		}
		if (input.isKeyPressed(TURN_RIGHT) || input.isMousePressed(input.MOUSE_RIGHT_BUTTON)) {
			this.turnDminoRight();
		}
	}

	public void renderFixed(Graphics graphics, float x, float y) throws SlickException {

		graphics.setColor(Game.ALTERNATIVE_MESSAGE);
		graphics.drawString("No." + String.valueOf(dominoNum), x, y - Game.dominoWidth * 0.5f);
		typeToImage(type1).draw(x, y, Game.dominoWidth, Game.dominoWidth);
		graphics.setColor(Game.MESSAGE_COLOR);
		graphics.drawString(String.valueOf(crownNum1), x + Game.dominoWidth / 2, y + Game.dominoWidth / 2);

		
		typeToImage(type2).draw(x + Game.dominoWidth, y, Game.dominoWidth, Game.dominoWidth);
		graphics.setColor(Game.MESSAGE_COLOR);
		graphics.drawString(String.valueOf(crownNum2), x + 3 * Game.dominoWidth / 2, y + Game.dominoWidth / 2);

	}

	public void render(Graphics graphics) throws SlickException {

		typeToImage(type1).draw(x, y, Game.dominoWidth, Game.dominoWidth);
		graphics.setColor(Game.MESSAGE_COLOR);
		graphics.drawString(String.valueOf(crownNum1), x + Game.dominoWidth / 2, y + Game.dominoWidth / 2);

		switch (direction) {
		case D1:
			
			typeToImage(type2).draw(x + Game.dominoWidth, y, Game.dominoWidth, Game.dominoWidth);
			graphics.setColor(Game.MESSAGE_COLOR);
			graphics.drawString(String.valueOf(crownNum2), x + 3 * Game.dominoWidth / 2, y + Game.dominoWidth / 2);
			break;

		case D2:
			typeToImage(type2).draw(x, y + Game.dominoWidth, Game.dominoWidth, Game.dominoWidth);
			graphics.setColor(Game.MESSAGE_COLOR);
			graphics.drawString(String.valueOf(crownNum2), x + Game.dominoWidth / 2, y + 3 * Game.dominoWidth / 2);
			break;

		case D3:
			
			typeToImage(type2).draw(x - Game.dominoWidth, y, Game.dominoWidth, Game.dominoWidth);
			graphics.setColor(Game.MESSAGE_COLOR);
			graphics.drawString(String.valueOf(crownNum2), x - Game.dominoWidth / 2, y + Game.dominoWidth / 2);
			break;

		case D4:
			
			typeToImage(type2).draw(x, y - Game.dominoWidth, Game.dominoWidth, Game.dominoWidth);
			graphics.setColor(Game.MESSAGE_COLOR);
			graphics.drawString(String.valueOf(crownNum2), x + Game.dominoWidth / 2, y - Game.dominoWidth / 2);
			break;

		}
	}

	
		
	public static void main(String[] args) throws FileNotFoundException {

	}

}
