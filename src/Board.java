import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

//import players.Player;


public class Board extends JPanel{
	
	/*Contiene la parte de imagen y letras en el tablero, sin embargo, las imágenes ya se han prcargado anteriormente. También 
	 * sabe si ya ganó alguien
	 * LOS MÉTODOS QU ESTÁN AQUÍ SON UNICAMENTE PARA IMPRIMIR BONITO O SABER EL ESTADO DE LAS CLASES Y MENSAJES
	 * */
	
	private BoardState actual;
	
	private String[] palabra ={"delfin","elefante","escalera","escuela", "magico", "serpiente", "jirafa", "caballo","flores", "mochila"};
	public String[] letras = {"a", "b", "c", "d", "e", "f", "g", "h", "i", 
								"j", "k", "l", "m", "n", "ñ", "o", "p", "q",
								"r", "s", "t", "u", "v", "w", "x", "y", "z"} ;

	private int selection = (int) (Math.random() * 10);
	private String selectioned;
	private String spaceWord="";
	private String tryWord="";
	private final static String IMS_FILE = "imsInfo.txt";
	private Loader imsLoader;
	private BufferedImage board, mago, pollo, panda, gana;
	private int x, y;
	private boolean palabraActivated, pressed=false, isWin=false;
	private String message="";
	private String jugador="";
	
	
	private Rectangle rect = new Rectangle(GamePanel.VWIDTH-163,438,125,195);
	
	public Board() {
		actual= StateFactory.getState(1, this);
		System.out.println("Contextualmente funcionando ;) ");
		
		imsLoader = new Loader(IMS_FILE);
		initImages();
		
		spacingWord();
		spacingTryWord();
	}
	
	
	public void setState(BoardState bs) {
		actual=bs;
	}
	
// -----------_AREA GRAFICA_---------	

	private void initImages(){
		board = imsLoader.getImage("board");
		mago = imsLoader.getImage("MagoRoberto");
		pollo = imsLoader.getImage("pollo");
		panda = imsLoader.getImage("panda");
		gana = imsLoader.getImage("ganaste");
	}
	
	@Override
	
	public void paintComponent(Graphics brocha) {
		Graphics2D g2d = (Graphics2D)brocha; 
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		g2d.setColor(Color.blue);
		g2d.fillRect(0, 0, 40,20);
		
		//DIBUJITOS
		//drawImage(g2d, board, 0,0);
		
		
		actual.paintComponent(brocha);
		
	}

	private void drawImage(Graphics2D g2d, BufferedImage im, int x, int y) {
		// TODO Auto-generated method stub
		if (im == null) {
	         System.out.println("Null image supplied");
	         g2d.setColor(Color.yellow);
	         g2d.fillRect(x, y, 20, 20);
	         g2d.setColor(Color.black);
	         g2d.drawString("??", x+10, y+10);
	       }
	       else
	         g2d.drawImage(im, x, y, this);
	   } // end of drawImage()
	

	public void paint(Graphics brocha) {
		brocha.setColor(Color.black);		
		brocha.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 40));
		Graphics2D g2d= (Graphics2D)brocha;
			
			drawImage(g2d, mago, 0,0);
		
			if((actual==StateFactory.getState(3, this))||(actual==StateFactory.getState(4, this))) {
				drawImage(g2d, board, 0,0);
				if(jugador=="Jugador 1") {
					drawImage(g2d, pollo, 745,-60);
				}else if(jugador=="Jugador 2") {
					drawImage(g2d, panda, 742,-35);
				}
	//			brocha.drawString(spacing(),GamePanel.VWIDTH/2 - palabra.length/2, 300);
				if(palabraActivated) {
					printTryWord(brocha);
					brocha.setColor(Color.blue);		
					brocha.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
					brocha.drawString("Escribe la palabra", 380, 340);
				}
				else if(!palabraActivated) {
					printSpaceWord(brocha);
					brocha.setColor(Color.blue);		
					brocha.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
					brocha.drawString("Escribe la letra", GamePanel.VWIDTH/2-85, 340);
				}
	//			brocha.drawString(getWord(), GamePanel.VWIDTH/2-235, 300);
				brocha.setColor(Color.black);		
				brocha.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 40));
				printLetter(brocha); //imprime las letras
				brocha.setColor(Color.ORANGE);
				brocha.setFont(new Font("Impact", Font.PLAIN, 35));
				drawCenteredString(message, GamePanel.VWIDTH,GamePanel.VHEIGHT+100, brocha);
				g2d.draw(rect);
			}
			
		if(isWin) {
			drawImage(g2d, gana, 0,0);
		}
		if(pressed) {
			palabraActivated();
		}
		actual.paint(brocha);
		g2d.setStroke(new BasicStroke(7));
		g2d.drawRect(4, 4, GamePanel.VWIDTH-6, GamePanel.VHEIGHT-6);
	}
	
	
	
	public void printLetter(Graphics brocha) {
		brocha.setColor(Color.black);		
		brocha.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 40));
	
		for(int i=0;i<letras.length;i++) { 	
			if(i/8==0) brocha.drawString(letras[i], GamePanel.VWIDTH/2 -235 + i*60, 520); 
			if(i/8==1) brocha.drawString(letras[i], GamePanel.VWIDTH/2 -235 + i*60 - 8*60, 570); 
			if(i/8==2) brocha.drawString(letras[i], GamePanel.VWIDTH/2 -235 + i*60 - 16*60, 620); 
		}
		brocha.setFont(new Font("Impact", Font.PLAIN, 20));
		brocha.drawString("Letras restantes", 400, 460);
	}
	
	//Cierra gráfico
	
	
	public String getWord() {//Convierte la palabra en un String que se puede recorrer fácilmente
		selectioned = palabra[selection];
		return selectioned;//Se llama en los turnos
	}
	
	public void setCleanLetters(String[] letras) {
		this.letras=letras;
	}
	
	public void startTurn(BoardState actual) {
		this.actual=actual;
	}
	
	public String spacingWord() {
		String spaces ="";
		String palabraBase = getWord();
		for(int i=0; i<palabraBase.length(); i++) {
			spaces = spaces + "_";
		}
		setSpaceWord(spaces);
		return spaceWord;
	}

	public String spacingTryWord() {
		String spaces2="";
		String palabraBase = getWord();
		for(int i=0; i<palabraBase.length(); i++) {
			spaces2 = spaces2 + "_";
		}
		setTryWord(spaces2);
		return tryWord;
		
	}
	
	public void setSpaceWord (String spaceWord) {
		this.spaceWord = spaceWord;
	}
	
	public String getTryWord() {
		return tryWord;
	}
	
	public void setTryWord (String tryWord) {
		this.tryWord = tryWord;
	}
	
	public void clearTry() {
		spacingTryWord();
	}
	
	public String getSpaceWord() {
		return spaceWord;
	}
	
	public void printSpaceWord(Graphics brocha) {
		String[] spaced=spaceWord.split("");
		for(int i=0;i<spaced.length;i++ ) {
			brocha.drawString(spaced[i], 300+i*40,300 );
		}
	}
	
	public void printTryWord(Graphics brocha) {
		String[] spaced2=tryWord.split("");
		for(int i=0;i<spaced2.length;i++ ) {
			brocha.drawString(spaced2[i], 300+i*40,300 );
		}
	}
	
	public boolean clickedRectangle() {
		Rectangle rec = this.getHitbox();
		return rec.contains(x,y);
	}
	
	public Rectangle getHitbox(){
		return rect;
	}
	
	public void palabraActivated() {
		if(clickedRectangle()==true) {
//			System.out.println("Listo para la palabra");
			palabraActivated=true;
		}
	}
	
	public void setPalabraActivated(boolean bool) {
		this.palabraActivated=bool;
	}
	
	public boolean getPalabraActivated() {
		return palabraActivated;
	}
	
	public void keyPressed(int k) {
		actual.keyPressed(k);
	}

	public void setX(int x) {
		System.out.println("Se ha seteado x a: "+x);
		this.x = x;
	}
	
	public void setY(int y) {
		System.out.println("Se ha seteado y a: "+y);
		this.y = y;
	}
	
	public void setPressed(boolean bool) {
		this.pressed=bool;
	}
	
	
	// ¿GANA O NO?
	public void tick() {
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	public void clearMessage() {
		this.message="";
	}
	
	 public void drawCenteredString(String s, int w, int h, Graphics g) {
		    FontMetrics fm = g.getFontMetrics();
		    int x = (w - fm.stringWidth(s)) / 2;
		    int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		    g.drawString(s, x, y);
		  }


	public String getJugador() {
		return jugador;
	}


	public void setJugador(String jugador) {
		this.jugador = jugador;
	}


	public boolean isWin() {
		return isWin;
	}


	public void setWin(boolean isWin) {
		this.isWin = isWin;
	}


	
}

