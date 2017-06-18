package rbadia.voidspace.model;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import rbadia.voidspace.main.GameScreen;

public class Asteroid extends Rectangle implements List<Asteroid>  {
	private static final long serialVersionUID = 1L;
	private  Random rand = new Random();
	public static final int DEFAULT_SPEED = 4;
	
	private int asteroidWidth = 32;
	private int asteroidHeight = 32;
	private int speed = DEFAULT_SPEED;
	

	
	/**
	 * Crates a new asteroid at a random x location at the top of the screen 
	 * @param screen the game screen
	 */
	public Asteroid(GameScreen screen){
		this.setLocation(
				screen.getWidth() - asteroidWidth,
        		rand.nextInt(screen.getHeight() - asteroidHeight - 32)
        		);
		this.setSize(asteroidWidth, asteroidHeight);
	}
	
	public int getAsteroidWidth() {
		return asteroidWidth;
	}
	public int getAsteroidHeight() {
		return asteroidHeight;
	}

	/**
	 * Returns the current asteroid speed
	 * @return the current asteroid speed
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * Set the current asteroid speed
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	/**
	 * Returns the default asteroid speed.
	 * @return the default asteroid speed
	 */
	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Asteroid> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(Asteroid e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Asteroid> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Asteroid> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Asteroid get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Asteroid set(int index, Asteroid element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(int index, Asteroid element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Asteroid remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<Asteroid> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<Asteroid> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Asteroid> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
