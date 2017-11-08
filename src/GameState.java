import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public interface GameState {
	public void tick();
	public void paint(Graphics brocha);
	public void keyPressed(int k);
	public void keyReleased(int k);

	public void menu();
	public void corriendo();
	public void drawImage(Graphics2D g2d, BufferedImage currentImage, int i, int j);
}
