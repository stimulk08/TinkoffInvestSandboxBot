package wrappers;

import com.google.common.collect.Iterables;
import handlers.AuthorizationHandler;
import ru.tinkoff.invest.openapi.SandboxContext;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.exceptions.WrongTokenException;
import ru.tinkoff.invest.openapi.models.Currency;
import ru.tinkoff.invest.openapi.models.market.*;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;
import ru.tinkoff.invest.openapi.models.sandbox.CurrencyBalance;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApiFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class WrappedApi {
    private SandboxOpenApi api;
    private Logger logger;

    public WrappedApi(String token) {
        initLogger();
        initApi(token);
    }

    private void initApi(String token) {
        OkHttpOpenApiFactory factory = new OkHttpOpenApiFactory(token, logger);
        api = factory.createSandboxOpenApiClient(
                Executors.newSingleThreadExecutor());

    }

    private void initLogger() {
        try {
            logger = getLogger();
        } catch (IOException ex) {
            System.err.println("При инициализации логгера произошла ошибка: "
                    + ex.getLocalizedMessage());
        }
    }

    private static Logger getLogger() throws IOException {
        final LogManager logManager = LogManager.getLogManager();
        final ClassLoader classLoader = AuthorizationHandler.class.getClassLoader();

        try (final InputStream input = classLoader.getResourceAsStream("logging.properties")) {

            if (input == null) {
                throw new FileNotFoundException();
            }

            Files.createDirectories(Paths.get("./logs"));
            logManager.readConfiguration(input);
        }

        return Logger.getLogger(AuthorizationHandler.class.getName());
    }

    public Boolean checkTokenValidity() {
        boolean isValidToken = false;
        try {
            api.getSandboxContext().performRegistration(null).get();
            isValidToken = true;
        } catch (ExecutionException e) {
            if (e.getCause() instanceof WrongTokenException) {
                try {
                    api.close();
                } catch (Exception ex) {
                    e.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isValidToken;
    }

    public BigDecimal getLastPriceAsset(String figi) {
        OffsetDateTime currentTime = OffsetDateTime.now();

        Optional<HistoricalCandles> historicalCandles = api
                .getMarketContext()
                .getMarketCandles(figi, currentTime.minusDays(1),
                        OffsetDateTime.now(), CandleInterval.ONE_MIN).join();

        List<Candle> candles = new ArrayList<>();
        if (historicalCandles.isPresent())
            candles = historicalCandles.get().candles;

        Candle lastCandle = Iterables.getLast(candles);
        return lastCandle.openPrice;
    }

    public void setBalanceUSD(BigDecimal amountUSD) {
        CurrencyBalance currencyBalanceUSD = new CurrencyBalance(
                Currency.USD, amountUSD);
        SandboxContext context = api.getSandboxContext();
        context.clearAll(null).join();
        context.setCurrencyBalance(currencyBalanceUSD, null).join();
    }

    public InstrumentsList getAllInstruments() {
        return api
                .getMarketContext()
                .getMarketStocks()
                .join();
    }

    public InstrumentsList getInstrumentsByTicker(String ticker) {
        return api
                .getMarketContext()
                .searchMarketInstrumentsByTicker(ticker).join();
    }

    public String getCurrencyByFigi(String figi) {
        Optional<Instrument> instr = api
                .getMarketContext()
                .searchMarketInstrumentByFigi(figi).join();
        String currency = "";
        if (instr.isPresent())
            currency = instr.get().currency.name();
        return currency;

    }

    public Portfolio getPortfolio() {
        return api.getPortfolioContext().getPortfolio(null).join();
    }
}
