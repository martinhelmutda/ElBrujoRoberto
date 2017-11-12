import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class Win implements BoardState{
	Board board;
	public Win(Board tab) {
		this.board=tab;
		System.out.println("Ya hay un ganador");
	}

	


	@Override
	public void paint(Graphics brocha) {
		brocha.setFont(new Font("Marker Felt", Font.PLAIN, 60));
		board.drawCenteredString(board.getJugador(),GamePanel.VWIDTH,GamePanel.VHEIGHT+50,brocha);
		brocha.setFont(new Font("Marker Felt", Font.PLAIN, 20));
		board.drawCenteredString("El mago Roberto está impresionado",GamePanel.VWIDTH,900,brocha);
	}


	@Override
	public void keyPressed(int k) {
		
	}


	@Override
	public void paintComponent(Graphics brocha) {
		
	}


	@Override
	public void win() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void changeTurn() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}
}
