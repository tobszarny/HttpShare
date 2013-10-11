package pl.biltech.httpshare.ui.awt.element;

import java.awt.Image;

import pl.biltech.httpshare.ui.awt.util.ImageUtil;

/**
 * @author bilu
 */
public enum Icon {
	DEFAULT("/images/ico.png", "Waiting"), DOWNLOADING("/images/downloading.png", "Downloading"), UPLOADING(
			"/images/uploading.png", "Uploading"), PAUSED(
			"/images/pause.png",
			"Paused");

	private final String path;
	private final String description;

	private Icon(String path, String description) {
		this.path = path;
		this.description = description;
	}

	public Image getImage() {
		return ImageUtil.createImageFromFilePath(path, description);
	}

	public String getDescription() {
		return description;
	}

}
