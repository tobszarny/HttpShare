package pl.biltech.httpshare.view;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import pl.biltech.httpshare.model.Download;
import pl.biltech.httpshare.view.util.ImageUtil;

public class HttpShareTray implements Tray {

	private TrayIcon trayIcon;
	private SystemTray systemTray;
	private PopupMenu popup;
	private MenuItem addFileItem;
	
	private String currentStatus = "";

	
	public HttpShareTray(File file) {
		createMenu();
		download(file);
	}

	private void download(File file) {
		if(isValidFile(file)) {
			try {
				new Download(file, this).startServer();
				addFileItem.setEnabled(false);
			} catch (IOException e) {
				displayMessageError("Server start problem", e.getMessage());
				currentStatus = "File not chosen";
			}
		}
		else {
			displayMessageWarning("File not found", "Path to file is incorrect"+((file != null)? " '"+file.getAbsolutePath()+"'" : ""));
			currentStatus = "File not chosen";
		}
	}
	
	private boolean isValidFile(File file) {
		return file != null && file.exists() && file.isFile();
	}

	private void createMenu() {

		if (!SystemTray.isSupported()) {
			JOptionPane.showMessageDialog(null, "SystemTray is not supported");
			return;
		}

		trayIcon = new TrayIcon(ImageUtil.createImageFromFilePath("/images/ico.png", "Choose file"));
		trayIcon.setImageAutoSize(true);
		systemTray = SystemTray.getSystemTray();

		popup = new PopupMenu();
		popup.add(createAddFileMenu());
	    popup.addSeparator();
		popup.add(createAboutMenu());
		popup.add(createExitMenu());
		
		trayIcon.setPopupMenu(popup);
		trayIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayMessage("Status:", currentStatus);
			}
		});

		try {
			systemTray.add(trayIcon);
		} catch (AWTException e) {
			JOptionPane.showMessageDialog(null, "TrayIcon could not be added.");
			return;
		}
	}

	private MenuItem createAboutMenu() {
		MenuItem aboutItem = new MenuItem("About");
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(null,
								"Quick file share via HTTP\n\n" +
								"Free to use for everyone\n\n" +
								"Author:   Paweł Bilewicz\n" +
								"Version:  0.2\n" +
								"Email:    pawel.bilewicz@gmail.com");
				
			}
		});
		return aboutItem;
	}
	private MenuItem createAddFileMenu() {
		addFileItem = new MenuItem("Add file...");
		addFileItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
					download(chooser.getSelectedFile().getAbsoluteFile());
				}
			}
		});
		return addFileItem;
	}

	private MenuItem createExitMenu() {
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		return exitItem;
	}

	/* (non-Javadoc)
	 * @see pl.biltech.httpshare.view.TrayMessageDisplayer#displayMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public void displayMessage(String title, String message) {
		trayIcon.displayMessage(title, message, TrayIcon.MessageType.NONE);
	}

	public void displayMessageWarning(String title, String message) {
		trayIcon.displayMessage(title, message, TrayIcon.MessageType.WARNING);
	}

	public void displayMessageError(String title, String message) {
		trayIcon.displayMessage(title, message, TrayIcon.MessageType.ERROR);
	}

	public void displayMessageInfo(String title, String message) {
		trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
	}

	@Override
	public void exit() {
		systemTray.remove(trayIcon);
		System.exit(0);
	}

	@Override
	public void setStatus(String message) {
		currentStatus = message;
	}
	
	public void setIcon(Image image) {
		trayIcon.setImage(image);
	}
	
}
