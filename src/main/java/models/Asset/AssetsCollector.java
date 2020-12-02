package models.Asset;


import java.util.ArrayList;
import java.util.List;

import ru.tinkoff.invest.openapi.models.market.Instrument;
import ru.tinkoff.invest.openapi.models.market.InstrumentsList;

import wrappers.WrappedApi;

public class AssetsCollector {

    private final WrappedApi api;
    private final AssetsPages ruAssetsPages;
    private final AssetsPages foreignAssetsPages;
    private final ArrayList<String> allFigies = new ArrayList<>();

    public AssetsCollector() {
        String systemToken = System.getenv("JAVA_TICKERS_TOKEN");
        this.api = new WrappedApi(systemToken);
        ruAssetsPages = new AssetsPages();
        foreignAssetsPages = new AssetsPages();
        fillPages();
    }

    private void fillPages() {
        InstrumentsList listInstruments = api.getAllInstruments();
        List<Instrument> instruments = listInstruments.instruments;
        String currency;
        for (Instrument asset : instruments) {
            allFigies.add(asset.figi);
            currency = asset.currency.name();
            if (currency.equals("RUB"))
                ruAssetsPages.setAsset(asset);
            else
                foreignAssetsPages.setAsset(asset);
        }
    }

    public List<String> getAllFigies() {
        return new ArrayList<>(allFigies);
    }

    public List<AssetInfo> getAssetsPageByNumber(Integer number, AssetPagesType type) {
        List<AssetInfo> info = new ArrayList<>();
        if (type.equals(AssetPagesType.RU)) info = ruAssetsPages.getAssetsPageByNumber(number);
        if (type.equals(AssetPagesType.FOREIGN)) info = foreignAssetsPages.getAssetsPageByNumber(number);
        return info;
    }

    public Boolean IsLastPage(Integer pageNumber, AssetPagesType type) {
        if (type.equals(AssetPagesType.RU))
            return pageNumber.equals(ruAssetsPages.getCountPages());
        return pageNumber.equals(foreignAssetsPages.getCountPages());
    }
}
