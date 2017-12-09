package lionel.java2d.gameoflife;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CellWorldRenderer {

	GameOfLifeWindow gw;
	
	private BufferedImage buffer;
	private Graphics g;

	public CellWorldRenderer(GameOfLifeWindow gw) {
		this.gw = gw;
		
		initialize();
	}
	
	public void initialize() {
		int canvasW = gw.getCanvasW();
		int canvasH = gw.getCanvasH();
		buffer = new BufferedImage(canvasW, canvasH, BufferedImage.TYPE_INT_RGB);
		g = buffer.getGraphics();
	}

	public void renderCellWorld() {
		
		Cell[][] cells = gw.getCellWorld().getCells();
		int worldW = gw.getCellWorld().getWorldW();
		int worldH = gw.getCellWorld().getWorldH();
		
		double cellW = getCellW();
		double cellH = getCellH();
		
		// Erase screen
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, gw.getCanvasW(), gw.getCanvasH());
		g.setColor(Color.WHITE);
		
		// Draw every cell
		for (int i = 0; i < worldW; i++)
			for (int j = 0; j < worldH; j++)
				if (cells[i][j].isAlive()) {
					double x = ((double) i) * cellW;
					double y = ((double) j) * cellH;
					drawRect(g, x, y, cellW, cellH);
				}
	}

	// Equivalent to fillRect but accepts doubles
	private void drawRect(Graphics g, double x, double y, double w, double h) {
		g.fillRect((int) x, (int) y, (int) w, (int) h);
	}

	public double getCellW() {
		return ((double) gw.getCanvasW()) / ((double) gw.getCellWorld().getWorldW());
	}

	public double getCellH() {
		return ((double) gw.getCanvasH()) / ((double) gw.getCellWorld().getWorldH());
	}

	public BufferedImage getBuffer() {
		return buffer;
	}
	
}
