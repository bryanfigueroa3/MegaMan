package rbadia.voidspace.model;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import rbadia.voidspace.main.GameScreen;

public class Boss extends Rectangle   {
	private static final long serialVersionUID = 1L;
	private  Random rand = new Random();
	public static final int DEFAULT_SPEED = 4;
	
	private int bossWidth = 100;
	private int bossHeight = 132;
	private int speed = DEFAULT_SPEED;
	

	
	/**
	 * Crates a new asteroid at a random x location at the top of the screen 
	 * @param screen the game screen
	 */
	public Boss(GameScreen screen){
		this.setLocation(
				screen.getWidth() - bossWidth,
        		rand.nextInt(screen.getHeight() - bossHeight - 32)
        		);
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
		int randspeed = rand.nextInt(7);
		return speed = randspeed;
	}
}