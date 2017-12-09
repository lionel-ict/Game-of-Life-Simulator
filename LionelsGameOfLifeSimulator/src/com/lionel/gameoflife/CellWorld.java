package lionel.java2d.gameoflife;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CellWorld { // Where the cells live!!

	// Generation
	private int generation = 0;

	// World Playing or paused?
	private boolean playing = false;

	// Force 1 game step, even if game paused
	private boolean force1step = false;

	// World size (amount of cells)
	private int worldW;
	private int worldH;
	private final int minW = 3;
	private final int minH = 3;
	private int maxW = Integer.MAX_VALUE;
	private int maxH = Integer.MAX_VALUE;

	// Cells
	private Cell[][] cells;
	private int nCellsAlive;

	// Default constructor
	public CellWorld() {

	}

	// Constructor
	public CellWorld(int ww, int wh) {

		// Check & fix width/height
		if (ww < minW)
			ww = minW;
		else if (ww > maxW)
			ww = maxW;
		if (wh < minH)
			wh = minH;
		else if (wh > maxH)
			wh = maxH;

		// Cell World size (amount of cells)
		this.worldW = ww;
		this.worldH = wh;

		// Initialize world
		initializeCells();
	}

	public boolean isPlaying() {
		return playing;
	}

	public void play() {
		playing = true;
	}

	public void pause() {
		playing = false;
	}

	public void force1step() {
		force1step = true;
	}

	// One step in the game of life
	public void step() throws Exception {

		// If pause, do nothing
		if (!playing && !force1step)
			return;

		// Check if only 1 step
		if (force1step)
			force1step = false;

		// All cells check neighbours and update their state
		allCheckNeighbours();
		allUpdateState();

		// Calculate how many are alive
		updateNumCellsAlive();

		// One more generation
		generation++;
	}

	// Tell all cells to check their neighbours
	private void allCheckNeighbours() {
		for (int i = 0; i < worldW; i++)
			for (int j = 0; j < worldH; j++)
				cells[i][j].checkNeighbours();
	}

	// Tell all cells to update their state
	private void allUpdateState() throws Exception {
		for (int i = 0; i < worldW; i++)
			for (int j = 0; j < worldH; j++)
				cells[i][j].updateState();
	}

	// Update number of cells alive
	private void updateNumCellsAlive() {
		nCellsAlive = 0;
		for (int i = 0; i < worldW; i++)
			for (int j = 0; j < worldH; j++)
				if (cells[i][j].isAlive())
					nCellsAlive++;
	}

	// Switch state of cell X,Y
	public void switchCellState(int x, int y) {
		cells[x][y].switchState();
		if (cells[x][y].isAlive())
			nCellsAlive++;
		else
			nCellsAlive--;
	}

	// Creates all cells in the world. Initially dead.
	private void initializeCells() {

		nCellsAlive = 0;
		cells = new Cell[worldW][worldH];

		for (int i = 0; i < worldW; i++)
			for (int j = 0; j < worldH; j++) {
				cells[i][j] = new Cell(Cell.DEAD);// new Cell(i, j, Cell.DEAD);
			}

		updateAllNeighbours();
	}

	// Set a new world size & update cell world
	public void newWorldSize(int w, int h) {
		int wch = w - this.worldW;
		int hch = h - this.worldH;
		modifyWorldSize(wch, hch);
	}

	// Create new cell matrix and copy existing cells
	public void modifyWorldSize(int w, int h) {
		// New size
		int newW = worldW + w;
		int newH = worldH + h;

		// Check & fix width/height
		if (newW < minW)
			newW = minW;
		else if (newW > maxW)
			newW = maxW;
		if (newH < minH)
			newH = minH;
		else if (newH > maxH)
			newH = maxH;

		// New cell matrix
		Cell[][] newCells = new Cell[newW][newH];

		// For every cell in the new matrix
		for (int i = 0; i < newW; i++)
			for (int j = 0; j < newH; j++) {
				// If (x,y) exists in current matrix, copy to new matrix
				if ((i < worldW) && (j < worldH)) {
					newCells[i][j] = cells[i][j];
				}
				// Otherwise, create new dead Cell
				else {
					newCells[i][j] = new Cell(Cell.DEAD);// new Cell(i, j, Cell.DEAD);
				}
			}

		// Fix new cells and world size
		cells = newCells;
		worldW = newW;
		worldH = newH;

		// Update cells alive and neighbours
		updateNumCellsAlive();
		updateAllNeighbours();
	}

	// Randomize every cell state with 50% chance of being alive
	public void randomize() {
		randomize(0.5);
	}

	// Randomize every cell state with 'percent' chance of being alive
	public void randomize(double percent) {
		for (int i = 0; i < worldW; i++)
			for (int j = 0; j < worldH; j++)
				if (Math.random() < percent) {
					cells[i][j].live();
				} else
					cells[i][j].die();
		updateNumCellsAlive();
	}

	// All cells die
	public void allDie() {
		nCellsAlive = 0;
		for (int i = 0; i < worldW; i++)
			for (int j = 0; j < worldH; j++)
				cells[i][j].die();
	}

	// All cells alive
	public void allLive() {
		nCellsAlive = worldW * worldH;
		for (int i = 0; i < worldW; i++)
			for (int j = 0; j < worldH; j++)
				cells[i][j].live();
	}

	// Tells every cell who are their 8 neighbours
	private void updateAllNeighbours() {
		for (int i = 0; i < worldW; i++)
			for (int j = 0; j < worldH; j++) {
				setNeighbour(i, j);
			}
	}

	// Sets the neighbours of cell [x,y]
	private void setNeighbour(int x, int y) {

		Cell[] nc = new Cell[8];

		int count = 0;
		for (int i = x - 1; i <= x + 1; i++)
			for (int j = y - 1; j <= y + 1; j++) {
				if ((i != x) || (j != y)) {
					nc[count] = cells[normalizeX(i)][normalizeY(j)];
					count++;
				}
			}

		cells[x][y].setNeighbours(nc);
	}

	// Normalize X values if out-of-bounds (between 0 and worldWidth-1)
	private int normalizeX(int x) {
		if (x == -1)
			x = worldW - 1;
		else if (x == worldW)
			x = 0;

		return x;
	}

	// Normalize Y values if out-of-bounds (between 0 and worldHeight-1)
	private int normalizeY(int y) {
		if (y == -1)
			y = worldH - 1;
		else if (y == worldH)
			y = 0;

		return y;
	}

	public void setMaxSize(int maxW, int maxH) {
		this.maxW = maxW;
		this.maxH = maxH;

		if (worldW > maxW && worldH > maxH)
			newWorldSize(maxW, maxH);
		else if (worldW > maxW)
			newWorldSize(maxW, worldH);
		else if (worldH > maxH)
			newWorldSize(worldW, maxH);
	}

	public Cell[][] getCells() {
		return cells;
	}

	public int getNumCellsAlive() {
		return nCellsAlive;
	}

	public int getnCellsAlive() {
		return nCellsAlive;
	}

	public int getWorldW() {
		return worldW;
	}

	public int getWorldH() {
		return worldH;
	}

	public int getGeneration() {
		return generation;
	}

	public void saveWorldToFile(String filepath) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String str = mapper.writeValueAsString(cells);
			MyGzipCompressor.StringToFile(str, filepath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadWorldFromFile(String filepath) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String str = MyGzipCompressor.FileToString(filepath);
			cells = mapper.readValue(str, Cell[][].class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		worldW = cells.length;
		worldH = cells[0].length;
		updateAllNeighbours();
		updateNumCellsAlive();
		generation = 0;

	}
}
