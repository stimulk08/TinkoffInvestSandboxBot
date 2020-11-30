package models.tickers;

public class AssetInfo {

	private final String fullName;
	private final String ticker;
	private final String figi;

	public AssetInfo(String fullName, String ticker, String figi) {
		this.fullName = fullName;
		this.ticker = ticker;
		this.figi = figi;
	}

	public String getFullName() {
		return fullName;
	}

	public String getTicker() {
		return ticker;
	}

	public String getFigi() {
		return figi;
	}
}
