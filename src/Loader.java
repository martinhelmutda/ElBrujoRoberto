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

	private HashMap imagesMap; //Creamos un hash para guardar las imágenes con su nombre correspondiente
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
	
	
	//cuenta regresiva
		@Override
		public void paint(Graphics brocha) {
			if(contador<140) {
				contador++;
//				System.out.print(contador+", ");
				brocha.setColor(Color.blue);
				brocha.fillRect(0, 0, GamePanel.VWIDTH, GamePanel.VHEIGHT);
				brocha.setColor(Color.white);
				brocha.fillRect(100, 100, GamePanel.VWIDTH-200, GamePanel.VHEIGHT-200);
				brocha.setColor(Color.black);		
				brocha.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 500));
				
				if(contador<20) {
					brocha.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 90));
					board.drawCenteredString("Cuenta regresiva",GamePanel.VWIDTH,GamePanel.VHEIGHT,brocha);
				}else if(contador<40) brocha.drawString("5", 350, 510);
				else if(contador<60) brocha.drawString("4", 350, 510);
				else if(contador<80) brocha.drawString("3", 350, 510);
				else if(contador<100) brocha.drawString("2", 350, 510);
				else if(contador<120) brocha.drawString("1", 350, 510);
				else if(contador<140) {
					brocha.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 100));
					board.drawCenteredString("Comienza",GamePanel.VWIDTH,GamePanel.VHEIGHT,brocha);

				}
			}else {
			changeTurn();
			}
		
		}
	
		//Carga IMAGENES
	
	private void initLoader() {
		imagesMap = new HashMap();		//Iniciamos El cargador
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment(); //Optimizacion 
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		  
	}
	
	private void loadImagesFile(String fnm) { //Con el nombre de una imagen podemos llegar a su dirección en el disco
		String imsFNm = IMAGE_DIR + fnm;	//Tenemos un archivo que nos ayuda a reconocer el nombre de todas las imágenes a cargar
		System.out.println("Reading file: " + imsFNm);
		try {
			InputStream in = this.getClass().getResourceAsStream(imsFNm);
			BufferedReader br = new BufferedReader( new InputStreamReader(in));
			String line;
			char ch;	
			while((line = br.readLine()) != null) {//Ignoramos lo que estorba deldocumento
				if (line.length() == 0) 
					continue;
				if (line.startsWith("//"))  
					continue;
				ch = Character.toLowerCase( line.charAt(0) );
				if (ch == 'o')  // Una sola imagen
					getFileNameImage(line);
				else if (ch == 'g')  // Un grupo de imagenes
					getGroupImages(line); //Sirve para cargar varia a la vez. Es conveniente para aumentar velocidad de carga y/o dar secuencia numérica
				else
					System.out.println("No se reconoce la linea: " + line);
				}
			br.close();
			} 
		catch (IOException e){
			System.out.println("Error leyendo  archivo: " + imsFNm);
			System.exit(1);
		}
	}
	//----EN EL CASO DE UNA IMAGEN
	private void getFileNameImage(String line)
		
	
	  { StringTokenizer tokens = new StringTokenizer(line);

	    if (tokens.countTokens() != 2)
	      System.out.println("Número incorrecto de argumento " + line);
	    else {
	      tokens.nextToken();   
	      System.out.print("o Line: ");
	      loadSingleImage( tokens.nextToken() );
	    }
	  }  


	//métod más directo que no uso pero me sirve para hacer pruebas
	  public boolean loadSingleImage(String fnm)
	  {
	    String name = getPrefix(fnm);

	    if (imagesMap.containsKey(name)) {
	      System.out.println( "Error: " + name + "ya está en uso");
	      return false;
	    }

	    BufferedImage bi = loadImage(fnm);
	    if (bi != null) {
	      ArrayList imsList = new ArrayList();
	      imsList.add(bi);
	      imagesMap.put(name, imsList);
	      System.out.println("  Guardado " + name + "/" + fnm);
	      return true;
	    }
	    else
	      return false;
	  } 


	  private String getPrefix(String fnm) //obtenemos el prefijo del archivo
	  {
	    int posn;
	    if ((posn = fnm.lastIndexOf(".")) == -1) {
	      System.out.println("Prefijos no encontrados con el nombre: " + fnm);
	      return fnm;
	    }
	    else
	      return fnm.substring(0, posn);
	  } 
	  
/*Cargar imagenes numeradas*/
	  
	//AL DEJAR ESTE MÉTODO VA MÁS RÁPIDO

	  private void getGroupImages(String line)
	 
	  { StringTokenizer tokens = new StringTokenizer(line);

	    if (tokens.countTokens() < 3)
	      System.out.println("Número incorrecto de argumentos " + line);
	    else {
	      System.out.print("g Line: ");

	      String name = tokens.nextToken();

	      ArrayList fnms = new ArrayList();
	      fnms.add( tokens.nextToken() );  // lee nombres
	      while (tokens.hasMoreTokens()) //separa letras
	        fnms.add( tokens.nextToken() );

	      loadGroupImages(name, fnms);
	    }
	  }  


	  public int loadGroupImages(String name, ArrayList fnms)
	  /* Para cargar directamente
	  */
	  {
	    if (imagesMap.containsKey(name)) {
	      System.out.println( "Error: " + name + "ya está en uso");
	      return 0;
	    }

	    if (fnms.size() == 0) {
	      System.out.println("Lista de nombres de achivo vacías");
	      return 0;
	    }

	    BufferedImage bi;
	    ArrayList nms = new ArrayList();
	    ArrayList imsList = new ArrayList();
	    String nm, fnm;
	    int loadCount = 0;

	    System.out.println("  Agregando a " + name + "...");
	    System.out.print("  ");
	    for (int i=0; i < fnms.size(); i++) {    //Se agregan las imágenes
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
	      System.out.println("Ninguna imagen cargada para " + name);
	    else {
	      imagesMap.put(name, imsList);
	    }

	    return loadCount;
	  }  

	  public int loadGroupImages(String name, String[] fnms)
	  
	  {  
	    ArrayList al = new ArrayList( Arrays.asList(fnms) );
	    return loadGroupImages(name, al);  
	  }

/* Métodos de acceso a las imágenes cargadas*/
	  public BufferedImage getImage(String name)
	  /*
	   * Se Obtienenb TODAS las imágenes con ese nombre de pila 
	  */
	  {
	    ArrayList imsList = (ArrayList) imagesMap.get(name);
	    if (imsList == null) {
	      System.out.println("No hay imágenes bajo el nombre " + name);  
	      return null;
	    }

	    System.out.println("Regresar Imagenes guardadas con el nombre " + name);  //ESTE ES LA SEÑAL DE QUE TODO VA BIEN
	    return (BufferedImage) imsList.get(0);
	  }  





	  public BufferedImage getImage(String name, String fnmPrefix)
	  /* Obtenemos imágenes de mismo nombre y prefijo
	  */
	  {
	    ArrayList imsList = (ArrayList) imagesMap.get(name);
	    if (imsList == null) {
	      System.out.println("No hay imagenes bajo el nombre de " + name);  
	      return null;
	    }

	    int posn = getGroupPosition(name, fnmPrefix);
	    if (posn < 0) {
	      return (BufferedImage) imsList.get(0);   //REGRESA PRIMER IMAGEN
	    }

	    return (BufferedImage) imsList.get(posn);
	  }  


	  private int getGroupPosition(String name, String fnmPrefix)
	 //Busca en el hashmap por los nombres y arroja archivos con mismo prefijo
	  {
	    System.out.println("Ningun " + fnmPrefix + 
	                  " Grupo encontrado para " + name);  
	    return -1;
	  }  



	  public ArrayList getImages(String name)
	 //Regresa todas las imágenes
	  {
	    ArrayList imsList = (ArrayList) imagesMap.get(name);
	    if (imsList == null) {
	      System.out.println("Ninguna imagen guardada bajo el nombre de " + name);  
	      return null;
	    }

	    System.out.println("Regresando todas las imágenes con nombre: " + name);  
	    return imsList;
	  }  

	  public boolean isLoaded(String name)
	  // La clave del HashMAp es el nombre
	  {
	    ArrayList imsList = (ArrayList) imagesMap.get(name);
	    if (imsList == null)
	      return false;
	    return true;
	  }  


	  public int numImages(String name)
	  //¿Cuantas imagenes hay guardadas bajo ese nombre?
	  {
	    ArrayList imsList = (ArrayList) imagesMap.get(name);
	    if (imsList == null) {
	      System.out.println("No image(s) stored under " + name);  
	      return 0;
	    }
	    return imsList.size();
	  } 


	  // ------------------- Image Input ------------------

//Aquí se ingresan los resultados
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

	
	
	
	//________________________________________//


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
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeTurn() {
		// TODO Auto-generated method stub
		board.setState(StateFactory.getState(6, board));
		board.setState(StateFactory.getState(3, board));
		
	}

}
