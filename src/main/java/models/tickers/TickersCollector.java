package models.tickers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.models.market.Instrument;
import ru.tinkoff.invest.openapi.models.market.InstrumentsList;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApiFactory;

public class TickersCollector {

	private final SandboxOpenApi api;
	private final HashMap<Integer, List<AssetInfo>> pages;

	public TickersCollector() {
		this.api = getApi();
		pages = new HashMap<>();
		fillPages();
	}

	private SandboxOpenApi getApi() {
		final Logger logger;
		try {
			logger = initLogger();
		} catch (IOException ex) {
			System.err.println("При инициализации логгера произошла ошибка: "
					+ ex.getLocalizedMessage());
			return null;
		}

		OkHttpOpenApiFactory factory = new OkHttpOpenApiFactory(
				System.getenv("JAVA_TICKERS_TOKEN"), logger);
		return factory.createSandboxOpenApiClient(
				Executors.newSingleThreadExecutor());
	}

	private static Logger initLogger() throws IOException {
		final LogManager logManager = LogManager.getLogManager();
		final ClassLoader classLoader = TickersCollector.class.getClassLoader();

		try (final InputStream input = classLoader.getResourceAsStream("logging.properties")) {

			if (input == null) {
				throw new FileNotFoundException();
			}

			Files.createDirectories(Paths.get("./logs"));
			logManager.readConfiguration(input);
		}

		return Logger.getLogger(TickersCollector.class.getName());
	}

	private void fillPages() {
		InstrumentsList listInstruments = api.getMarketContext().getMarketStocks().join();
		Integer pageNumber = 1;
		pages.put(pageNumber, new ArrayList<>());
		for (Instrument instrument : listInstruments.instruments) {
			if (pages.get(pageNumber).size() < 10) {
				pages.get(pageNumber).add(new AssetInfo(
						instrument.name, instrument.ticker, instrument.figi));
			}
			if (pages.get(pageNumber).size() == 10) {
				pageNumber++;
				pages.put(pageNumber, new ArrayList<>());
			}
		}
	}

	public List<AssetInfo> getPageByNumber(Integer number) {
		return pages.get(number);
	}
}
