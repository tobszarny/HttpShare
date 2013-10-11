package pl.biltech.httpshare.ui.awt;

import java.io.File;

import javax.swing.SwingUtilities;

import pl.biltech.httpshare.ui.awt.element.Tray;

public class Startup {

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Tray tray = new Tray();
				if (args.length == 1) {
					tray.addFileAction(new File(args[0]));
				}
			}
		});
	}
}
