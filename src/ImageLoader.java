import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

public class ImageLoader {
	private final static String IMAGE_DIR = "Images/";

	private HashMap imagesMap; 
	
	private GraphicsConfiguration gc;


	public ImageLoader(String fnm){
		initLoader();
		loadImagesFile(fnm);
	}  

	public ImageLoader(){initLoader();} 


	private void initLoader(){
		imagesMap = new HashMap();
		
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
	} 

	  private void loadImagesFile(String fnm) {
	  /* Formats:
	        o <fnm>                     // a single image
	        n <fnm*.ext> <number>       // a numbered sequence of images
	    
	     and blank lines and comment lines.
	  */ 
		  String imsFNm = IMAGE_DIR + fnm;
		  System.out.println("Reading file: " + imsFNm);
		  try {
			 InputStream in = this.getClass().getResourceAsStream(imsFNm);
			  BufferedReader br = new BufferedReader( new InputStreamReader(in));
			  String line;
			  char ch;
			  while((line = br.readLine()) != null) {
				  if (line.length() == 0) 
					  continue;
				  if (line.startsWith("//")) 
					  continue;
				  ch = Character.toLowerCase( line.charAt(0) );
				  if (ch == 'n') 
					  getNumberedImages(line);
				  }
			  br.close();
		  	} 
		  	catch (IOException e){
		  		System.out.println("Error reading file: " + imsFNm);
		  		System.exit(1);
	    }
	  } 


	  // --------- load a single image -------------------------------





	  private String getPrefix(String fnm){  //Antes del nombre
		  int posn;
		  if ((posn = fnm.lastIndexOf(".")) == -1) {
			  System.out.println("No prefix found for filename: " + fnm);
			  return fnm;
			  }
		  else return fnm.substring(0, posn);
		  }
	  // --------- load numbered images -------------------------------
	  private void getNumberedImages(String line) {
	  /* format:
	        n <fnm*.ext> <number>
	  */
		  StringTokenizer tokens = new StringTokenizer(line);
		  if (tokens.countTokens() != 3)
			  System.out.println("Wrong no. of arguments for " + line);
		  else {
			  tokens.nextToken();    // skip command label
			  System.out.print("n Line: ");

			  //DAR FORMATO
	      String fnm = tokens.nextToken();
	      int number = -1;
	      try {
	        number = Integer.parseInt( tokens.nextToken() );
	      }
	      catch(Exception e)
	      { System.out.println("Number is incorrect for " + line);  }

	      loadNumImages(fnm, number);
	    }
	  }  // end of getNumberedImages()



	  public int loadNumImages(String fnm, int number)
	  /*  Can be called directly.
	      fnm is the filename argument in:
	           n <f*.ext> <number>
	  */
	  {
	    String prefix = null;
	    String postfix = null;
	    int starPosn = fnm.lastIndexOf("*");   // find the '*'
	    if (starPosn == -1) {
	      System.out.println("No '*' in filename: " + fnm);
	      prefix = getPrefix(fnm);
	    }
	    else {   // treat the fnm as prefix + "*" + postfix
	      prefix = fnm.substring(0, starPosn);
	      postfix = fnm.substring(starPosn+1);
	    }

	    if (imagesMap.containsKey(prefix)) {
	      System.out.println( "Error: " + prefix + "already used");
	      return 0;
	    }

	    return loadNumImages(prefix, postfix, number);
	  }  // end of loadNumImages()



	  private int loadNumImages(String prefix, String postfix, int number){ 
	    String imFnm;
	    BufferedImage bi;
	    ArrayList imsList = new ArrayList();
	    int loadCount = 0;

	    if (number <= 0) {
	      System.out.println("Error: Number <= 0: " + number);
	      imFnm = prefix + postfix;
	      if ((bi = loadImage(imFnm)) != null) {
	        loadCount++;
	        imsList.add(bi);
	        System.out.println("  Stored " + prefix + "/" + imFnm);
	      }
	    }
	    else {   // load prefix + <i> + postfix, where i = 0 to <number-1>
	      System.out.print("  Adding " + prefix + "/" + 
	                           prefix + "*" + postfix + "... ");
	      for(int i=0; i < number; i++) {
	        imFnm = prefix + i + postfix;
	        if ((bi = loadImage(imFnm)) != null) {
	          loadCount++;
	          imsList.add(bi);
	          System.out.print(i + " ");
	        }
	      }
	      System.out.println();
	    }

	    if (loadCount == 0)
	      System.out.println("No images loaded for " + prefix);
	    else 
	      imagesMap.put(prefix, imsList);

	    return loadCount;
	  }  // end of loadNumImages()



	  


	  

	  // ------ grouped filename seq. of images ---------



	  // ------------------ access methods -------------------

	  



	  public BufferedImage getImage(String name, int posn)
	  /* Get the image associated with <name> at position <posn>
	    in its list. If <posn> is < 0 then return the first image
	    in the list. If posn is bigger than the list's size, then
	    calculate its value modulo the size.
	  */
	  {
	    ArrayList imsList = (ArrayList) imagesMap.get(name);
	    if (imsList == null) {
	      System.out.println("No image(s) stored under " + name);  
	      return null;
	    }

	    int size = imsList.size();
	    if (posn < 0) {
	      // System.out.println("No " + name + " image at position " + posn +
	      //                      "; return position 0"); 
	      return (BufferedImage) imsList.get(0);   // return first image
	    }
	    else if (posn >= size) {
	      // System.out.println("No " + name + " image at position " + posn); 
	      int newPosn = posn % size;   // modulo
	      // System.out.println("Return image at position " + newPosn); 
	      return (BufferedImage) imsList.get(newPosn);
	    }

	    // System.out.println("Returning " + name + " image at position " + posn);  
	    return (BufferedImage) imsList.get(posn);
	  }  // end of getImage() with posn input;



	  

	   




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


	       g2d.drawImage(im,0,0,null);
	       g2d.dispose();
	       return copy;
	     } 
	     catch(IOException e) {
	       System.out.println("Load Image error for " +
	                     IMAGE_DIR + "/" + fnm + ":\n" + e); 
	       return null;
	     }
	  } 
	  
}
