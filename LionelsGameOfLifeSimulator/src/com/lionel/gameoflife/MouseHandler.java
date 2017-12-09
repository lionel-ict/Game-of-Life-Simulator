package com.lionel.gameoflife;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {
	
	// If a click happened
	private boolean click = false;
	
	// Position of last click
	private int x = -1;
	private int y = -1;
	
	public MouseHandler(Component c) {
		c.addMouseListener(this);
	}
	
	public boolean isClicked() {
		return click;
	}
	
	public void releaseClick() {
		click = false;
		x = -1;
		y = -1;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	private void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void mousePressed(MouseEvent e) {
		//showDebug("Mouse pressed; # of clicks: " + e.getClickCount(), e);
	}

	public void mouseReleased(MouseEvent e) {
		//showDebug("Mouse released; # of clicks: " + e.getClickCount(), e);
	}

	public void mouseEntered(MouseEvent e) {
		//showDebug("Mouse entered", e);
	}

	public void mouseExited(MouseEvent e) {
		//showDebug("Mouse exited", e);
	}

	public void mouseClicked(MouseEvent e) {
		//showDebug("Mouse clicked (# of clicks: " + e.getClickCount() + ")", e);
		click = true;
		setXY(e.getX(), e.getY());
	}

	void showDebug(String eventDescription, MouseEvent e) {
		System.out.println(eventDescription + " detected on " + e.getComponent().getClass().getName() + ".");
	}
}