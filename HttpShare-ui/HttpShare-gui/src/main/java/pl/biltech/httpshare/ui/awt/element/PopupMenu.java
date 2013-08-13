package pl.biltech.httpshare.ui.awt.element;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import pl.biltech.httpshare.ui.awt.action.AddFileActionListener;
import pl.biltech.httpshare.ui.awt.action.ExitActionListener;

/**
 * @author bilu
 */
public class PopupMenu extends java.awt.PopupMenu {

	private static final long serialVersionUID = 1L;
	private final AddFileActionListener addFileListener;
	private final ExitActionListener exitListener;
	private MenuItem addFileItem;

	public PopupMenu(AddFileActionListener addFileListener, ExitActionListener exitListener) {
		super();
		this.addFileListener = addFileListener;
		this.exitListener = exitListener;
		add(createMenuAddFile());
		addSeparator();
		add(createMenuAbout());
		add(createMenuExit());
	}

	private MenuItem createMenuAddFile() {
		addFileItem = new MenuItem("Add file...");
		addFileItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
					addFileListener.addFileAction(chooser.getSelectedFile().getAbsoluteFile());
				}
			}
		});
		return addFileItem;
	}

	private MenuItem createMenuExit() {
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitListener.exitAction();
			}
		});
		return exitItem;
	}

	private MenuItem createMenuAbout() {
		MenuItem aboutItem = new MenuItem("About");
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String version = getClass().getPackage().getImplementationVersion();
				JOptionPane.showMessageDialog(null, "Quick file share via HTTP\n\nApache License Version 2.0\n"
						+ "Project url: https://github.com/bilu/HttpShare\nVersion:  " + version);
			}
		});
		return aboutItem;
	}

}
