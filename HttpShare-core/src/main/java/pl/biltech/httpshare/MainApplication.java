package pl.biltech.httpshare;

import java.io.File;

import javax.swing.SwingUtilities;

import pl.biltech.httpshare.view.HttpShareTray;

public class MainApplication {

	public static void main(final String[] args) {

		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (args == null || args.length == 0) {
					new HttpShareTray(null);
				}
				else {
					new HttpShareTray(new File(args[0]));
				}
			}
		});
	}
}
