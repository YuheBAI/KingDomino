import org.newdawn.slick.*;

public class Area {
	private int type;
	private int crownNum;
	private boolean isOccupied;

	public static Image crown;
	// Crown by Marek Polakovic from the Noun Project

	public Area() {
		this.type = 0;
		this.crownNum = 0;
		this.isOccupied = false;
	}

	public Area(int type) {
		this.type = type;
		this.crownNum = 0;
		this.isOccupied = true;
	}

	// types
	public final static int CHAMPS = 1;
	public final static int FORET = 2;
	public final static int MER = 3;
	public final static int PRAIRIE = 4;
	public final static int MINE = 5;
	public final static int MONTAGNE = 6;
	public final static int CASTLE = 100;

	// colors
	public final static Color CHAMPS_COLOR = Color.yellow;
	public final static Color FORET_COLOR = new Color(0, 100, 0);// darkgreen
	public final static Color MER_COLOR = Color.cyan;
	public final static Color PRAIRIE_COLOR = new Color(173, 255, 47); // greenyellow
	public final static Color MINE_COLOR = Color.gray;
	public final static Color MONTAGNE_COLOR = new Color(165, 42, 42); // brown
	public final static Color CASTLE_COLOR = Color.white;
	public final static Color EMPTY_COLOR = new Color(25, 25, 112);

	// Getter & Setter
	public int getType() {
		return type;
	}

	public int getCrownNum() {
		return crownNum;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setType(int num) {
		type = num;
	}

	public void setCrownNum(int num) {
		crownNum = num;
	}

	public void setStat(boolean stat) {
		isOccupied = stat;
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
		case CASTLE:
			return "Castle";
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
		case "Castle":
			return CASTLE;
		default:
			return 0;

		}
	}

	public Color typeToColor() {
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
		case CASTLE:
			return CASTLE_COLOR;
		default:
			return EMPTY_COLOR;

		}
	}

	public Image typeToImage() throws SlickException {
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
		case CASTLE:
			return Game.CASTLE_IMAGE;
		default:
		case 0:
			return Game.EMPTY_IMAGE;

		}
	}

	public void render(Graphics graphics, float x, float y) {
		// graphics.setColor(typeToColor());
		// graphics.fillRect(x, y, Game.dominoWidth, Game.dominoWidth);
		try {
			typeToImage().draw(x, y, Game.dominoWidth, Game.dominoWidth);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (crownNum != 0) {
			graphics.setColor(Color.white);
			graphics.drawString(String.valueOf(crownNum), x + Game.dominoWidth / 2, y + Game.dominoWidth / 2);
		}

	}

	public void renderMini(Graphics graphics, float x, float y) {
		graphics.setColor(typeToColor());
		graphics.fillRect(x, y, Game.dominoWidth * 0.2f, Game.dominoWidth * 0.2f);
		// graphics.drawString(String.valueOf(crownNum1), x + Game.dominoWidth / 2, y +
		// Game.dominoWidth / 2);
	}

	public static void main(String[] args) {

	}

}
