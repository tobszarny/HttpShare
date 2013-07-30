package pl.biltech.httpshare.view.util;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class ImageUtil {

  public static Image createImageFromFilePath(String path, String description) {
      URL imageURL = ImageUtil.class.getResource(path);
      if (imageURL != null) {
        return (new ImageIcon(imageURL, description)).getImage();
      }
      throw new IllegalStateException("No icon found");
  }
}
