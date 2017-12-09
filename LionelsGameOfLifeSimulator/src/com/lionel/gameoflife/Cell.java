package lionel.java2d.gameoflife;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Cell {

	public final static boolean ALIVE = true;
	public final static boolean DEAD = false;

	//private int x;
	//private int y;
	private boolean alive;
	private int aliveN = -1;
	
	@JsonIgnore
	private Cell[] neighbours;

	// Constructor
	@JsonCreator
	//public Cell(@JsonProperty("x") int x, @JsonProperty("y") int y, @JsonProperty("state") boolean state) {
	public Cell(@JsonProperty("state") boolean state) {
		//this.x = x;
		//this.y = y;
		this.alive = state;
	}

	public void setNeighbours(Cell[] neighbours) {
		this.neighbours = neighbours;
	}

	// Count how many neighbours are alive
	public void checkNeighbours() {
		int n = 0;
		for (int i = 0; i < neighbours.length; i++)
			if (neighbours[i].isAlive())
				n++;
		aliveN = n;
	}

	// Updates state depending on its state and alive neighbours
	public void updateState() throws Exception {

		if (aliveN == -1)
			throw new Exception("Error: Está intentando updateState() sin haber hecho checkNeighbour()");

		if (this.isAlive()) {
			if (aliveN < 2 || aliveN > 3)
				this.die();
		} else {
			if (aliveN == 3)
				this.live();
		}

		aliveN = -1;
	}

	/*public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}*/

	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void die() {
		alive = DEAD;
	}

	public void live() {
		alive = ALIVE;
	}

	public void switchState() {
		if (isAlive())
			die();
		else
			live();
	}

}
