import java.awt.Graphics;


public interface BoardState {
	
	public void win();
	public void paint(Graphics brocha);
	public void keyPressed(int k);
	public void paintComponent(Graphics brocha);
	public void changeTurn();
	public void tick();
}

