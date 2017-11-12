import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;


public class Start implements BoardState {

	private Board board;
	
	private String[] inicio = {"¿Listo para comenzar?","Listo", "Seleccionar nivel"};
	private int select=1;
	
	public Start (Board tab) {
		this.board = tab;
		System.out.println("Listo para jugar?");
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
				changeTurn(true);
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
			brocha.setFont(new Font("Marker Felt", Font.PLAIN, 40));
			brocha.drawString(inicio[i], GamePanel.VWIDTH/2 -50, 250 + i*100); //Esto dibuja una palabra en la coordenada deseada
		}
	}


	@Override
	public void paintComponent(Graphics brocha) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void win() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void changeTurn(boolean correct) {
		board.setState(StateFactory.getState(6, board));
		board.setState(StateFactory.getState(2, board));
	}


	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
