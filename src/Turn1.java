import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Arrays;



public class Turn1 implements BoardState {
	/*Esta clase se encarga de controlar lo que pasa en el juego, sin embargo no imprime nada.
	 * Controla cuando un turno gana o pierde
	 */
	
	private Board bigBoard;
	
	
	//----
	private String[] usedLetters;
	private char[] tryWord;
	private char[] word;
	private char[] spaceWord;
	private int i=0;
	boolean correct;
	
	public Turn1(Board tab) {
		this.bigBoard=tab;
		System.out.println("Turno Jugador 1");
	}
	
	

	@Override
	public void keyPressed(int key) {
		// TODO Auto-generated method stub
		char letra = 0;
		switch(key) {
			case (KeyEvent.VK_A): letra='a';break;
			case (KeyEvent.VK_B): letra='b';break;
			case (KeyEvent.VK_C): letra='c';break;
			case (KeyEvent.VK_D): letra='d';break;
			case (KeyEvent.VK_E): letra='e';break;
			case (KeyEvent.VK_F): letra='f';break;
			case (KeyEvent.VK_G): letra='g';break;
			case (KeyEvent.VK_H): letra='h';break;
			case (KeyEvent.VK_I): letra='i';break;
			case (KeyEvent.VK_J): letra='j';break;
			case (KeyEvent.VK_K): letra='k';break;
			case (KeyEvent.VK_L): letra='l';break;
			case (KeyEvent.VK_M): letra='m';break;
			case (KeyEvent.VK_N): letra='n';break;
			case (KeyEvent.VK_O): letra='o';break;
			case (KeyEvent.VK_P): letra='p';break;
			case (KeyEvent.VK_Q): letra='q';break;
			case (KeyEvent.VK_R): letra='r';break;
			case (KeyEvent.VK_S): letra='s';break;
			case (KeyEvent.VK_T): letra='t';break;
			case (KeyEvent.VK_U): letra='u';break;
			case (KeyEvent.VK_V): letra='v';break;
			case (KeyEvent.VK_W): letra='w';break;
			case (KeyEvent.VK_X): letra='x';break;
			case (KeyEvent.VK_Y): letra='y';break;
			case (KeyEvent.VK_Z): letra='z';break;
			case (KeyEvent.VK_BACK_SPACE): letra='-';break;
		}
		if(bigBoard.getPalabraActivated()) {
			compareWord(letra);
		}else if(!bigBoard.getPalabraActivated()){
			compare(letra);
			cleanLetters(letra);
		}
	}

	public String[] usedLetter(char letra){
			String used ="" + letra;
			String[] usedLetter=used.split("");
			System.out.println("Las bajas son " + usedLetter[0]);
		return usedLetter;
	}
	
	public void cleanLetters(char letra) {
		String[] temporalUsedLetter = usedLetter(letra);
		String[]	 temporalLetras= bigBoard.letras;
		for(int i = 0;i<temporalLetras.length;i++) {
//			System.out.println(temporalLetras[i]+" es igual a "+temporalUsedLetter[0]);
			if(temporalLetras[i].equals(temporalUsedLetter[0])) {//MUY IMPORTANTE, para comparar strings usamos el equals to
//				System.out.println(temporalLetras[i]+" Así quedo");
				temporalLetras[i]=" ";
				bigBoard.setCleanLetters(temporalLetras);
			}
		}
	}
	


	@Override
	public void paint(Graphics brocha) {
		brocha.setColor(Color.black);		
		brocha.setFont(new Font("Stencil", Font.PLAIN, 40));
		brocha.drawString("Jugador 1",400,40);
	}

	
	public void compare(char k) {
		word=bigBoard.getWord().toCharArray();
		spaceWord=bigBoard.getSpaceWord().toCharArray();
			for(int i=0; i<spaceWord.length;i++) {
				if(word[i]==k) {
					correct=true;
					spaceWord[i]=k;
					String temporalSpace=new String(spaceWord);
					bigBoard.setSpaceWord(temporalSpace);
					System.out.println(temporalSpace);
					//OJOCHECALO
				}else{
					correct=false;
				}
			}
			if(correct) win();
			else {
				System.out.println("Error de letra");
				changeTurn(correct);
			}	
	}
	
	
	public void compareWord(char k) {
		word=bigBoard.getWord().toCharArray();
		tryWord=bigBoard.getTryWord().toCharArray();
		System.out.println("Todo bien hasta aquí");
		if(!Arrays.equals(word, tryWord)) {
			if((k=='-')&&(i>0)){
				tryWord[i-1]='_';	
				String lastTemporalSpace=new String(tryWord);
				bigBoard.setTryWord(lastTemporalSpace);
				i--;
			}
			else {
				if(i<word.length){//¿Qué está pasando aquí?
					System.out.print(i+ " ");
					tryWord[i]=k;
					String lastTemporalSpace=new String(tryWord);
					bigBoard.setTryWord(lastTemporalSpace);
					System.out.println(lastTemporalSpace);
				}else{
					System.out.println("Hay error");
					bigBoard.setPalabraActivated(false);
					bigBoard.setPressed(false);
					if(!bigBoard.getPalabraActivated()) System.out.println("Está desactivada");
					changeTurn(true);
				}i++;
			}
		}
		else if(Arrays.equals(word, tryWord)) {
			bigBoard.setState(StateFactory.getState(5, bigBoard));
		}
	}

	
	
	public void win() {
		boolean winner=true;
		word=bigBoard.getWord().toCharArray();
		spaceWord=bigBoard.getSpaceWord().toCharArray();
		for(int i=0;i<word.length;i++) {
			if(word[i]!=spaceWord[i]) {
				winner= false;
			}
		}if(winner) { 
			System.out.println("Felicidades Ganador");
			bigBoard.setState(StateFactory.getState(5, bigBoard));
		}
	}
	
	

	@Override
	public void paintComponent(Graphics brocha) {
		
	}

	


	@Override
	public void changeTurn(boolean correct) {
		if (!correct) {
			bigBoard.setState(StateFactory.getState(6, bigBoard));
			bigBoard.setState(StateFactory.getState(4, bigBoard));
		}
	}



	@Override
	public void tick() {
		
		
	}
	
	
	
}
