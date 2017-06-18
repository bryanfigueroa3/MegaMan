package rbadia.voidspace.main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigAsteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.BulletBoss2;
import rbadia.voidspace.model.BulletEnemy;
import rbadia.voidspace.model.EnemyShip;
import rbadia.voidspace.model.Floor;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

/**
 * Main game screen. Handles all game graphics updates and some of the game logic.
 */
public class GameScreen extends BaseScreen{
	private static final long serialVersionUID = 1L;

	private BufferedImage backBuffer;
	private Graphics2D g2d;

	private static final int NEW_SHIP_DELAY = 500;
	private static final int NEW_ASTEROID_DELAY = 500;
	private static final int NEW_ASTEROID_2_DELAY = 500;
	private static final int FIRE_BULLET_DELAY = 500;
	private static final int FIRE_BULLET_BOSS_DELAY = 700;
	private static final int NEW_BIG_ASTEROID_DELAY = 500;

	//	private long lastShipTime;
	private long lastAsteroidTime;
	private long lastAsteroid2Time;
	//	private long lastBigAsteroidTime;
	private long lastBulletTime;
	private long lastEnemyShip;

	private Rectangle asteroidExplosion;
	private Rectangle enemyExplosion;
	private Rectangle background;
	private Rectangle bigAsteroidExplosion;
	private Rectangle shipExplosion;
	//	private Rectangle bossExplosion;

	private JLabel shipsValueLabel;
	private JLabel destroyedValueLabel;
	private JLabel levelValueLabel;

	private Random rand;

	private Font originalFont;
	private Font bigFont;
	private Font biggestFont;

	private GameStatus status;
	private SoundManager soundMan;
	private GraphicsManager graphicsMan;
	private GameLogic gameLogic;
	//private InputHandler input;
	//private Platform[] platforms;

	private int boom=0;
	private int level=1;
	//private int damage=0;
	//	private int scroll=0;
	//	private int bossHealth=0;
	//	private int delay=0;


	/**
	 * This method initializes 
	 * 
	 */
	public GameScreen() {
		super();
		// initialize random number generator
		rand = new Random();

		initialize();

		// init graphics manager
		graphicsMan = new GraphicsManager();

		// init back buffer image
		backBuffer = new BufferedImage(500, 400, BufferedImage.TYPE_INT_RGB);
		g2d = backBuffer.createGraphics();
	}

	/**
	 * Initialization method (for VE compatibility).
	 */
	protected void initialize() {
		// set panel properties
		this.setSize(new Dimension(500, 400));
		this.setPreferredSize(new Dimension(500, 400));
		this.setBackground(Color.BLACK);
	}

	/**
	 * Update the game screen.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// draw current backbuffer to the actual game screen
		g.drawImage(backBuffer, 0, 0, this);
	}

	/**
	 * Update the game screen's backbuffer image.
	 */
	public void updateScreen(){
		MegaMan megaMan = gameLogic.getMegaMan();
		Floor[] floor = gameLogic.getFloor();
		Platform[] platform = gameLogic.getNumPlatforms();
		List<Bullet> bullets = gameLogic.getBullets();
		List<BulletEnemy> enemyBullet = gameLogic.getEnemyBullet();
		List<BulletBoss2> bulletsBoss2 = gameLogic.getBulletBoss2();
		List<BigBullet> bigBullets = gameLogic.getBigBullets();
		Asteroid asteroid = gameLogic.getAsteroid();
		Asteroid asteroid2 = gameLogic.getAsteroid2();
		BigAsteroid bigAsteroid = gameLogic.getBigAsteroid();
		Boss boss = gameLogic.getBoss();
		EnemyShip enemy = gameLogic.getEnemy();
		//		Boss boss2 = gameLogic.getBoss2();


		// set orignal font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		// erase screen
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, getSize().width, getSize().height);

		// draw 50 random stars
		drawStars(50);

		// if the game is starting, draw "Get Ready" message
		if(status.isGameStarting()){
			drawGetReady();
			return;
		}

		// if the game is over, draw the "Game Over" message
		if(status.isGameOver()){
			// draw the message
			drawGameOver();

			long currentTime = System.currentTimeMillis();
			// draw the explosions until their time passes
			if((currentTime - lastAsteroidTime) < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			if((currentTime - lastAsteroid2Time) < NEW_ASTEROID_2_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			
			return;
		}

		//if the game is won, draw the "You Win!!!" message
		if(status.isGameWon()){
			// draw the message
			drawYouPass();

			long currentTime = System.currentTimeMillis();
			// draw the explosions until their time passes
			if((currentTime - lastAsteroidTime) < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			if((currentTime - lastAsteroid2Time) < NEW_ASTEROID_2_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			return;
		}

		// the game has not started yet
		if(!status.isGameStarted()){
			// draw game title screen
			initialMessage();
			return;
		}

		//draw Floor
		for(int i=0; i<9; i++){
			graphicsMan.drawFloor(floor[i], g2d, this, i);	
		}
		//draw level 1 Platforms
		for(int i=0; i<8; i++){
			graphicsMan.drawPlatform(platform[i], g2d, this, i);
		}
		//draw MegaMan
		if(!status.isNewMegaMan()){
			if((Gravity() == true) || ((Gravity() == true) && (Fire() == true || Fire2() == true))){
				graphicsMan.drawMegaFallR(megaMan, g2d, this);
			}
		}

		if((Fire() == true || Fire2()== true) && (Gravity()==false)){
			graphicsMan.drawMegaFireR(megaMan, g2d, this);
		}

		if((Gravity()==false) && (Fire()==false) && (Fire2()==false)){
			graphicsMan.drawMegaMan(megaMan, g2d, this);
		}
		if((Gravity()==false) && (FireBoss()== true)){
			graphicsMan.drawBossFire(boss, g2d, this);
		}
		
		shootAsteroid(asteroid);
		if(status.getLevel()>1){
			shootAsteroid(asteroid2);
		}
		if(status.getLevel()>2){
			shootBigAsteroid(bigAsteroid);
		}
		releaseEnemy(enemy);
		


		//Draw bullets   
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			graphicsMan.drawBullet(bullet, g2d, this);

			boolean remove = gameLogic.moveBullet(bullet);
			if(remove){
				bullets.remove(i);
				i--;
			}
		}
		//Draw enemy bullets
		long currentTime = System.currentTimeMillis();
		if((currentTime - lastBulletTime) > FIRE_BULLET_DELAY){
				lastBulletTime = currentTime;
				gameLogic.fireEnemyBullet();
		}
		for(int i=0;i<enemyBullet.size();i++){
			BulletEnemy bulletb = enemyBullet.get(i);	
			graphicsMan.drawBulletBoss(bulletb, g2d, this);
			
			boolean remove = gameLogic.moveBulletEnemy(bulletb);
			if(remove){
				enemyBullet.remove(i);
				i--;
			}
		}
				
//		//Draw boss
//		if(!status.isNewBoss() && boom > 8){
//			//LEVEL 3
//			if((Gravity()==false)){
//				graphicsMan.drawBoss(boss, g2d, this);
//				boss.translate(-boss.getSpeed(), 0);
//			}
//			if(boss.getX()<=this.getWidth()/2){
//				boss.translate(boss.getSpeed(),0);
//			}
//		}
//		//Draw boss bullet
//		if((currentTime - lastBulletTime) > FIRE_BULLET_BOSS_DELAY){
//				lastBulletTime = currentTime;
//				gameLogic.fireBossBullet();
//		}
//				for(int i=0;i<bulletsBoss2.size();i++){
//					BulletBoss2 bulletBoss = bulletsBoss2.get(i);	
//				  graphicsMan.drawBulletBoss(bulletBoss, g2d, this);
//					
//					boolean remove = gameLogic.moveBulletBoss2(bulletBoss);
//					if(remove){
//						bulletsBoss2.remove(i);
//						i--;
//			}
//		}
				
		//Draw big bullets
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			graphicsMan.drawBigBullet(bigBullet, g2d, this);

			boolean remove = gameLogic.moveBigBullet(bigBullet);
			if(remove){
				bigBullets.remove(i);
				i--;
			}
		}
		
		checkCollisions(asteroid, asteroid2, bigAsteroid, bullets, bigBullets, enemyBullet, floor, enemy, megaMan);
		
		if(status.isNewAsteroid()){
			//long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
				// draw a new asteroid
				lastAsteroidTime = currentTime;
				status.setNewAsteroid(false);
				asteroid.setLocation(this.getWidth() - asteroid.getAsteroidWidth(),
						rand.nextInt(this.getHeight() - asteroid.getAsteroidHeight() - 32));
			}

			else{
				// draw explosion
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this); 
			}
		}
		
		if(boom == 2){
			restructure();
			status.setLevel(status.getLevel()+1);
		}
		
		
		if(boom == 8){
			restructure();
			status.setLevel(status.getLevel()+1);
		}

		status.getAsteroidsDestroyed();
		status.getShipsLeft();
		status.getLevel();

		// update asteroids destroyed label  
		destroyedValueLabel.setText(Long.toString(status.getAsteroidsDestroyed()));

		// update ships left label
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));

		//update level label
		levelValueLabel.setText(Long.toString(status.getLevel()));
		
	}
	
	public void	shootAsteroid(Asteroid asteroid){
		switch(status.getLevel()){
		case 1:
			if(!status.isNewAsteroid() && boom <= 2){
				//LEVEL 1
				if((asteroid.getX() + asteroid.getAsteroidWidth() >  0) && (boom <= 5 || boom == 15)){
					asteroid.translate(-asteroid.getSpeed(), 0);
					graphicsMan.drawAsteroid(asteroid, g2d, this);	
				}
				else if (boom <= 5){
					asteroid.setLocation(this.getWidth() - asteroid.getAsteroidWidth(),
							rand.nextInt(this.getHeight() - asteroid.getAsteroidHeight() - 32));
				}	
			}
			break;
		case 2:
			if(!status.isNewAsteroid() && boom > 2 && boom < 8){
				//LEVEL 2
				if((asteroid.getX() + asteroid.getAsteroidWidth() >  0)){
					asteroid.translate(-asteroid.getSpeed(), asteroid.getSpeed()/2);
					graphicsMan.drawAsteroid(asteroid, g2d, this);	
				}
				else if (boom <= 7){
					asteroid.setLocation(this.getWidth() - asteroid.getAsteroidWidth(),
							rand.nextInt(this.getHeight() - asteroid.getAsteroidHeight() - 32));
				}
			}
			break;
		case 3:
			if(!status.isNewAsteroid() && boom > 8){
				//LEVEL 3
				if((asteroid.getX() + asteroid.getAsteroidWidth() >  0)){
					asteroid.translate(-asteroid.getSpeed(), asteroid.getSpeed()/2);
					graphicsMan.drawAsteroid(asteroid, g2d, this);	
				}
				else if (boom <= 16){
					asteroid.setLocation(this.getWidth() - asteroid.getAsteroidWidth(),
							rand.nextInt(this.getHeight() - asteroid.getAsteroidHeight() - 32));
				}
			}
			break;
		}
	}

	public void shootBigAsteroid(BigAsteroid bigAsteroid){
		if(!status.isNewBigAsteroid() && boom > 8){
			//LEVEL 3
			if((bigAsteroid.getX() + bigAsteroid.getBigAsteroidWidth() >  0)){
				bigAsteroid.translate(-bigAsteroid.getSpeed2(), bigAsteroid.getSpeed2()/2);
				graphicsMan.drawBigAsteroid(bigAsteroid, g2d, this);	
			}
			else if (boom <= 16){
				bigAsteroid.setLocation(this.getWidth() - bigAsteroid.getBigAsteroidWidth(),
						rand.nextInt(this.getHeight() - bigAsteroid.getBigAsteroidHeight() - 32));
			}
		}
	}
	
	public void releaseEnemy(EnemyShip enemy){
		//Draw enemy ship
		 if(!status.isNewEnemy() && boom > 2){
			if(enemy.getY()-enemy.getEnemyHeight() + 65 >0)
			{	
				enemy.translate(0,-enemy.getSpeed());
				graphicsMan.drawEnemyShip(enemy, g2d, this);
			}
			else if(enemy.getY()-enemy.getEnemyHeight()+65<0){
				enemy.translate(0,enemy.getSpeed());
			}
			else if (boom > 8){
				enemy.setLocation(this.getWidth()-enemy.getEnemyWidth(),this.getHeight() - enemy.getEnemyHeight()+25);
			}
		}
//		 if(!status.isNewBoss() && boom > 8){
//			if(boss.getY() <=0 || (boss.getY() + boss.getBossHeight() + 50) >= this.getHeight()){
//				boss.setSpeed(-boss.getSpeed());
//				boss.setLocation((int) boss.getX(), (int)boss.getY() + boss.getSpeed());
//				graphicsMan.drawEnemyShip(boss, g2d, this);
//			}
//		 if(status.isNewEnemy()){
//			System.out.println("hey");
//			// draw explosion
//			graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this); 
//		}
//		 }
	}
	
	public void checkCollisions(Asteroid asteroid, Asteroid asteroid2, BigAsteroid bigAsteroid, List<Bullet> bullets, 
			List<BigBullet> bigBullets, List<BulletEnemy> enemyBullet, Floor[] floor, EnemyShip enemy, MegaMan megaMan){
		//Bullet Collisions
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			//Bullet-asteroid collision
			if(asteroid.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				boom=boom + 1;
				damage=0;
				bullets.remove(i);
				break;
			}
			
			if(asteroid2.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid2);
				boom=boom + 1;
				damage=0;
				bullets.remove(i);
				break;
			}
			
			//Bullet-bigAsteroid collision
			if(bigAsteroid.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeBigAsteroid(bigAsteroid);
				boom=boom + 1;
				damage=0;
				bullets.remove(i);
				break;
			}
			
			//Bullet-enemy collision
			if(enemy.intersects(bullet)){
				// increase enemy destroyed count
				status.setEnemyDestroyed(status.getEnemyDestroyed() + 100);
				removeEnemy(enemy);
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
		

		//BigBullet Collisions
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			//BigBullet-asteroid collision
			if(asteroid.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				boom=boom + 1;
				damage=0;
				bigBullets.remove(i);
			}
			
			if(asteroid2.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid2);
				boom=boom + 1;
				damage=0;
				bigBullets.remove(i);
			}
			
			if(bigAsteroid.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeBigAsteroid(bigAsteroid);
				boom=boom + 1;
				damage=0;
				bigBullets.remove(i);
				break;
			}
			
			if(enemy.intersects(bigBullet)){
				// increase enemy destroyed count
				status.setEnemyDestroyed(status.getEnemyDestroyed() + 100);
				removeEnemy(enemy);
				boom=boom + 1;
				damage=0;
				bigBullets.remove(i);
				break;
			}
		}
		
		//MegaMan Collisions
		//MegaMan-asteroid Collision
		if(asteroid.intersects(megaMan)){
			status.setShipsLeft(status.getShipsLeft() - 1);
			removeAsteroid(asteroid);
		}
		
		if(asteroid2.intersects(megaMan)){
			status.setShipsLeft(status.getShipsLeft() - 1);
			removeAsteroid(asteroid2);
		}
		
		//MegaMan-bigAsteroid Collision
		if(bigAsteroid.intersects(megaMan)){
			status.setShipsLeft(status.getShipsLeft() - 1);
			removeBigAsteroid(bigAsteroid);
		}
		
		////MegaMan-enemyBullet Collision
		for(int i=0; i<enemyBullet.size(); i++){
			BulletEnemy bullet = enemyBullet.get(i);
			if(bullet.intersects(megaMan)){
				status.setShipsLeft(status.getShipsLeft() - 1);	
					enemyBullet.remove(i);
					i--;
			}
		}
		

		//Asteroid-Floor collision
		for(int i=0; i<9; i++){
			if(asteroid.intersects(floor[i])){
				removeAsteroid(asteroid);

			}
			
			if(asteroid2.intersects(floor[i])){
				removeAsteroid(asteroid2);

			}
			
			if(bigAsteroid.intersects(floor[i])){
				removeBigAsteroid(bigAsteroid);

			}
		}
	}		
		
	/**
	 * Draws the "Game Over" message.
	 */
	protected void drawGameOver() {
		String gameOverStr = "GAME OVER";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameOverStr);
		if(strWidth > this.getWidth() - 10){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(gameOverStr);
		}
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.WHITE);
		g2d.drawString(gameOverStr, strX, strY);

		boomReset();
		healthReset();
		delayReset();
	}

	protected void drawYouPass() {
		String youWinStr = "You Pass";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(youWinStr);
		if(strWidth > this.getWidth() - 10){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(youWinStr);
		}
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(youWinStr, strX, strY);

		g2d.setFont(originalFont);
		fm = g2d.getFontMetrics();
		String newGameStr = "Next level starting soon";
		strWidth = fm.stringWidth(newGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = (this.getHeight() + fm.getAscent())/2 + ascent + 16;
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(newGameStr, strX, strY);

		//Change value in order for the next level to start
		if(boom==2)
		{
			boom=3;
		}
			
		if(boom==8)
		{
			boom=9;
		}
	

//				boomReset();
//				healthReset();
//				delayReset();
	}

	/**
	 * Draws the initial "Get Ready!" message.
	 */
	protected void drawGetReady() {
		String readyStr = "Get Ready"; 
		g2d.setFont(originalFont.deriveFont(originalFont.getSize2D() + 1));
		FontMetrics fm = g2d.getFontMetrics();
		int ascent = fm.getAscent();
		int strWidth = fm.stringWidth(readyStr);
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(readyStr, strX, strY);
	}

	/**
	 * Draws the specified number of stars randomly on the game screen.
	 * @param numberOfStars the number of stars to draw
	 */
	protected void drawStars(int numberOfStars) {
		g2d.setColor(Color.WHITE);
		for(int i=0; i<numberOfStars; i++){
			int x = (int)(Math.random() * this.getWidth());
			int y = (int)(Math.random() * this.getHeight());
			g2d.drawLine(x, y, x, y);
		}
	}

	/**
	 * Display initial game title screen.
	 */
	protected void initialMessage() {
		String gameTitleStr = "Definitely Not MegaMan";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD).deriveFont(Font.ITALIC);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameTitleStr);
		if(strWidth > this.getWidth() - 10){
			bigFont = currentFont;
			biggestFont = currentFont;
			fm = g2d.getFontMetrics(currentFont);
			strWidth = fm.stringWidth(gameTitleStr);
		}
		g2d.setFont(bigFont);
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2 - ascent;
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(gameTitleStr, strX, strY);

		g2d.setFont(originalFont);
		fm = g2d.getFontMetrics();
		String newGameStr = "Press <Space> to Start a New Game.";
		strWidth = fm.stringWidth(newGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = (this.getHeight() + fm.getAscent())/2 + ascent + 16;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(newGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String itemGameStr = "Press <I> for Item Menu.";
		strWidth = fm.stringWidth(itemGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(itemGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String shopGameStr = "Press <S> for Shop Menu.";
		strWidth = fm.stringWidth(shopGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(shopGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String exitGameStr = "Press <Esc> to Exit the Game.";
		strWidth = fm.stringWidth(exitGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(exitGameStr, strX, strY);
	}

	/**
	 * Prepare screen for game over.
	 */
	public void doGameOver(){
		shipsValueLabel.setForeground(new Color(128, 0, 0));
	}

	/**
	 * Prepare screen for a new game.
	 */
	public void doNewGame(){		
		lastAsteroidTime = -NEW_ASTEROID_DELAY;
		//lastBigAsteroidTime = -NEW_BIG_ASTEROID_DELAY;
		lastShipTime = -NEW_SHIP_DELAY;

		bigFont = originalFont;
		biggestFont = null;

		// set labels' text
		shipsValueLabel.setForeground(Color.BLACK);
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));
		destroyedValueLabel.setText(Long.toString(status.getAsteroidsDestroyed()));
		levelValueLabel.setText(Long.toString(status.getLevel()));
	}

	/**
	 * Sets the game graphics manager.
	 * @param graphicsMan the graphics manager
	 */
	public void setGraphicsMan(GraphicsManager graphicsMan) {
		this.graphicsMan = graphicsMan;
	}

	/**
	 * Sets the game logic handler
	 * @param gameLogic the game logic handler
	 */
	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		this.status = gameLogic.getStatus();
		this.soundMan = gameLogic.getSoundMan();
	}

	/**
	 * Sets the label that displays the value for asteroids destroyed.
	 * @param destroyedValueLabel the label to set
	 */
	public void setDestroyedValueLabel(JLabel destroyedValueLabel) {
		this.destroyedValueLabel = destroyedValueLabel;
	}

	/**
	 * Sets the label that displays the value for ship (lives) left
	 * @param shipsValueLabel the label to set
	 */
	public void setShipsValueLabel(JLabel shipsValueLabel) {
		this.shipsValueLabel = shipsValueLabel;
	}

	public void setLevelValueLabel(JLabel levelValueLabel){
		this.levelValueLabel = levelValueLabel;
	}

	public int getBoom(){
		return boom;
	}
	public void setBoom(int boom){
		this.boom=boom;
	}
	public int boomReset(){
		boom= 0;
		return boom;
	}
	public long healthReset(){
		boom= 0;
		return boom;
	}
	public long delayReset(){
		boom= 0;
		return boom;
	}

	protected boolean Gravity(){
		MegaMan megaMan = gameLogic.getMegaMan();
		Floor[] floor = gameLogic.getFloor();

		for(int i=0; i<9; i++){
			if((megaMan.getY() + megaMan.getMegaManHeight() -17 < this.getHeight() - floor[i].getFloorHeight()/2) 
					&& Fall() == true){

				megaMan.translate(0 , 2);
				return true;

			}
		}
		return false;
	}
	//Bullet fire pose
	protected boolean Fire(){
		MegaMan megaMan = gameLogic.getMegaMan();
		List<Bullet> bullets = gameLogic.getBullets();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if((bullet.getX() > megaMan.getX() + megaMan.getMegaManWidth()) && 
					(bullet.getX() <= megaMan.getX() + megaMan.getMegaManWidth() + 60)){
				return true;
			}
		}
		return false;
	}

	//BigBullet fire pose
	protected boolean Fire2(){
		MegaMan megaMan = gameLogic.getMegaMan();
		List<BigBullet> bigBullets = gameLogic.getBigBullets();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if((bigBullet.getX() > megaMan.getX() + megaMan.getMegaManWidth()) && 
					(bigBullet.getX() <= megaMan.getX() + megaMan.getMegaManWidth() + 60)){
				return true;
			}
		}
		return false;
	}
	protected boolean FireBoss(){
		Boss boss = gameLogic.getBoss();
		List<BulletBoss2> bulletBoss = gameLogic.getBulletBoss2();
		for(int i=0; i<bulletBoss.size(); i++){
			BulletBoss2 bulletsBoss = bulletBoss.get(i);
			if((bulletsBoss.getX() > boss.getX() + boss.getBossWidth()) && 
					(bulletsBoss.getX() <= boss.getX() + boss.getBossWidth() + 60)){
				return true;
			}
		}
		return false;
	}

	//Platform Gravity
	public boolean Fall(){
		MegaMan megaMan = gameLogic.getMegaMan(); 
		Platform[] platform = gameLogic.getNumPlatforms();
		for(int i=0; i<8; i++){
			if((((platform[i].getX() < megaMan.getX()) && (megaMan.getX()< platform[i].getX() + platform[i].getPlatformWidth()))
					|| ((platform[i].getX() < megaMan.getX() + megaMan.getMegaManWidth()) 
							&& (megaMan.getX() + megaMan.getMegaManWidth()< platform[i].getX() + platform[i].getPlatformWidth())))
					&& megaMan.getY() + megaMan.getMegaManHeight() == platform[i].getY()
					){
				return false;
			}
		}
		return true;
	}

	public void restructure(){
		Platform[] platform = gameLogic.getNumPlatforms();
		if(boom==2){
			for(int i=0; i<8; i++){
				if(i<4)	platform[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);
				if(i==4) platform[i].setLocation(50 +i*50, getHeight()/2 + 140 - 3*40);
				if(i>4){	
					int n=4;
					platform[i].setLocation(50 + i*50, getHeight()/2 + 20 + (i-n)*40 );
					n=n+2;
				}
			}
		}
		
		else if(boom==8){
			for(int i=0; i<8; i++){
				if(i<4)	platform[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);
				if(i==4) platform[i].setLocation(50 +i*50, getHeight()/2 + 140 - i*40);
				if(i>4){	
					int n=4;
					platform[i].setLocation( i*50, getHeight()/2 - 170 + (i-n)*40 );
					n=n-2;
				}
			}
		}
		
	}
//
	public void removeAsteroid(Asteroid asteroid){
		// "remove" asteroid
		asteroidExplosion = new Rectangle(
				asteroid.x,
				asteroid.y,
				asteroid.width,
				asteroid.height);
		asteroid.setLocation(-asteroid.width, -asteroid.height);
		status.setNewAsteroid(true);
		lastAsteroidTime = System.currentTimeMillis();

		// play asteroid explosion sound
		soundMan.playAsteroidExplosionSound();
	}
	
	public void removeAsteroid2(Asteroid asteroid2){
		// "remove" asteroid
		asteroidExplosion = new Rectangle(
				asteroid2.x,
				asteroid2.y,
				asteroid2.width,
				asteroid2.height);
		asteroid2.setLocation(-asteroid2.width, -asteroid2.height);
		status.setNewAsteroid2(true);
		lastAsteroid2Time = System.currentTimeMillis();

		// play asteroid explosion sound
		soundMan.playAsteroidExplosionSound();
	}
	
	public void removeBigAsteroid(BigAsteroid bigAsteroid){
		// "remove" asteroid
		asteroidExplosion = new Rectangle(
				bigAsteroid.x,
				bigAsteroid.y,
				bigAsteroid.width,
				bigAsteroid.height);
		bigAsteroid.setLocation(-bigAsteroid.width, -bigAsteroid.height);
		status.setNewAsteroid(true);
		lastAsteroidTime = System.currentTimeMillis();

		// play asteroid explosion sound
		soundMan.playAsteroidExplosionSound();
	}
	public void removeEnemy (EnemyShip enemy){
		enemyExplosion = new Rectangle(
				enemy.x,
				enemy.y,
				enemy.width,
				enemy.height);
		enemy.setLocation(-enemy.width, -enemy.height);
		status.setNewEnemy(true);
		lastEnemyShip = System.currentTimeMillis();
		// play asteroid explosion sound
		soundMan.playAsteroidExplosionSound();
	}
}
