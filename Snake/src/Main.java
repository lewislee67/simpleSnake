import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JFrame {

	public Main() {
		setSize(new Dimension(View.BOARD_WIDTH*12, View.BOARD_HEIGHT*12));
		add(new View());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	public static void main (String[] args) {
		new Main();
	}
	
}
