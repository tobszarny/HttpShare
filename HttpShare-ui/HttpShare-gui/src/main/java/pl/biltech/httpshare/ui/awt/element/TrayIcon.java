package pl.biltech.httpshare.ui.awt.element;

import static pl.biltech.httpshare.ui.awt.element.Icon.DEFAULT;
import pl.biltech.httpshare.ui.awt.action.IconChangeActionListener;


/**
 * @author bilu
 * 
 */
public class TrayIcon extends java.awt.TrayIcon implements IconChangeActionListener {

	private String currentStatus = "Wating";

	public TrayIcon(PopupMenu popupMenu) {
		super(DEFAULT.getImage());

		setImageAutoSize(true);
		setPopupMenu(popupMenu);

		// addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// displayMessage("Status:", currentStatus);
		// }
		// });
	}

	public void displayMessage(String title, String message) {
		displayMessage(title, message, TrayIcon.MessageType.NONE);
	}

	public void displayWarning(String title, String message) {
		displayMessage(title, message, TrayIcon.MessageType.WARNING);
	}

	public void displayError(String title, String message) {
		displayMessage(title, message, TrayIcon.MessageType.ERROR);
	}

	public void displayInfo(String title, String message) {
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
