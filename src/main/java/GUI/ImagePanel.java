package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	BufferedImage image;

	public ImagePanel() {
	}

	@Override
	protected void paintComponent(Graphics arg0) {
		if (image != null) {
			super.paintComponent(arg0);
			arg0.drawImage(image, 0, 0, null);
		}
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}