import models.Asset.AssetInfo;
import models.Asset.AssetsPages;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

public class AssetsPagesTest {
    private AssetsPages pages;
    private AssetInfo asset;
    @Before
    public void setUp() {
        pages = new AssetsPages();
        asset = mock(AssetInfo.class);
    }

    @Test
    public void setAsset_AddFirstRows_ShouldAddToFirstPage(){
        pages.setAsset(asset);
        List<AssetInfo> expected = List.of(asset);
        Assert.assertEquals(expected, pages.getAssetsPageByNumber(1));
    }

    @Test
    public void setAsset_LastPageIsFull_ShouldAddNewPage(){
        AssetInfo asset = mock(AssetInfo.class);
        for(int i=0;i<pages.getCountRowsPerPage() + 1; i++)
            pages.setAsset(asset);
        Integer expected = 2;
        Assert.assertEquals(expected, pages.getCountPages());
    }




}
