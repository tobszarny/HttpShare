package pl.biltech.httpshare.ui.awt.element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.biltech.httpshare.HttpShare;
import pl.biltech.httpshare.eventbus.event.Event;
import pl.biltech.httpshare.eventbus.manager.EventManager;
import pl.biltech.httpshare.eventbus.manager.impl.AsyncEventManager;
import pl.biltech.httpshare.eventbus.subscriber.EventSubscriber;
import pl.biltech.httpshare.eventbus.subscriber.impl.DownloadFinishedEventSubscriber;
import pl.biltech.httpshare.eventbus.subscriber.impl.DownloadProgressNotificationEventSubscriber;
import pl.biltech.httpshare.eventbus.subscriber.impl.DownloadStartedEventSubscriber;
import pl.biltech.httpshare.eventbus.subscriber.impl.DownloadWaitingForRequestEventSubscriber;
import pl.biltech.httpshare.ui.awt.action.AddFileActionListener;
import pl.biltech.httpshare.ui.awt.action.ExitActionListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bilu
 */
public class Tray implements AddFileActionListener, ExitActionListener {

    private static final Logger logger = LoggerFactory.getLogger(Tray.class);

    private SystemTray systemTray;
    private TrayIcon trayIcon;
    private final HttpShare httpShare = new HttpShare();
    private final EventManager eventManager = AsyncEventManager.INSTANCE;

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

        httpShare.start();

        trayIcon.displayInfo("HttpShare", "Application started");

        for (EventSubscriber<? extends Event> eventSubscriber : createEventSubscribers()) {
            eventManager.addEventSubscriber(eventSubscriber);
        }
    }

    private List<EventSubscriber<? extends Event>> createEventSubscribers() {
        List<EventSubscriber<? extends Event>> subscribers = new ArrayList<EventSubscriber<? extends Event>>();
        subscribers.add(new DownloadStartedEventSubscriber(trayIcon));
        subscribers.add(new DownloadFinishedEventSubscriber(trayIcon));
        subscribers.add(new DownloadProgressNotificationEventSubscriber(trayIcon));
        subscribers.add(new DownloadWaitingForRequestEventSubscriber(trayIcon));
        return subscribers;
    }

//	public static void main(String[] args) {
//		Tray tray = new Tray();
//		for (int i = 0; i <= 100; i++) {
//			System.out.println(tray.getPercentAsTextProgressBar(i));
//		}
//	}

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
