import java.awt.Graphics;



public class GameStateContext {
	private GameState actual; //Necesario para setState
	
	public GameStateContext(){
		actual= StateFactory.getState(1,this); //Al ser invocado por primera vez sale
		System.out.println("Contextualmente esto es correcto");
	}
	
	public void setState(GameState st) {
		actual=st;
		System.out.println("Esta corrindo el: " + actual);
	}
	
	public void tick() {
		// TODO Auto-generated method stub
		actual.tick();
	}

	public void keyPressed(int keyCode) {
		// TODO Auto-generated method stub
		actual.keyPressed(keyCode);
	}

	public void keyReleased(int keyCode) {
		// TODO Auto-generated method stub
		actual.keyReleased(keyCode);
	}

	public void paint(Graphics brocha) {
		// TODO Auto-generated method stub
		actual.paint(brocha);
	}

}
