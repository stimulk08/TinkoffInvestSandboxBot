package models.tickers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.models.market.Instrument;
import ru.tinkoff.invest.openapi.models.market.InstrumentsList;

public class TickersCollector {

	private SandboxOpenApi api;
	private HashMap<Integer, List<AssetInfo>> pages;

	public TickersCollector(SandboxOpenApi api) {
		this.api = api;
		pages = new HashMap<Integer, List<AssetInfo>>();
		fillPages();
	}

	private void fillPages() {
		InstrumentsList listInstruments = api.getMarketContext().getMarketStocks().join();
		Integer pageNumber = 1;
		pages.put(pageNumber, new ArrayList<AssetInfo>());
		for (Instrument instrument : listInstruments.instruments) {
			if (pages.get(pageNumber).size() < 10) {
				pages.get(pageNumber).add(new AssetInfo(
						instrument.name, instrument.ticker, instrument.figi));
			}
			if (pages.get(pageNumber).size() == 10) {
				pageNumber++;
				pages.put(pageNumber, new ArrayList<AssetInfo>());
			}
		}
	}

	public List<AssetInfo> getPageByNumber(Integer number) {
		return pages.get(number);
	}
}
