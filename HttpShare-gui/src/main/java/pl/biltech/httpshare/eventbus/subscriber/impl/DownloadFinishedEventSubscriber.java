package pl.biltech.httpshare.eventbus.subscriber.impl;

import pl.biltech.httpshare.eventbus.event.Event;
import pl.biltech.httpshare.eventbus.event.impl.DownloadFinishedEvent;
import pl.biltech.httpshare.eventbus.subscriber.EventSubscriber;
import pl.biltech.httpshare.ui.awt.element.Icon;
import pl.biltech.httpshare.ui.awt.element.TrayIcon;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class DownloadFinishedEventSubscriber implements EventSubscriber<DownloadFinishedEvent> {

    private TrayIcon trayIcon;

    public DownloadFinishedEventSubscriber(TrayIcon trayIcon) {
        this.trayIcon = trayIcon;
    }

    @Override
    public void handleEvent(DownloadFinishedEvent event) {
        trayIcon.displayInfo("Download finished", event.getMessage());
        trayIcon.iconChangeAction(Icon.DEFAULT);
    }

    @Override
    public boolean isEventAMatch(Event event) {
        Type type = this.getClass().getGenericInterfaces()[0];
        Type genenericParameterType = ((ParameterizedType) type).getActualTypeArguments()[0];
        return event.getClass().equals(genenericParameterType);
    }
}