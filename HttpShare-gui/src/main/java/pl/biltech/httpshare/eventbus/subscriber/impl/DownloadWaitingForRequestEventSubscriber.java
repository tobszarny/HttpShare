package pl.biltech.httpshare.eventbus.subscriber.impl;

import pl.biltech.httpshare.eventbus.event.Event;
import pl.biltech.httpshare.eventbus.event.impl.DownloadWaitingForRequestEvent;
import pl.biltech.httpshare.eventbus.subscriber.EventSubscriber;
import pl.biltech.httpshare.ui.awt.element.TrayIcon;

public class DownloadWaitingForRequestEventSubscriber implements EventSubscriber<DownloadWaitingForRequestEvent> {

    private TrayIcon trayIcon;

    public DownloadWaitingForRequestEventSubscriber(TrayIcon trayIcon) {
        this.trayIcon = trayIcon;
    }

    @Override
    public void handleEvent(DownloadWaitingForRequestEvent event) {
        trayIcon.displayInfo("File ready to download", "Download url: " + event.getUrl());
    }

    @Override
    public boolean isEventAMatch(Event event) {
        return false;
    }
}

