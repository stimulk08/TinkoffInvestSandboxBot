package models.Asset;

import ru.tinkoff.invest.openapi.models.market.Instrument;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AssetsPages {
    private final ConcurrentHashMap<Integer, ArrayList<AssetInfo>> pages;
    public static final Integer countRowsPerPage = 10;
    public AssetsPages(){
        pages = new ConcurrentHashMap<>();
        pages.put(1, new ArrayList<>());
    }

    public List<AssetInfo> getAssetsPageByNumber(Integer number){
        return pages.get(number);
    }

    public Integer getCountRowsPage(Integer pageNumber){
        return pages.get(pageNumber).size();
    }

    public Integer getCountPages() {return pages.size();}

    public Integer getCountRowsPerPage(){return countRowsPerPage;}

    public void setAsset(AssetInfo asset){
        Integer count = pages.size();
        Integer lastPageRowCount = getCountRowsPage(count);
        if (lastPageRowCount < countRowsPerPage) {
            pages.get(count).add(asset);
        }else if (lastPageRowCount.equals(countRowsPerPage)) {
            pages.put(count + 1, new ArrayList<>());
        }
    }
}
