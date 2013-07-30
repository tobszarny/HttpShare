package pl.biltech.httpshare.view;

import java.awt.Image;

public interface Tray {

	public abstract void displayMessage(String title, String message);

	public void exit();
	
	public void setStatus(String message);

	void setIcon(Image image);

}