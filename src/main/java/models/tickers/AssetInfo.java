package models.tickers;

public class AssetInfo {
	private String fullName;
	private String ticker;
	private String figi;

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
