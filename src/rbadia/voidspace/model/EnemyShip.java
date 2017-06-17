package rbadia.voidspace.model;

import java.awt.Rectangle;

import rbadia.voidspace.main.GameScreen;

/**
 * Represents a ship/space craft.
 *
 */
public class EnemyShip extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_SPEED3 = 2;
	private static final int Y_OFFSET = 325; // initial y distance of the ship from the bottom of the screen 
	
	private int enemyWidth = 75;
	private int enemyHeight = 83;
	private int bossWidth2 = 110;
	private int bossHeight2 = 100;
	private int speed = DEFAULT_SPEED3;
	
	/**
	 * Creates a new ship at the default initial location. 
	 * @param screen the game screen
	 */
	public EnemyShip(GameScreen screen){
		this.setLocation((screen.getWidth() - enemyWidth)/2,
				screen.getHeight() - enemyHeight - Y_OFFSET);
		this.setSize(enemyWidth, enemyHeight);
	}
	
	/**
	 * Get the default ship width
	 * @return the default ship width
	 */
	public int getEnemyWidth() {
		return enemyWidth;
	}
	
	/**
	 * Get the default ship height
	 * @return the default ship height
	 */
	public int getBossHeight2() {
		return bossHeight2;
	}
	
	/**
	 * Get the default ship width
	 * @return the default ship width
	 */
	public int getBossWidth2() {
		return bossWidth2;
	}
	
	/**
	 * Get the default ship height
	 * @return the default ship height
	 */
	public int getEnemyHeight() {
		return enemyHeight;
	}
	
	/**
	 * Returns the current ship speed
	 * @return the current ship speed
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * Set the current ship speed
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	/**
	 * Returns the default ship speed.
	 * @return the default ship speed
	 */
	public int getDefaultSpeed(){
		return DEFAULT_SPEED3;
	}
	public boolean touchUpperScreen(EnemyShip enemy){
		if(enemy.getY() <= this.getHeight()-20){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean touchBottomScreen(EnemyShip enemy){
		if(enemy.getY() >= this.getHeight()-100){
			return true;
		}
		else{
			return false;
		}
	}
}
