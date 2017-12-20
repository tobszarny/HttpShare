package pl.biltech.httpshare.eventbus.subscriber.impl;

import pl.biltech.httpshare.eventbus.event.Event;
import pl.biltech.httpshare.eventbus.event.impl.DownloadStartedEvent;
import pl.biltech.httpshare.eventbus.subscriber.EventSubscriber;
import pl.biltech.httpshare.ui.awt.element.Icon;
import pl.biltech.httpshare.ui.awt.element.TrayIcon;


public class DownloadStartedEventSubscriber implements EventSubscriber<DownloadStartedEvent> {

    private TrayIcon trayIcon;

    public DownloadStartedEventSubscriber(TrayIcon trayIcon) {
        this.trayIcon = trayIcon;
    }

    @Override
    public void handleEvent(DownloadStartedEvent event) {
        trayIcon.displayInfo("Download started", event.getMessage());
        trayIcon.iconChangeAction(Icon.DOWNLOADING);
    }

    @Override
    public boolean isEventAMatch(Event event) {
        return false;
    }
}
