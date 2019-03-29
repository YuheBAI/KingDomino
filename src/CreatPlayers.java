import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class CreatPlayers extends BasicGameState {
	
	private final static int ID = Game.CREAT_PLAYERS;
	  Font font;
	  TextField textField;



	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		font = font = new TrueTypeFont(new java.awt.Font(java.awt.Font.SERIF,java.awt.Font.BOLD , 26), false);
		//new UnicodeFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.ITALIC, 26));
	    textField = new TextField(gc, gc.getDefaultFont(), 400, 300, 300, 50);  
	    // <- i get a error here and the fix is to cast argument gc to GUIContext// i replaced your font in the constructor with the container's default font and it works.
	    textField.setText("Player");
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics graph) throws SlickException {
		textField.render(gc, graph);
		
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
