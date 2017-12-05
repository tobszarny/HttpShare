package pl.biltech.httpshare.ui.awt;

import pl.biltech.httpshare.ui.awt.element.Tray;

import javax.swing.*;
import java.io.File;

public class Startup {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tray tray = new Tray();
            if (args.length == 1) {
                tray.addFileAction(new File(args[0]));
            }
        });
    }
}
