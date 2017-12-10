package com.lionel.gameoflife;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Main class for the game
 */
public class GameOfLifeWindow extends JFrame {

	// Desired & Real frames per second
	private long dFps = 20;
	private long rFps = 0;

	// Game running?
	private boolean isRunning = true;

	// Cell World & Graphics Renderer
	CellWorld cw;
	CellWorldRenderer cwr;
	GameInfoRenderer gir;

	// Canvas size for cell world (pixels), doesn't include info
	private int canvasW = 840;
	private int canvasH = 600;

	// Initial cell world size (amount of cells)
	private int initialWW = canvasW / 5;
	private int initialWH = canvasH / 5;

	// Window insets (title bar, etc.)
	private Insets insets;

	// Manage user inputs: Keys and Mouse
	KeyboardHandler input;
	MouseHandler mouse;

	// File chooser
	JFileChooser fc = null;

	public static void main(String[] args) {
		GameOfLifeWindow game = new GameOfLifeWindow();
		game.run();
		System.exit(0);
	}

	/**
	 * This method starts the game and runs it in a loop
	 */
	public void run() {

		initialize();

		while (isRunning) {

			long startTime = System.currentTimeMillis();

			update();
			draw();

			long sleepTime = (1000 / dFps) - (System.currentTimeMillis() - startTime);
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (Exception e) {
				}
				;
			}

			long frameTime = System.currentTimeMillis() - startTime;
			if (frameTime > 0)
				rFps = 1000 / frameTime;
		}

		// Make windows disappear
		setVisible(false);
	}

	/**
	 * Set up everything needed for the game to run
	 */
	void initialize() {
		setTitle("Lionel's Game of Life Simulator");
		setSize(canvasW, canvasH);

		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		try {
			cw = new CellWorld(initialWW, initialWH);
			cw.setMaxSize(canvasW, canvasH);
			cw.randomize(0.4);
			cwr = new CellWorldRenderer(this);
			gir = new GameInfoRenderer(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		insets = getInsets();
		setSize(insets.left + canvasW + insets.right,
				insets.top + gir.getTopSize() + canvasH + gir.getBotSize() + insets.bottom);

		input = new KeyboardHandler(this);
		mouse = new MouseHandler(this);

		// Add window resized listener
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				windowResized();
			}
		});
	}

	// Window Resized Handler
	void windowResized() {
		int newW = getWidth();
		int newH = getHeight();
		canvasW = newW - insets.left - insets.right;
		canvasH = newH - insets.top - insets.bottom - gir.getTopSize() - gir.getBotSize();
		cw.setMaxSize(canvasW, canvasH);
		cwr.initialize();
		gir.initialize();
	}

	/**
	 * This method will draw everything
	 */
	void draw() {
		Graphics g = getGraphics();

		// Render Game settings and Cell World to a buffer
		gir.renderGameSettings();
		cwr.renderCellWorld();

		// Draw buffers to main window
		g.drawImage(gir.getTopBuffer(), insets.left, insets.top, this);
		g.drawImage(cwr.getBuffer(), insets.left, insets.top + gir.getTopSize(), this);
		g.drawImage(gir.getBotBuffer(), insets.left, insets.top + gir.getTopSize() + canvasH, this);

	}

	/**
	 * This method will check for input, move things around and check for win
	 * conditions, etc
	 */
	void update() {

		// Play
		if (input.isKeyDown(KeyEvent.VK_ENTER)) {
			cw.play();
		}

		// Pause & 1 step
		if (input.isKeyDown(KeyEvent.VK_SPACE)) {
			if (cw.isPlaying())
				cw.pause();
			else
				cw.force1step();
		}

		// Show / hide game information
		if (input.isKeyDown(KeyEvent.VK_I)) {
			gir.setVisible(!gir.isVisible());
		}

		// Increase / Decrease FPS
		if (input.isKeyDown(KeyEvent.VK_X)) {
			if (dFps < Long.MAX_VALUE)
				dFps += 1;
		}
		if (input.isKeyDown(KeyEvent.VK_Z)) {
			dFps -= 1;
			if (dFps < 1)
				dFps = 1;
		}

		// Increase / Decrease cell world size
		if (input.isKeyDown(KeyEvent.VK_RIGHT)) {
			cw.modifyWorldSize(10, 0);
		}
		if (input.isKeyDown(KeyEvent.VK_LEFT)) {
			cw.modifyWorldSize(-10, 0);
		}
		if (input.isKeyDown(KeyEvent.VK_UP)) {
			cw.modifyWorldSize(0, -10);
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN)) {
			cw.modifyWorldSize(0, 10);
		}

		// Randomize Cell World to 0-90% living cells
		if (input.isKeyDown(KeyEvent.VK_0)) {
			cw.randomize(0.0);
		}
		if (input.isKeyDown(KeyEvent.VK_1)) {
			cw.randomize(0.1);
		}
		if (input.isKeyDown(KeyEvent.VK_2)) {
			cw.randomize(0.2);
		}
		if (input.isKeyDown(KeyEvent.VK_3)) {
			cw.randomize(0.3);
		}
		if (input.isKeyDown(KeyEvent.VK_4)) {
			cw.randomize(0.4);
		}
		if (input.isKeyDown(KeyEvent.VK_5)) {
			cw.randomize(0.5);
		}
		if (input.isKeyDown(KeyEvent.VK_6)) {
			cw.randomize(0.6);
		}
		if (input.isKeyDown(KeyEvent.VK_7)) {
			cw.randomize(0.7);
		}
		if (input.isKeyDown(KeyEvent.VK_8)) {
			cw.randomize(0.8);
		}
		if (input.isKeyDown(KeyEvent.VK_9)) {
			cw.randomize(0.9);
		}

		// Save cell world to file
		if (input.isKeyDown(KeyEvent.VK_S)) {
			fc = new JFileChooser();
			fc.setDialogTitle("Save Cell World");
			fc.setMultiSelectionEnabled(false);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setFileFilter(new FileNameExtensionFilter("JSON Gzip Files", "gz"));

			if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				cw.saveWorldToFile(fc.getSelectedFile().getAbsolutePath());
			}
			fc = null;
		}
		// Load cell world from file
		if (input.isKeyDown(KeyEvent.VK_L)) {
			fc = new JFileChooser();
			fc.setDialogTitle("Load Cell World");
			fc.setMultiSelectionEnabled(false);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setFileFilter(new FileNameExtensionFilter("JSON Gzip Files", "gz"));

			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				cw.loadWorldFromFile(fc.getSelectedFile().getAbsolutePath());
			}
			fc = null;
		}

		// Mouse clicked
		if (mouse.isClicked()) {
			int x = (int) ((double) (mouse.getX() - insets.left) / cwr.getCellW());
			int y = (int) ((double) (mouse.getY() - insets.top - gir.getTopSize()) / cwr.getCellH());
			cw.switchCellState(x, y);
			mouse.releaseClick();
		}
		try {
			cw.step();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getCanvasW() {
		return canvasW;
	}

	public int getCanvasH() {
		return canvasH;
	}

	public long getDesiredFps() {
		return dFps;
	}

	public long getRealFps() {
		return rFps;
	}

	public CellWorld getCellWorld() {
		return cw;
	}

	public CellWorldRenderer getCellWorldRenderer() {
		return cwr;
	}

}