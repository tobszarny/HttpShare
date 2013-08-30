package pl.biltech.httpshare.ui.awt.element;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltech.httpshare.HttpShare;
import pl.biltech.httpshare.event.Event;
import pl.biltech.httpshare.event.EventPublisher;
import pl.biltech.httpshare.event.EventSubscriber;
import pl.biltech.httpshare.event.impl.DownloadFinishedEvent;
import pl.biltech.httpshare.event.impl.DownloadProgressEvent;
import pl.biltech.httpshare.event.impl.DownloadStartedEvent;
import pl.biltech.httpshare.event.impl.SimpleEventPublisher;
import pl.biltech.httpshare.ui.awt.action.AddFileActionListener;
import pl.biltech.httpshare.ui.awt.action.ExitActionListener;

/**
 * @author bilu
 * 
 */
public class Tray implements AddFileActionListener, ExitActionListener {

	private static final Logger logger = LoggerFactory.getLogger(Tray.class);

	private SystemTray systemTray;
	private TrayIcon trayIcon;
	private final HttpShare httpShare = new HttpShare();
	private final EventPublisher eventPublisher = SimpleEventPublisher.INSTANCE;
	private List<EventSubscriber<? extends Event>> subscribers;
	
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

		subscribers = createEventSubscribers();
		eventPublisher.addEventSubscribers(subscribers);
	}

	private List<EventSubscriber<? extends Event>> createEventSubscribers() {
		List<EventSubscriber<? extends Event>> subscribers = new ArrayList<EventSubscriber<? extends Event>>();
		subscribers.add(new EventSubscriber<DownloadStartedEvent>() {
			@Override
			public void handleEvent(DownloadStartedEvent event) {
				trayIcon.displayInfo("Download started", event.getMessage());
				trayIcon.iconChangeAction(Icon.DOWNLOADING);
			}
		});
		subscribers.add(new EventSubscriber<DownloadFinishedEvent>() {
			@Override
			public void handleEvent(DownloadFinishedEvent event) {
				trayIcon.displayInfo("Download finished", event.getMessage());
				trayIcon.iconChangeAction(Icon.DEFAULT);
			}
		});
		subscribers.add(new EventSubscriber<DownloadProgressEvent>() {
			@Override
			public void handleEvent(DownloadProgressEvent event) {
				trayIcon.displayInfo("Download in progress", "" + getPercentAsTextProgressBar(event.getPercent()));
			}
		});
		return subscribers;
	}

	/**
	 * @param percent
	 * @return String with simple progress bar, sample for 88
	 * 
	 *         <pre>
	 * [||||||||||||||||||||||||||||||||||||||||||||      ] 88%
	 * </pre>
	 */
	private String getPercentAsTextProgressBar(int percent) {
		int maxChar = percent / 2;
		StringBuilder sb = new StringBuilder("[");
		for (int i = 1; i <= 50; i++) {
			if (i <= maxChar) {
				sb.append("|");
			} else {
				sb.append(" ");
			}
		}
		sb.append("] ").append(percent).append("%");
		return sb.toString();
	}

	public static void main(String[] args) {
		Tray tray = new Tray();
		for (int i = 0; i <= 100; i++) {
			System.out.println(tray.getPercentAsTextProgressBar(i));
		}
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
