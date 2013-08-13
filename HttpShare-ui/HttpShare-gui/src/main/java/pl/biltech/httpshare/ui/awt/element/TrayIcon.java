package pl.biltech.httpshare.ui.awt.element;

import static pl.biltech.httpshare.ui.awt.element.Icon.DEFAULT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.biltech.httpshare.ui.awt.action.IconChangeActionListener;


/**
 * @author bilu
 * 
 */
public class TrayIcon extends java.awt.TrayIcon implements IconChangeActionListener {

	private String currentStatus = "";

	public TrayIcon(PopupMenu popupMenu) {
		super(DEFAULT.getImage());

		setImageAutoSize(true);
		setPopupMenu(popupMenu);
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayMessage("Status:", currentStatus);
			}
		});
	}

	// TODO consider remove
	private void displayMessage(String title, String message) {
		displayMessage(title, message, TrayIcon.MessageType.NONE);
	}

	// TODO consider remove
	private void displayMessageWarning(String title, String message) {
		displayMessage(title, message, TrayIcon.MessageType.WARNING);
	}

	// TODO consider remove
	private void displayMessageError(String title, String message) {
		displayMessage(title, message, TrayIcon.MessageType.ERROR);
	}

	// TODO consider remove
	private void displayMessageInfo(String title, String message) {
		displayMessage(title, message, TrayIcon.MessageType.INFO);
	}

	@Override
	public void iconChangeAction(Icon icon) {
		setImage(icon.getImage());
		currentStatus = icon.getDescription();
		displayStatus();
	}

	public void displayStatus() {
		displayMessage("Status:", currentStatus);
	}


}
