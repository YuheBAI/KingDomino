import java.util.*;
import java.io.*;
import org.newdawn.slick.*;

import org.newdawn.slick.*;

public class King {
	private int id;
	private String color;
	static int KING_ID = 1;

	public static Image king;
	// King by icongeek from the Noun Project

	public King() {

	}

	public King(String color) {
		this.id = KING_ID;
		this.color = color;
		KING_ID++;
	}

	// Getter & Setter

	public int getId() {
		return id;
	}

	public String getColor() {
		return color;
	}
	/*
	 * public void setId(int id) { this.id = id; }
	 */

	public void setColor(String color) {
		this.color = color;
	}

	
	public Domino draw() {
		Domino d = null;
		Console.printDominoList(Console.dominoListDraw);
		boolean dominoOk = false;
		if (Console.getPlayerByKing(this).playerType.equals("Person")) {
			do {
				try {
					Console.printKingInfo(this);
					System.out.println("choisissez un domino");
					int n = Console.inputInt();
					d = Console.dominoListDraw.get(n);
					Console.dominoListDraw.remove(d);
					dominoOk = true;
				} catch (Exception e) {
					System.out.println("Erreur.");
					// e.printStackTrace();
				}
			} while (!dominoOk);
		} else { // AI
			if (Console.round == 1) {
				int n = Console.getPlayerByKing(this).chooseBestDominoTurn1(Console.dominoListDraw);
				d = Console.dominoListDraw.get(n);
				Console.dominoListDraw.remove(d);
			} else {
				System.out.println("other rounds");
				Domino pre = Console.kingToDomino.get(this);
				int x = Console.getPlayerByKing(this).bestPosition(pre)[1];
				int y = Console.getPlayerByKing(this).bestPosition(pre)[2];
				int direction = Console.getPlayerByKing(this).bestPosition(pre)[3];
				int n = Console.getPlayerByKing(this).chooseBestDomino(pre, x, y, direction, Console.dominoListDraw);
				d = Console.dominoListDraw.get(n);
				Console.dominoListDraw.remove(d);
				System.out.println("AI chose the domino " + d.getDominoNum());

			}
		}
		return d;
	}


	

	public Color color() {
		switch (color) {
		case "blue":
			return Color.blue;
		case "red":
			return Color.red;
		case "green":
			return Color.green;
		case "pink":
			return Color.pink;
		case "yellow":
			return Color.yellow;
		case "cyan":
			return Color.cyan;
		default:
			return null;
		}
	}

	public void render(Graphics graphics, float x, float y) {
		// graphics.fillRect(this.x, this.y, 200, 200);
		//graphics.drawImage(king, x, y,(int)(Game.dominoWidth),(int)(Game.dominoWidth), y, y, color());
		king.draw(x,y,Game.dominoWidth*1.5f,Game.dominoWidth*1.5f,color());
	}
	
	
	
	
	

}



