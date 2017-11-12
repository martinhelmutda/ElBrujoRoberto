import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;


public class Loader implements BoardState{
	private Board board;
	
	private final static String IMAGE_DIR = "images/";

	private HashMap imagesMap; 
	private GraphicsConfiguration gc;
	private int contador;
	
	
	public Loader(Board tab) {
		this.board = tab;
		System.out.println("Cargando Imágenes");
	}
	
	public Loader(String fnm) {
		initLoader();
	    loadImagesFile(fnm);
	}
	
	public Loader() {
		initLoader();
	}
	//------------------LOAD------------------//
	
	private void initLoader() {
		imagesMap = new HashMap();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		  
	}
	
	private void loadImagesFile(String fnm) {
		String imsFNm = IMAGE_DIR + fnm;
		System.out.println("Reading file: " + imsFNm);
		try {
			InputStream in = this.getClass().getResourceAsStream(imsFNm);
			BufferedReader br = new BufferedReader( new InputStreamReader(in));
	      // BufferedReader br = new BufferedReader( new FileReader(imsFNm));
			String line;
			char ch;
			while((line = br.readLine()) != null) {
				if (line.length() == 0)  // blank line
					continue;
				if (line.startsWith("//"))   // comment
					continue;
				ch = Character.toLowerCase( line.charAt(0) );
				if (ch == 'o')  // a single image
					getFileNameImage(line);
				else if (ch == 'g')  // a group of images
					getGroupImages(line);
				else
					System.out.println("Do not recognize line: " + line);
				}
			br.close();
			} 
		catch (IOException e){
			System.out.println("Error reading file: " + imsFNm);
			System.exit(1);
		}
	}
	
	private void getFileNameImage(String line)
	  /* format:
	        o <fnm>
	  */
	  { StringTokenizer tokens = new StringTokenizer(line);

	    if (tokens.countTokens() != 2)
	      System.out.println("Wrong no. of arguments for " + line);
	    else {
	      tokens.nextToken();    // skip command label
	      System.out.print("o Line: ");
	      loadSingleImage( tokens.nextToken() );
	    }
	  }  // end of getFileNameImage()


	  public boolean loadSingleImage(String fnm)
	  // can be called directly
	  {
	    String name = getPrefix(fnm);

	    if (imagesMap.containsKey(name)) {
	      System.out.println( "Error: " + name + "already used");
	      return false;
	    }

	    BufferedImage bi = loadImage(fnm);
	    if (bi != null) {
	      ArrayList imsList = new ArrayList();
	      imsList.add(bi);
	      imagesMap.put(name, imsList);
	      System.out.println("  Stored " + name + "/" + fnm);
	      return true;
	    }
	    else
	      return false;
	  }  // end of loadSingleImage()


	  private String getPrefix(String fnm)
	  // extract name before '.' of filename
	  {
	    int posn;
	    if ((posn = fnm.lastIndexOf(".")) == -1) {
	      System.out.println("No prefix found for filename: " + fnm);
	      return fnm;
	    }
	    else
	      return fnm.substring(0, posn);
	  } // end of getPrefix()
	  
/*Cargar imagenes numeradas*/
	  
	//AL DEJAR ESTE MÉTODO VA MÁS RÁPIDO

	  private void getGroupImages(String line)
	  /* format:
	        g <name> <fnm>  [ <fnm> ]*
	  */
	  { StringTokenizer tokens = new StringTokenizer(line);

	    if (tokens.countTokens() < 3)
	      System.out.println("Wrong no. of arguments for " + line);
	    else {
	      tokens.nextToken();    // skip command label
	      System.out.print("g Line: ");

	      String name = tokens.nextToken();

	      ArrayList fnms = new ArrayList();
	      fnms.add( tokens.nextToken() );  // read filenames
	      while (tokens.hasMoreTokens())
	        fnms.add( tokens.nextToken() );

	      loadGroupImages(name, fnms);
	    }
	  }  // end of getGroupImages()



	  public int loadGroupImages(String name, ArrayList fnms)
	  /* Can be called directly to load a group of images, whose
	     filenames are stored in the ArrayList <fnms>. They will
	     be stored under the 'g' name <name>.
	  */
	  {
	    if (imagesMap.containsKey(name)) {
	      System.out.println( "Error: " + name + "already used");
	      return 0;
	    }

	    if (fnms.size() == 0) {
	      System.out.println("List of filenames is empty");
	      return 0;
	    }

	    BufferedImage bi;
	    ArrayList nms = new ArrayList();
	    ArrayList imsList = new ArrayList();
	    String nm, fnm;
	    int loadCount = 0;

	    System.out.println("  Adding to " + name + "...");
	    System.out.print("  ");
	    for (int i=0; i < fnms.size(); i++) {    // load the files
	      fnm = (String) fnms.get(i);
	      nm = getPrefix(fnm);
	      if ((bi = loadImage(fnm)) != null) {
	        loadCount++;
	        imsList.add(bi);
	        nms.add( nm );
	        System.out.print(nm + "/" + fnm + " ");
	      }
	    }
	    System.out.println();

	    if (loadCount == 0)
	      System.out.println("No images loaded for " + name);
	    else {
	      imagesMap.put(name, imsList);
	    }

	    return loadCount;
	  }  // end of loadGroupImages()


	  public int loadGroupImages(String name, String[] fnms)
	  // supply the group filenames in an array
	  {  
	    ArrayList al = new ArrayList( Arrays.asList(fnms) );
	    return loadGroupImages(name, al);  
	  }

/* access methods*/
	  public BufferedImage getImage(String name)
	  /* Get the image associated with <name>. If there are
	     several images stored under that name, return the 
	     first one in the list.
	  */
	  {
	    ArrayList imsList = (ArrayList) imagesMap.get(name);
	    if (imsList == null) {
	      System.out.println("No hay imágenes bajo el nombre " + name);  
	      return null;
	    }

	    System.out.println("Returning image stored under " + name);  
	    return (BufferedImage) imsList.get(0);
	  }  // end of getImage() with name input;





	  public BufferedImage getImage(String name, String fnmPrefix)
	  /* Get the image associated with the group <name> and filename
	     prefix <fnmPrefix>. 
	  */
	  {
	    ArrayList imsList = (ArrayList) imagesMap.get(name);
	    if (imsList == null) {
	      System.out.println("No image(s) stored under " + name);  
	      return null;
	    }

	    int posn = getGroupPosition(name, fnmPrefix);
	    if (posn < 0) {
	      // System.out.println("Returning image at position 0"); 
	      return (BufferedImage) imsList.get(0);   // return first image
	    }

	    // System.out.println("Returning " + name + 
	    //                        " image with pair name " + fnmPrefix);  
	    return (BufferedImage) imsList.get(posn);
	  }  // end of getImage() with fnmPrefix input;



	  private int getGroupPosition(String name, String fnmPrefix)
	  /* Search the hashmap entry for <name>, looking for <fnmPrefix>.
	     Return its position in the list, or -1.
	  */
	  {


	    System.out.println("No " + fnmPrefix + 
	                  " group name found for " + name);  
	    return -1;
	  }  // end of getGroupPosition()



	  public ArrayList getImages(String name)
	  // return all the BufferedImages for the given name
	  {
	    ArrayList imsList = (ArrayList) imagesMap.get(name);
	    if (imsList == null) {
	      System.out.println("No image(s) stored under " + name);  
	      return null;
	    }

	    System.out.println("Returning all images stored under " + name);  
	    return imsList;
	  }  // end of getImages();


	  public boolean isLoaded(String name)
	  // is <name> a key in the imagesMap hashMap?
	  {
	    ArrayList imsList = (ArrayList) imagesMap.get(name);
	    if (imsList == null)
	      return false;
	    return true;
	  }  // end of isLoaded()


	  public int numImages(String name)
	  // how many images are stored under <name>?
	  {
	    ArrayList imsList = (ArrayList) imagesMap.get(name);
	    if (imsList == null) {
	      System.out.println("No image(s) stored under " + name);  
	      return 0;
	    }
	    return imsList.size();
	  } // end of numImages()


	  // ------------------- Image Input ------------------


	   public BufferedImage loadImage(String fnm) 
	   {
	     try {
	       BufferedImage im =  ImageIO.read( 
	                      getClass().getResource(IMAGE_DIR + fnm) );

	       int transparency = im.getColorModel().getTransparency();
	       BufferedImage copy =  gc.createCompatibleImage(
	                                im.getWidth(), im.getHeight(),
			                        transparency );
	       Graphics2D g2d = copy.createGraphics();


	       // copy image
	       g2d.drawImage(im,0,0,null);
	       g2d.dispose();
	       return copy;
	     } 
	     catch(IOException e) {
	       System.out.println("Load Image error for " +
	                     IMAGE_DIR + "/" + fnm + ":\n" + e); 
	       return null;
	     }
	  } // end of loadImage() using ImageIO

	
	
	
	//________________________________________//

	@Override
	public void paint(Graphics brocha) {
		if(contador<140) {
			contador++;
//			System.out.print(contador+", ");
			brocha.setColor(Color.blue);
			brocha.fillRect(0, 0, GamePanel.VWIDTH, GamePanel.VHEIGHT);
			brocha.setColor(Color.black);		
			brocha.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 500));
			
			if(contador<20) {
				brocha.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 100));
				brocha.drawString("¿Están Listos?", 240, 300);
			}else if(contador<40) brocha.drawString("5", 380, 500);
			else if(contador<60) brocha.drawString("4", 380, 500);
			else if(contador<80) brocha.drawString("3", 380, 500);
			else if(contador<100) brocha.drawString("2", 380, 500);
			else if(contador<120) brocha.drawString("1", 380, 500);
			else if(contador<140) {
				brocha.setFont(new Font("Impact", Font.PLAIN, 100));
				brocha.drawString("Comienza", 270, 300);
			}
		}else {
		changeTurn(true);
		}
	
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
		board.setState(StateFactory.getState(6, board));
		board.setState(StateFactory.getState(3, board));
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
