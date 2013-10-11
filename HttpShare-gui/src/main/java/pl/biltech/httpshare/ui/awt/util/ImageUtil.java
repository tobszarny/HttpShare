package pl.biltech.httpshare.ui.awt.util;

import static java.lang.String.format;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * @author bilu
 * 
 */
public class ImageUtil {

	public static Image createImageFromFilePath(String path, String description) {
		URL imageURL = ImageUtil.class.getResource(path);
		if (imageURL != null) {
			return (new ImageIcon(imageURL, description)).getImage();
		}
		throw new IllegalStateException(format("No icon found for path='%s', description='%s'", path, description));
	}
}
