import java.util.*;

public class AI extends Player {

	int[][][] scoreList;

	public AI() {

	}

	public AI(String name, String kingColor, int kingNum) {
		this.playerName = name;
		this.id = PLAYER_ID;
		this.land = intialiseLand();
		this.kingColor = kingColor;
		this.kingNum = kingNum;
		this.playerType = "AI";
		PLAYER_ID++;
	}

	public int bestScore(Domino domino) {
		return 0;
	}

	// for turn 1
	public int chooseBestDominoTurn1(List<Domino> dominoList) {
		int max = bestPosition(dominoList.get(0))[0];
		int index = 0;
		for (Domino domino : dominoList) {
			if (bestPosition(domino)[0] > max) {
				max = bestPosition(domino)[0];
				index = dominoList.indexOf(domino);
			}
			domino.setDirection(Domino.D1);
		}
		return index;
	}

	// other turns
	public int chooseBestDomino(Domino preDomino, int preX, int preY, int preDirection, List<Domino> dominoList) {
		preDomino.setDirection(preDirection);
		placeDomino(preDomino, preX, preY);
		int max = bestPosition(dominoList.get(0))[0];
		int index = 0;
		for (Domino domino : dominoList) {
			if (bestPosition(domino)[0] > max) {
				max = bestPosition(domino)[0];
				index = dominoList.indexOf(domino);
			}
			domino.setDirection(Domino.D1);
		}
		removeDominoFromLand(preDomino, preX, preY);
		preDomino.setDirection(Domino.D1);
		return index;
	}

	public void putDomino(Domino domino) {
		int x = bestPosition(domino)[1];
		int y = bestPosition(domino)[2];
		int direction = bestPosition(domino)[3];

		domino.setDirection(direction);
		placeDomino(domino, x, y);
		printLand();
		System.out.println("Current score of player " + getId() + " is " + calculateScore());

	}

	public int[] bestPosition(Domino domino) {
		// detectPositions(domino);
		int[] returnArray = new int[4];
		if (detectPositions(domino)) {
			int max = scoreList[0][0][0];
			int[] index = new int[3];

			for (int i = 0; i < 9; i++) { // rows
				for (int j = 0; j < 9; j++) { // columns
					for (int k = 0; k < 4; k++) { // for direction 1 to 4
						if (scoreList[i][j][k] > max) {
							max = scoreList[i][j][k];
							index[0] = i;
							index[1] = j;
							index[2] = k;
						} else if (scoreList[i][j][k] == max) {
							// To maximise the chance where castle is in the middle
							if (Math.abs(i - 4) + Math.abs(j - 4) < Math.abs(index[0] - 4) + Math.abs(index[1] - 4)) {
								{
									max = scoreList[i][j][k];
									index[0] = i;
									index[1] = j;
									index[2] = k;
								}
							}
						}
					}
				}
			}
			returnArray[0] = max; // score
			returnArray[1] = index[0]; // x
			returnArray[2] = index[1]; // y
			returnArray[3] = index[2] + 1; // direction
		} else {
			returnArray[0] = -1; // score
			returnArray[1] = 0; // x
			returnArray[2] = 0; // y
			returnArray[3] = 1; // direction
		}
		return returnArray;
	}

	@Override
	public boolean detectPositions(Domino domino) {
		int availablePositions = 0;
		scoreList = new int[9][9][4];
		for (int i = 0; i < 9; i++) { // rows
			for (int j = 0; j < 9; j++) { // columns
				for (int k = 0; k < 4; k++) { // for direction 1 to 4
					scoreList[i][j][k] = -1; // initialisation
					domino.setDirection(k + 1);
					if (!isLandOccupied(domino, i, j) && isPlaceOk(domino, i, j)) {
						placeDomino(domino, i, j);
						if (isDimensionOk()) {
							availablePositions = availablePositions + 1;
							scoreList[i][j][k] = finalScore();
						}
						removeDominoFromLand(domino, i, j);
					}
				}
			}
		}
		if (availablePositions == 0) {
			System.out.println(
					"No position available for domino " + domino.getDominoNum() + "! You have to discard this domino");
			// discard domino code
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void place(Domino domino) {
		if (detectPositions(domino)) {
			putDomino(domino);
		} else {
			System.out.println("No position available! Domino discarded");
		}
	}
}
