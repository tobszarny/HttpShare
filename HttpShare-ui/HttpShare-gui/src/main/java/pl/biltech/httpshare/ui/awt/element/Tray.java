package pl.biltech.httpshare.ui.awt.element;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.io.File;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltech.httpshare.ui.awt.action.AddFileActionListener;
import pl.biltech.httpshare.ui.awt.action.ExitActionListener;

/**
 * @author bilu
 * 
 */
public class Tray implements AddFileActionListener, ExitActionListener {

	private static final Logger logger = LoggerFactory.getLogger(Tray.class);


	// private TrayIcon trayIcon;
	private SystemTray systemTray;

	private pl.biltech.httpshare.ui.awt.element.TrayIcon trayIcon;
	
	// private String currentStatus = "";

	
	// public AwtTray(File file) {
	public Tray() {
		createMenu();
		// download(null);
	}

	// private void download(File file) {
	// // if(isValidFile(file)) {
	// // try {
	// // new Download(file, this).startServer();
	// // addFileItem.setEnabled(false);
	// // } catch (IOException e) {
	// // displayMessageError("Server start problem", e.getMessage());
	// // currentStatus = "File not chosen";
	// // }
//		}
	// // else {
	// // displayMessageWarning("File not found",
	// "Path to file is incorrect"+((file != null)?
	// " '"+file.getAbsolutePath()+"'" : ""));
	// // currentStatus = "File not chosen";
	// // }
	// }
	
	private void createMenu() {

		if (!SystemTray.isSupported()) {
			String message = "SystemTray is not supported";
			logger.error(message);
			JOptionPane.showMessageDialog(null, message);
			return;
		}

		// trayIcon = new
		// TrayIcon(ImageUtil.createImageFromFilePath("/images/ico.png",
		// "Choose file"));
		// trayIcon.setImageAutoSize(true);
		systemTray = SystemTray.getSystemTray();

		PopupMenu popupMenu = new PopupMenu(this, this);
		trayIcon = new pl.biltech.httpshare.ui.awt.element.TrayIcon(popupMenu);
		
		// trayIcon.setPopupMenu(popup);
		// trayIcon.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// displayMessage("Status:", currentStatus);
		// }
		// });
		//
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e) {
			JOptionPane.showMessageDialog(null, "TrayIcon could not be added.");
			return;
		}
	}


	// public void displayMessage(String title, String message) {
	// trayIcon.displayMessage(title, message, TrayIcon.MessageType.NONE);
	// }
	//
	// public void displayMessageWarning(String title, String message) {
	// trayIcon.displayMessage(title, message, TrayIcon.MessageType.WARNING);
	// }
	//
	// public void displayMessageError(String title, String message) {
	// trayIcon.displayMessage(title, message, TrayIcon.MessageType.ERROR);
	// }
	//
	// public void displayMessageInfo(String title, String message) {
	// trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
	// }
	//
	// public void setStatus(String message) {
	// currentStatus = message;
	// }
	//
	// public void setIcon(Image image) {
	// trayIcon.setImage(image);
	// }

	@Override
	public void exitAction() {
		systemTray.remove(trayIcon);
		System.exit(0);
	}

	@Override
	public void addFileAction(File file) {
		// TODO create server and add file
		System.out.println(file.getAbsolutePath());
	}
	
}
