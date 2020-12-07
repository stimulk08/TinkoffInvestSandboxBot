import handlers.ChoosePortfolioHandler;
import models.Asset.AssetInfo;
import models.Asset.AssetsPages;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.tinkoff.invest.openapi.models.market.Instrument;
import wrappers.WrappedUpdate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AssetsPagesTest {
    private AssetsPages pages;
    @Before
    public void setUp() {
        pages = new AssetsPages();
    }

    @Test
    public void test1(){
        AssetInfo asset = mock(AssetInfo.class);
        pages.setAsset(asset);
        List<AssetInfo> expected = List.of(asset);
        Assert.assertEquals(expected, pages.getAssetsPageByNumber(1));
    }

    @Test
    public void test2(){
        AssetInfo asset = mock(AssetInfo.class);
        for(int i=0;i<pages.getCountRowsPerPage() + 1; i++)
            pages.setAsset(asset);
        Integer expected = 2;
        Assert.assertEquals(expected, pages.getCountPages());
    }




}
