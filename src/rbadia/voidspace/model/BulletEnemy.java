package rbadia.voidspace.model;
import java.awt.Rectangle;

/**
 * Represents a bullet fired by a ship.
 */
public class BulletEnemy extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	private int bulletEnemyWidth = 8;
	private int bulletEnemyHeight = 8;
	private int speed = 5;

	
	/**
	 * Creates a new bullet above the ship, centered on it
	 * @param ship
	 */	
	public BulletEnemy(EnemyShip enemy){
		this.setLocation(enemy.x + enemy.width - bulletEnemyWidth/2,
				enemy.y + enemy.width/2 - bulletEnemyHeight +2);
		this.setSize(bulletEnemyWidth, bulletEnemyHeight);
	}
	

	/**
	 * Return the bullet's speed.
	 * @return the bullet's speed.
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Set the bullet's speed
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
