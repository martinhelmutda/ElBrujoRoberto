import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int VWIDTH = 800;
	static final int VHEIGHT= 600;

	private volatile boolean running = false;//Volatile significa que puede ser cambiada por distintos threads
	private Thread animator;
	
	private int FPS=60;
	private long tiempoObj= 1000/FPS;
	
	private GameStateContext contexto;
	
	private ImagesPlayer numbersPlayer;
	
	public GamePanel() {
		setPreferredSize(new Dimension(VWIDTH,VHEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		
		addMouseListener( new MouseAdapter() { 
			public void mousePressed(MouseEvent e) { 
				testPress(e.getX(), e.getY()); }
		});
		
		contexto = new GameStateContext();
	}
	
	public void addNotify(){
		super.addNotify();
		start();
	}
	
	public void start() {
		running=true;
		animator = new Thread(this);
		animator.start();
	}
	
	
	@Override
	public void run() {
		
		 long inicio, transcurso, espera;
		 while(running) {
			 inicio=System.nanoTime();	//Le pedimos al sistema la hora			 
			 transcurso=System.nanoTime()-inicio;
			 espera = tiempoObj - transcurso / 1000000;
			 
			 tick();
			 repaint();
			 
			 if (espera <= 0) {

				 espera=10;
			 }	
			 
			 try {
				 Thread.sleep(espera);
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
		 }
		 
	}



	private void tick() {
		// TODO Auto-generated method stub
		contexto.tick();
	}
	
	private void testPress(int x, int y) {
	}
	
	public void paintComponent(Graphics dbg) {
		super.paintComponent(dbg);
		Graphics2D g2d = (Graphics2D)dbg; 
		dbg.clearRect(0, 0, VWIDTH, VHEIGHT);
		contexto.paint(dbg); 	//Pintamos las im�genes que el director administra
	}

	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		contexto.keyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		contexto.keyReleased(e.getKeyCode());
	}

	
}
