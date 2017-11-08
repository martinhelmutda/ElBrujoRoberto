import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GameStateListo implements GameState{
	GameStateContext contextus;
	private String[] inicio = {"¿Listo para comenzar?","Listo", "Seleccionar nivel"};
	private int select=1;
	
	public GameStateListo(GameStateContext cont){
		this.contextus = cont;
		System.out.println("Somos aire");
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
		if(k == KeyEvent.VK_DOWN) {
			select++;
			if(select >= inicio.length) {	//Si la seleccion se pasa del numero de opciones, regresa al inicio
				select=1;
			}
			System.out.println("Me presionan");
		}else if(k == KeyEvent.VK_UP){
			System.out.println("También me presionan");
			select--;
			if(select<1) {
				select = inicio.length-1;
			}
		}else if(k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE) {
			if(select==1) {	//Inicio
				contextus.setState(StateFactory.getState(2, contextus));
			}
			else if(select==2) {	//Salir
				System.exit(0);
			}
		}
	}
	
	@Override
	public void paint(Graphics brocha) {
		// TODO Auto-generated method stub
		brocha.setColor(Color.green);
		brocha.fillRect(0,0,GamePanel.VWIDTH, GamePanel.VHEIGHT);
		for(int i=0;i<inicio.length;i++) { 	//Recorre e imprime todas las opciones
			if(i==select) {
				brocha.setColor(Color.blue);
			}else{
				brocha.setColor(Color.white);
			}
			brocha.setFont(new Font("Arial", Font.PLAIN, 40));
			brocha.drawString(inicio[i], GamePanel.VWIDTH/2 -50, 250 + i*100); //Esto dibuja una palabra en la coordenada deseada
		}
	}


	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void corriendo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawImage(Graphics2D g2d, BufferedImage currentImage, int i, int j) {
		// TODO Auto-generated method stub
		
	}
}
