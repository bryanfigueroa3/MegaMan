package rbadia.voidspace.model;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import rbadia.voidspace.main.GameScreen;

public class Boss extends Rectangle   {
	private static final long serialVersionUID = 1L;
	private  Random rand = new Random();
	public static final int DEFAULT_SPEED = 2;
	
	private int bossWidth = 95;
	private int bossHeight = 100;
	private int speed = DEFAULT_SPEED;
	

	private static final int Y_OFFSET = 30;
	

	
	/**
	 * Crates a new asteroid at a random x location at the top of the screen 
	 * @param screen the game screen
	 */
	public Boss(GameScreen screen){
		this.setLocation((screen.getWidth() - bossWidth),
				(screen.getHeight() - bossHeight - Y_OFFSET) );
		this.setSize(bossWidth, bossHeight);
	}
	
	public int getBossWidth() {
		return bossWidth;
	}
	public int getBossHeight() {
		return bossHeight;
	}

	/**
	 * Returns the current asteroid speed
	 * @return the current asteroid speed
	 */
	public int getSpeed() {
		return speed = DEFAULT_SPEED;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}