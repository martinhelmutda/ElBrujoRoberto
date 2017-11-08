import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.text.DecimalFormat;
import javax.swing.*;

public class GameStateJugando extends JPanel implements GameState, ActionListener, ImagesPlayerWatcher {
	GameStateContext contextus;
	private final static String IMS_FILE = "imsInfo.txt";
	private static final int PERIOD = 1000;
	
	private static final int PWIDTH = 350;     // size of this panel
	private static final int PHEIGHT = 400;
	
	
	private ImageLoader imsLoader;   // the image loader
	private int counter;
	private boolean justStarted;
	
	
	private GraphicsDevice gd;   // for reporting accl. memory usage
	private int accelMemory;   
	private DecimalFormat df;
	
	
	
	
	private ImagesPlayer numbersPlayer;
	
	public GameStateJugando (GameStateContext cont) {
		this.contextus = cont;
		System.out.println("Entre al juego");
		
		df = new DecimalFormat("0.0");  // 1 dp

		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    gd = ge.getDefaultScreenDevice();

		    accelMemory = gd.getAvailableAcceleratedMemory();  // in bytes
		    System.out.println("Initial Acc. Mem.: " + 
		                   df.format( ((double)accelMemory)/(1024*1024) ) + " MB" );


		    // load and initialise the images
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


	  }  // end of initImages()


	  public void sequenceEnded(String imageName)
	  // called by ImagesPlayer when its images sequence has finished
	  {  System.out.println( imageName + " sequence has ended");  }



	  public void actionPerformed(ActionEvent e)
	  // triggered by the timer: update, repaint
	  { 
	    if (justStarted)   // don't do updates the first time through
	      justStarted = false;
	    else {
	      imagesUpdate();
	     }
	    repaint();
	  } // end of actionPerformed()



	  private void imagesUpdate()
	  { 
	    // numbered images ('n' images); using ImagesPlayer
	    numbersPlayer.updateTick();
	    if (counter%30 == 0)     // restart the image sequence periodically
	      numbersPlayer.restartAt(0);//número de inicio

	  } // end of imagesUpdate()



	  


	  public void paintComponent(Graphics g)

	   {
	     super.paintComponent(g);
	     Graphics2D g2d = (Graphics2D)g; 

	     // clear the background
	     g2d.setColor(Color.blue);
	     g2d.fillRect(0, 0, PWIDTH, PHEIGHT);
	 
	     drawImage(g2d, numbersPlayer.getCurrentImage(), PWIDTH/2, PHEIGHT/2);

	     reportAccelMemory();

	     // increment the counter, modulo 100
	     counter = (counter + 1)% 100;    // 0-99 is a large enough range
	   } // end of paintComponent()



	   public void drawImage(Graphics2D g2d, BufferedImage im, int x, int y)
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
	   } 



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
	  }  
	
	
	
	
	
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
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
	public void paint(Graphics brocha) {
		// TODO Auto-generated method stub
		brocha.setColor(Color.black);
		brocha.fillRect(3, 60, 500, 600);
		
	}



}
