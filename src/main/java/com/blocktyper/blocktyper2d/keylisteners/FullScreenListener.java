package com.blocktyper.blocktyper2d.keylisteners;

import java.awt.GraphicsDevice;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class FullScreenListener implements KeyListener{
	
	private int keyCode;
	private boolean isFullScreen;
	
	private JFrame frame;
	private GraphicsDevice device;
	
	public FullScreenListener(int keyCode, JFrame frame, GraphicsDevice device){
		this.keyCode = keyCode;
		this.frame = frame;
		this.device = device;
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == keyCode){
			toggleFullscreen();
		}
	}
	public void keyReleased(KeyEvent arg0) {
		
	}
	public void keyTyped(KeyEvent arg0) {
		
	}
	
	
	
	
	public boolean isFullScreen() {
		return isFullScreen;
	}

	//BEGIN PRIVATE HELPERS
	private void toggleFullscreen()
    {
		isFullScreen = !isFullScreen;
        frame.setVisible(false);
        frame.dispose();
        frame.setUndecorated(isFullScreen);
        if(isFullScreen)
        {
            device.setFullScreenWindow(frame);
            frame.validate();
        }
        else
        {
        	device.setFullScreenWindow(null);
        	frame.setVisible(true);
        }
    }
	//END PRIVATE HELPERS
}
