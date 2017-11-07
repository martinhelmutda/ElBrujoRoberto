import java.awt.Graphics;


public interface GameState {
	public void tick();
	public void paint(Graphics brocha);
	public void keyPressed(int k);
	public void keyReleased(int k);

	public void menu();
	public void corriendo();
}
