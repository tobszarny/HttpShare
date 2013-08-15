package pl.biltech.httpshare.ui.awt.element;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.io.File;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltech.httpshare.HttpShare;
import pl.biltech.httpshare.ui.awt.action.AddFileActionListener;
import pl.biltech.httpshare.ui.awt.action.ExitActionListener;

/**
 * @author bilu
 * 
 */
public class Tray implements AddFileActionListener, ExitActionListener {

	private static final Logger logger = LoggerFactory.getLogger(Tray.class);

	private SystemTray systemTray;
	private final HttpShare httpShare = new HttpShare();
	private TrayIcon trayIcon;
	
	public Tray() {
		if (!SystemTray.isSupported()) {
			String message = "SystemTray is not supported";
			logger.error(message);
			JOptionPane.showMessageDialog(null, message);
			return;
		}
		systemTray = SystemTray.getSystemTray();
		PopupMenu popupMenu = new PopupMenu(this, this);
		trayIcon = new TrayIcon(popupMenu);
		
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e) {
			JOptionPane.showMessageDialog(null, "TrayIcon could not be added.");
			return;
		}
		trayIcon.displayInfo("HttpShare", "Application started");
	}

	@Override
	public void exitAction() {
		systemTray.remove(trayIcon);
		System.exit(0);
	}

	@Override
	public void addFileAction(File file) {
		if (!httpShare.isStarted()) {
			httpShare.start();
		}
		httpShare.addFileToDownload(file);
	}
	
}
