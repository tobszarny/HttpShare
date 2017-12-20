package pl.biltech.httpshare.eventbus.subscriber.impl;

import pl.biltech.httpshare.eventbus.event.Event;
import pl.biltech.httpshare.eventbus.event.impl.DownloadProgressNotificationEvent;
import pl.biltech.httpshare.eventbus.subscriber.EventSubscriber;
import pl.biltech.httpshare.ui.awt.element.TrayIcon;

public class DownloadProgressNotificationEventSubscriber implements EventSubscriber<DownloadProgressNotificationEvent> {

    private TrayIcon trayIcon;

    public DownloadProgressNotificationEventSubscriber(TrayIcon trayIcon) {
        this.trayIcon = trayIcon;
    }

    @Override
    public void handleEvent(DownloadProgressNotificationEvent event) {
        trayIcon.displayInfo("Download in progress", "" + getPercentAsTextProgressBar(event.getPercent()));
    }

    /**
     * @param percent
     * @return String with simple progress bar, sample for 88
     * <p>
     * <pre>
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

    @Override
    public boolean isEventAMatch(Event event) {
        return false;
    }
}