

import java.awt.BorderLayout;

import javax.swing.JFrame;



public class Main {
	public static void main(String[] args) {
		JFrame ventana = new JFrame("PescuezoAmarrado");
		ventana.setContentPane(new GamePanel());
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setVisible(true);
		ventana.setLayout(new BorderLayout());
		ventana.setResizable(false);	//No queremos que vean lo que pasa detras de camaras
		ventana.pack();	//Redimensiona al tamanio del panel. Obtiene las dimensiones de cada elemento dentro del frame
		ventana.setLocationRelativeTo(null);
		System.setProperty("sun.java2d.translaccel", "true");
	}
}
