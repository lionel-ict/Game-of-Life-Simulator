package com.lionel.gameoflife;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class GameInfoRenderer {

	GameOfLifeWindow gw;
	Font f = new Font("Dialog", Font.PLAIN, 18);
	boolean visible = true;
	
	BufferedImage bufferTop;
	BufferedImage bufferBot;
	Graphics gTop;
	Graphics gBot;
	
	// Constructor
	public GameInfoRenderer(GameOfLifeWindow gw) {
		this.gw = gw;
		
		initialize();
	}
	
	public void initialize() {
		bufferTop = new BufferedImage(gw.getCanvasW(), getTopSize(), BufferedImage.TYPE_INT_RGB);
		bufferBot = new BufferedImage(gw.getCanvasW(), getBotSize(), BufferedImage.TYPE_INT_RGB);
		gTop = bufferTop.getGraphics();
		gBot = bufferBot.getGraphics();
	}
	
	// Render graphics
	public void renderGameSettings() {

		if (!visible)
			return;

		CellWorld cw = gw.getCellWorld();
		CellWorldRenderer cwr = gw.getCellWorldRenderer();
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		// TOP: Game status information
		String txt1 = "";
		txt1 += "Desired FPS: " + gw.getDesiredFps() + " # ";
		txt1 += "Real FPS: " + gw.getRealFps() + "   ";
		txt1 += "Generation: " + cw.getGeneration() + " # ";
		txt1 += "Cells: " + cw.getnCellsAlive() + " # ";
		txt1 += "World-Size: " + cw.getWorldW() + "x" + cw.getWorldH() + " # ";
		txt1 += "Cell-Size: " + df.format(cwr.getCellW()) + "x" + df.format(cwr.getCellH());

		// BOTTOM: Keyboard & Mouse information
		String txt2 = "[i] show/hide this info # [Enter] play  # [Space] pause/step-by-step # [0] kill all # [1-9] randomize";
		String txt3 = "[Arrow keys] resize world # [x/z] +/- speed # [s] save to file # [l] load file # Simulator by: @lionel_ict";

		// Draw Top text
		cleanTopBuffer();
		gTop.setColor(Color.WHITE);
		gTop.setFont(f);
		gTop.drawString(txt1, 0, getTopSize() - 4);
		
		// Draw bottom text
		cleanBotBuffer();
		gBot.setColor(Color.WHITE);
		gBot.setFont(f);
		gBot.drawString(txt2, 0, f.getSize() - 4);
		gBot.drawString(txt3, 0, 2*f.getSize() - 4);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}
	
	public int getTopSize() {
		return f.getSize();
	}
	
	public int getBotSize() {
		return 2*f.getSize();
	}
	
	private void cleanTopBuffer() {
		gTop.setColor(Color.BLACK);
		gTop.fillRect(0, 0, gw.getCanvasW(), getTopSize());
	}
	
	public BufferedImage getTopBuffer() {
		if (!visible)
			cleanTopBuffer();
		return bufferTop;
	}
	
	private void cleanBotBuffer() {
		gBot.setColor(Color.BLACK);
		gBot.fillRect(0, 0, gw.getCanvasW(), getBotSize());
	}
	
	public BufferedImage getBotBuffer() {
		if (!visible)
			cleanBotBuffer();
		return bufferBot;
	}
}
