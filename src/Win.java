import java.awt.Color;
import java.awt.Graphics;


public class Win implements BoardState{
	Board board;
	public Win(Board tab) {
		this.board=tab;
		System.out.println("Ya hay un ganador");
	}

	


	@Override
	public void paint(Graphics brocha) {
		// TODO Auto-generated method stub
		brocha.setColor(Color.black);
		brocha.fillRect(0, 0, 400, 500);
	}


	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}




	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}
}
