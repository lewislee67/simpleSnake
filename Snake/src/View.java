import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class View extends JPanel {

	private LinkedList<Integer[]> snake;
	private Integer[] apple;
	private Timer speedTimer;
	private Direction direction;
	private Random random;
	private boolean turned;
	private int currentGameSpeed;
	
	public View() {
		random = new Random();
		this.setSize(new Dimension(BOARD_WIDTH*12, BOARD_HEIGHT*12));
		this.addKeyListener(new keyboardListener());
		setFocusable(true);
		currentGameSpeed = GAME_SPEED;
		speedTimer = new Timer(currentGameSpeed, new TimerListener());
		initialise();
	}
	
	public void initialise() {
		snake = new LinkedList<Integer[]>(); 
		apple = new Integer[2];
		speedTimer.setDelay(currentGameSpeed);
		direction = Direction.RIGHT;
		for (int i = 0; i < 3; i++) {
			Integer[] coord = new Integer[2];
			coord[0] = i;
			coord[1] = BOARD_HEIGHT/2;
			snake.addFirst(coord);
		}
		newApple();
		speedTimer.start();	
	}
	
	public void paint(Graphics g) {
		
		super.paint(g);
		
		for (Integer[] i : snake) {
			g.drawOval(i[0]*10, i[1]*10, 10, 10);
		}
		g.drawRect(apple[0]*10, apple[1]*10, 10, 10);
		g.drawRect(0, 0, BOARD_WIDTH*10, BOARD_HEIGHT*10);
	}
	
	public void move() {
		Integer[] newHead = snake.getFirst().clone();
		if (direction == Direction.UP) {
			newHead[1]--;
		}
		if (direction == Direction.DOWN) {
			newHead[1]++;
		}
		if (direction == Direction.LEFT) {
			newHead[0]--;
		}
		if (direction == Direction.RIGHT) {
			newHead[0]++;
		}
		if (newHead[0] < 0 || newHead[0] >= BOARD_WIDTH
			|| newHead[1] < 0 || newHead[1] >= BOARD_HEIGHT
			|| ateItself(newHead)) {
			gameOver();
			return;
		}
		
		snake.addFirst(newHead);
		if (newHead[0].equals(apple[0]) && newHead[1].equals(apple[1])) {
			newApple();
			/*if (currentGameSpeed > 50) {
				currentGameSpeed -= 5;
			}
			speedTimer.setDelay(currentGameSpeed);*/
		} else {
			snake.removeLast();
		}
		repaint();
		turned = false;
	}
	
	public void newApple() {

		apple[0] = random.nextInt(BOARD_WIDTH);
		apple[1] = random.nextInt(BOARD_HEIGHT);
		if (ateItself(apple)) {
			newApple();
		}
	}
	
	private boolean ateItself(Integer[] head) {
		for (Integer[] body : snake) {
			if (body[0].equals(head[0]) && body[1].equals(head[1])) {
				return true;
			}
		}
		return false;
	}
	
	public void gameOver() {
		speedTimer.stop();
		initialise();
	}
	
	private class keyboardListener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP && direction != Direction.DOWN && !turned) {
				direction = Direction.UP;
				turned = true;
			} if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != Direction.UP && !turned) {
				direction = Direction.DOWN;
				turned = true;
			} if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != Direction.RIGHT && !turned) {
				direction = Direction.LEFT;
				turned = true;
			} if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != Direction.LEFT && !turned) {
				direction = Direction.RIGHT;
				turned = true;
			} if (e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
				currentGameSpeed = GAME_SPEED - (e.getKeyCode() - '0')*25;
				speedTimer.setDelay(currentGameSpeed);
			}
		}
		
	}
	private class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			move();
		}
	}

	private enum Direction { LEFT, RIGHT, UP, DOWN }
	
	public static final int BOARD_HEIGHT = 30;
	public static final int BOARD_WIDTH = 30;
	public static final int GAME_SPEED = 250;
}
