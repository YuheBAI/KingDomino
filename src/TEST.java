import java.util.*;
import java.io.*;
public class TEST {

	public static void main(String[] args) {
		List<King> kingList = new ArrayList<>();
		King k =new King("red");
		kingList.add(k);
		Game.printKingList(kingList);

	}

}
