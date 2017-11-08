import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements Runnable, KeyListener, ActionListener,ImagesPlayerWatcher {

	private static final long serialVersionUID = 1L;
	static final int VWIDTH = 800;
	static final int VHEIGHT= 600;

	private volatile boolean running = false;//Volatile significa que puede ser cambiada por distintos threads
	private Thread animator;
	
	private int FPS=60;
	private long tiempoObj= 1000/FPS;
	
	private final static String IMS_FILE = "imsInfo.txt";
	private static final int PERIOD = 1000;
	 //-------------
	  private ImageLoader imsLoader;   // the image loader
	  private int counter;
	  private boolean justStarted;
	  
	  private GraphicsDevice gd;   // for reporting accl. memory usage
	  private int accelMemory;   
	  private DecimalFormat df;
	  
	  
	  private ImagesPlayer numbersPlayer;
	  
	  
	//------------------
	
	private GameStateContext contexto;
	
	
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
		df = new DecimalFormat("0.0");  // 1 dp

	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    gd = ge.getDefaultScreenDevice();

	    accelMemory = gd.getAvailableAcceleratedMemory();  // in bytes
	    System.out.println("Initial Acc. Mem.: " + 
	                   df.format( ((double)accelMemory)/(1024*1024) ) + " MB" );

	    imsLoader = new ImageLoader(IMS_FILE); 
	    
	    initImages();

	    counter = 0;
	    justStarted = true;

	    new Timer(PERIOD, this).start();  
	}
	
	private void initImages()
	  {
	    // initialize the 'o' image variables


	    /* Initialize ImagesPlayers for the 'n' and 's' images.
	       The 'numbers' sequence is not cycled, the other are. 
	    */
	    numbersPlayer = 
	         new ImagesPlayer("numbers", PERIOD, 6 , false, imsLoader);
	    numbersPlayer.setWatcher(this);     // report the sequence's end back here


	  }
	
	public void sequenceEnded(String imageName)
	  // called by ImagesPlayer when its images sequence has finished
	  {  System.out.println( imageName + " sequence has ended");  }

	 public void actionPerformed(ActionEvent e)
	  // triggered by the timer: update, repaint
	  { 
	    if (justStarted)   // don't do updates the first time through
	      justStarted = false;
	    else
	      imagesUpdate();

	    repaint();  
	  }
	
	  private void imagesUpdate()
	  { 
	    // numbered images ('n' images); using ImagesPlayer
	    numbersPlayer.updateTick();
	    if (counter%30 == 0)     // restart the image sequence periodically
	      numbersPlayer.restartAt(0);//número de inicio

	 

	    // grouped images ('g' images)
	    // The 'fighter' images are the only grouped images in this example.
	  
	  } 
	
	 
	
	  
	  
	  private void drawImage(Graphics2D g2d, BufferedImage im, int x, int y)
	   /* Draw the image, or a yellow box with ?? in it if there is no image. */
	   { 
	     if (im == null) {
	       // System.out.println("Null image supplied");
	       g2d.setColor(Color.yellow);
	       g2d.fillRect(x, y, 20, 20);
	       g2d.setColor(Color.black);
	       g2d.drawString("??", x+10, y+10);
	     }
	     else
	       g2d.drawImage(im, x, y, this);
	   } // end of drawImage()

	  private void reportAccelMemory()
	  // report any change in the amount of accelerated memory
	  {
	    int mem = gd.getAvailableAcceleratedMemory();   // in bytes
	    int memChange = mem - accelMemory;

	    if (memChange != 0) 
	      System.out.println(counter + ". Acc. Mem: " + 
	                df.format( ((double)accelMemory)/(1024*1024) ) + " MB; Change: " +
	                df.format( ((double)memChange)/1024 ) + " K");
	    accelMemory = mem;
	  }  // end of reportAcceleMemory()
//--------------------------PARTE DE CODIGO
	
	
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
		contexto.paint(dbg); 	//Pintamos las im�genes que el director administra
		
		reportAccelMemory();
		drawImage(g2d, numbersPlayer.getCurrentImage(), VWIDTH/2, VHEIGHT/2);
		counter = (counter + 1)% 100;   
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
